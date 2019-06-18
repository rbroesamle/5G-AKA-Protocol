package Implementation;

import Implementation.protocol.entities.UE;
import Implementation.structure.Entity;
import Implementation.structure.Message;
import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;

public class App {

    public static void main(String[] args) {
        ownImplementation();
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
            entities[i] = new UE();
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
