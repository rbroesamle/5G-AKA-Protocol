package Implementation;

import Implementation.helper.Generator;
import Implementation.protocol.additional.ParameterLength;
import Implementation.protocol.entities.AUSF;
import Implementation.protocol.entities.SEAF;
import Implementation.protocol.entities.UDM;
import Implementation.protocol.entities.UE;
import Implementation.structure.Entity;
import Implementation.structure.Message;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.security.*;

public class App {

    public static void main(String[] args) {
        runProtocol();
    }

    private static void runProtocol() {
        byte[] K = Generator.randomBytes(ParameterLength.K);
        byte[] SUPI = Generator.randomBytes(ParameterLength.K);
        byte[] SNN = Generator.randomBytes(ParameterLength.K);
        byte[] AMF = Generator.randomBytes(ParameterLength.AMF);

        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(512);
            KeyPair pair = generator.generateKeyPair();
            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        UE ue = new UE(K, SUPI, publicKey);
        SEAF seaf = new SEAF(SNN);
        AUSF ausf = new AUSF();
        UDM udm = new UDM(K, AMF, privateKey);

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
