package Implementation;

import Implementation.helper.Generator;
import Implementation.protocol.additional.ParameterLength;
import Implementation.protocol.entities.AUSF;
import Implementation.protocol.entities.SEAF;
import Implementation.protocol.entities.UDM;
import Implementation.protocol.entities.UE;

import java.security.*;

public class App {

    public static void main(String[] args) {
        runProtocol();
    }

    private static byte[] K = Generator.randomBytes(ParameterLength.K);
    private static byte[] SUPI = Generator.randomBytes(ParameterLength.K);
    private static byte[] SNN = Generator.randomBytes(ParameterLength.K);
    private static byte[] AMF = Generator.randomBytes(ParameterLength.AMF);
    private static PublicKey publicKey = null;
    private static PrivateKey privateKey = null;

    private static UE ue;
    private static SEAF seaf;
    private static AUSF ausf;
    private static UDM udm;

    private static void runProtocol() {

        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(512);
            KeyPair pair = generator.generateKeyPair();
            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        ue = new UE(K, SUPI, publicKey);
        seaf = new SEAF(SNN);
        ausf = new AUSF();
        udm = new UDM(K, AMF, privateKey);

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

    public static void callback(boolean wasSuccessful) {
        if (wasSuccessful) {
            System.out.println("Authentication was successful.");
        } else {
            System.err.println("Authentication failed.");
        }
        if (ue != null) {
            ue.printKseafForSNN(SNN);
        }
        if (seaf != null) {
            seaf.printKseafForSUPI(SUPI);
        }
    }

    /*
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
    */
}
