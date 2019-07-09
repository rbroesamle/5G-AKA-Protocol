package Implementation;

import Implementation.protocol.entities.AUSF;
import Implementation.protocol.entities.SEAF;
import Implementation.protocol.entities.UDM;
import Implementation.protocol.entities.UE;
import Implementation.structure.Entity;
import Implementation.structure.Message;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class App {

    public static void main(String[] args) {
        runProtocol();
    }

    private static void runProtocol() {
        UE ue = new UE(null, null, null);
        SEAF seaf = new SEAF();
        AUSF ausf = new AUSF();
        UDM udm = new UDM();

        seaf.ausf = ausf;
        seaf.ue = ue;
        ausf.udm = udm;
        ausf.seaf = seaf;

        ue.initiateAuthentication(seaf);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private static void ownImplementation() {
        int numberOfMessages = 100;
        Entity[] entities = new UE[numberOfMessages];
        Message[] messages = new Message[numberOfMessages];
        Entity seaf = new Entity() {
            int i = -1;

            @Override
            public String getName() {
                return null;
            }

            @Override
            public void onReceiveMessage(Message message, Entity sender) {
                print(message);
            }

            private synchronized void print(Message message) {
                Integer n = new Integer(message.getName());
                if (i < n) {
                    i = n;
                    System.out.println(message.getName());
                } else {
                    System.err.println(message.getName());
                }
            }
        };

        for (int i = 0; i < numberOfMessages; i++) {
            final int j = i;
            entities[i] = new UE(null, null, null);
            messages[i] = () -> "" + j;
            entities[i].prepareMessage(messages[i], seaf);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i < numberOfMessages; i++) {
            entities[i].sendMessage(messages[i], seaf);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private static void flowable() {
        try {
            Flowable.range(1, 10)
                    .parallel()
                    .runOn(Schedulers.newThread())
                    .sequential()
                    .subscribe(System.out::println);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
