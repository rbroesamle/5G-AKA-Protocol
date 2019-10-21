package Implementation;

import Implementation.helper.Calculator;
import Implementation.helper.Converter;
import Implementation.helper.Generator;
import Implementation.protocol.additional.ParameterLength;
import Implementation.protocol.entities.*;

import java.security.*;

public class App {

    //--------- Settings -----------

    /*
     * This property decides if a successful run of the protocol or the Vulnerability should be run.
     * Acceptable values are RunMode.Protocol and RunMode.Vulnerability
     * */
    private static RunMode RUN_MODE = RunMode.Vulnerability;

    /*
     * This property decides if all sent messages should be printed on the console.
     * If set to 'true' the messages will appear in the following format: 'FROM -> TO : MESSAGE_NAME'
     * */
    public static boolean LOG_MESSAGES = true;

    /*
     * This property decides if the result of the authentication should be printed on the console with details or not.
     * */
    public static boolean DETAILED_AUTH_INFO = true;

    //------------------------------

    private enum RunMode {
        Protocol,
        Vulnerability
    }

    public static void main(String[] args) {
        generateKeyPair();

        System.out.println("Starting protocol...");

        UE ue;
        switch (RUN_MODE) {
            case Protocol:
                ue = new UE(K, SUPI, publicKey);
                System.out.println("RunMode: Protocol");
                break;
            case Vulnerability:
                ue = new EvilUE(K, SUPI, publicKey, SUPI_victim);
                System.out.println("RunMode: Vulnerability");
                break;
            default:
                System.err.println("Please specify a correct RunMode. Exiting.");
                return;
        }

        System.out.println();

        runProtocol(ue);

        System.out.println("\nExiting...");
    }

    private static byte[] K = Generator.randomBytes(ParameterLength.K);
    //MARK: Deviation 21
    private static byte[] SUPI = Generator.randomBytes(ParameterLength.K);
    private static byte[] SN_NAME = Generator.randomBytes(ParameterLength.K);
    private static byte[] AMF = Generator.randomBytes(ParameterLength.AMF);
    private static PublicKey publicKey = null;
    private static PrivateKey privateKey = null;

    private static byte[] SUPI_victim = Generator.randomBytes(ParameterLength.K);

    private static UE ue;
    private static SEAF seaf;
    private static AUSF ausf;
    private static UDM udm;

    private static void generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(512);
            KeyPair pair = generator.generateKeyPair();
            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void runProtocol(UE ue) {
        App.ue = ue;
        App.seaf = new SEAF(SN_NAME);
        App.ausf = new AUSF();
        App.udm = new UDM(K, AMF, privateKey);

        App.seaf.ausf = App.ausf;
        App.seaf.ue = App.ue;
        App.ausf.udm = App.udm;
        App.ausf.seaf = App.seaf;

        App.ue.initiateAuthentication(App.seaf);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void reportAuthResult(boolean wasSuccessful) {
        if (wasSuccessful) {
            System.out.println("Authentication was successful.");
        } else {
            System.err.println("Authentication failed.");
        }
        byte[] ueKseaf = ue.getKseafForSNN(SN_NAME);
        byte[] seafKseaf = null;
        if (App.RUN_MODE == RunMode.Protocol) {
            seafKseaf = seaf.getKseafForSUPI(SUPI);
        } else if (App.RUN_MODE == RunMode.Vulnerability) {
            seafKseaf = seaf.getKseafForSUPI(SUPI_victim);
        }

        if (App.DETAILED_AUTH_INFO) {
            System.out.println(" " + ue.getName() + ":   Kseaf: " + (ueKseaf == null ? "null" : Converter.bytesToHex(ueKseaf)));
            System.out.println(" " + seaf.getName() + ": Kseaf: " + (seafKseaf == null ? "null" : Converter.bytesToHex(seafKseaf)));
        }

        if (App.RUN_MODE == RunMode.Vulnerability &&
                wasSuccessful && Calculator.equals(ueKseaf, seafKseaf)) {
            System.out.println("Vulnerability was successful.");
        } else if (App.RUN_MODE == RunMode.Vulnerability) {
            System.err.println("Vulnerability failed.");
        }
    }
}
