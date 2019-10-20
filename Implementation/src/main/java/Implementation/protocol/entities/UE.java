package Implementation.protocol.entities;

import Implementation.App;
import Implementation.helper.Calculator;
import Implementation.helper.Converter;
import Implementation.protocol.additional.KDF;
import Implementation.protocol.additional.MAF;
import Implementation.protocol.additional.KGF;
import Implementation.protocol.additional.SIDF;
import Implementation.protocol.data.Data_AUTN;
import Implementation.protocol.messages.*;
import Implementation.structure.Entity;
import Implementation.structure.Message;

import java.security.PublicKey;
import java.util.HashMap;

public class UE extends Entity {

    private final byte[] K;

    private final byte[] SUPI;
    final PublicKey publicKey;

    //Saving the Kseaf for the corresponding SNN in hex-format.
    private HashMap<String, byte[]> Kseafs = new HashMap<>();

    public UE(byte[] K, byte[] SUPI, PublicKey publicKey) {
        this.K = K;
        this.SUPI = SUPI;
        this.publicKey = publicKey;
    }

    @Override
    public String getName() {
        return "UE";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof Authentication_Request && sender instanceof SEAF) {
            //Received Authentication Request
            Authentication_Request authRequest = (Authentication_Request) message;
            SEAF seaf = (SEAF) sender;
            if (App.LOG_MESSAGES) {
                System.out.println(seaf.getName() + " -> " + getName() + " : " + authRequest.getName());
            }

            Message authResponse = calculateAuthResponse(authRequest, seaf);

            sendMessage(authResponse, sender);
        } else if (message instanceof Authentication_Reject && sender instanceof SEAF) {
            //Received Authentication Reject
            Authentication_Reject authReject = (Authentication_Reject) message;
            SEAF seaf = (SEAF) sender;
            if (App.LOG_MESSAGES) {
                System.out.println(seaf.getName() + " -> " + getName() + " : " + authReject.getName());
            }

            App.reportAuthResult(false);
        } else {
            String messageName = message == null ? "?" : message.getName();
            String senderName = sender == null ? "?" : sender.getName();
            if (App.LOG_MESSAGES) {
                System.err.println(senderName + " -> " + getName() + " : " + messageName);
            }
        }
    }

    public void initiateAuthentication(SEAF seaf) {
        N1_Registration_Request n1 = new N1_Registration_Request(SIDF.concealSUPI(this.SUPI, this.publicKey));
        sendMessage(n1, seaf);
    }

    private Message calculateAuthResponse(Authentication_Request authRequest, SEAF seaf) {
        Data_AUTN AUTN = authRequest.AUTN;

        //MARK: Deviation 15
        // Verify freshness of AUTN, as described at 3GPP TS 33.501 V15.34.1 Page 45.
        if (false) {
            //Result of verification of freshness failed
            if (App.DETAILED_AUTH_INFO) {
                System.err.println(getName() + ": The AUTN is not valid.");
            }

            //MARK: Deviation 16
            return new Authentication_Failure(/*CAUSE value*/);
        }
        byte[] RAND = authRequest.RAND;

        byte[] AK = KGF.f5(this.K, RAND);
        byte[] SQN = Calculator.xor(AUTN.SQNxorAK, AK);

        byte[] XMAC = MAF.f1(K, Converter.concatenateBytes(SQN, RAND, AUTN.AMF));
        if (!Calculator.equals(XMAC, AUTN.MAC)) {
            if (App.DETAILED_AUTH_INFO) {
                System.out.println(getName() + ": The calculated XMAC doesn't equal to the received MAC");
            }

            //MARK: Deviation 16
            return new Authentication_Failure(/*CAUSE value*/);
        }

        byte[] RES = MAF.f2(K, RAND);

        byte[] CK = KGF.f3(K, RAND);
        byte[] IK = KGF.f4(K, RAND);


        byte[] KEY = Converter.concatenateBytes(CK, IK);


        //Derive Kausf
        //3GPP TS 33.501 V15.34.1 Page 154
        byte[] Fc_Kausf = Converter.intToBytes(0x6A);
        byte[][] Pis_Kausf = {
                seaf.servingNetworkName,
                AUTN.SQNxorAK
        };
        int SQNxorAKLength = Converter.shrinkBytes(AUTN.SQNxorAK).length == 0 ? 0x00 : 0x06;
        byte[][] Lis_Kausf = {
                null,
                Converter.intToBytes(SQNxorAKLength)
        };
        byte[] Kausf = KDF.deriveKey(KEY, Fc_Kausf, Pis_Kausf, Lis_Kausf);


        //Derive Kseaf
        //3GPP TS 33.501 V15.34.1 Page 155
        byte[] Fc = Converter.intToBytes(0x6C);
        byte[][] Pis = {
                seaf.servingNetworkName
        };
        byte[][] Lis = {
                null
        };
        byte[] Kseaf = KDF.deriveKey(Kausf, Fc, Pis, Lis);
        this.Kseafs.put(Converter.bytesToHex(seaf.servingNetworkName), Kseaf);


        //Derive RES*
        //3GPP TS 33.501 V15.34.1 Page 155
        byte[] Fc_RESstar = Converter.intToBytes(0x6B);
        byte[][] Pis_RESstar = {
                seaf.servingNetworkName,
                RAND,
                RES
        };
        int RANDLength = Converter.shrinkBytes(RAND).length == 0 ? 0x00 : 0x10;
        byte[][] Lis_RESstar = {
                null,
                Converter.intToBytes(RANDLength),
                null
        };
        byte[] RESstar = KDF.deriveKey(KEY, Fc_RESstar, Pis_RESstar, Lis_RESstar);


        return new Authentication_Response(RESstar);
    }

    //Custom function for displaying the Kseaf.
    //NOTE: This function is NOT part of the specification. It's only purpose is to allow the comparison of the Kseaf from UE and SEAF!
    public byte[] getKseafForSNN(byte[] SNN) {
        return this.Kseafs.get(Converter.bytesToHex(SNN));
    }
}
