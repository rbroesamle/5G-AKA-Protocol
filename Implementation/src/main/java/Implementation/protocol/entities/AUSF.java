package Implementation.protocol.entities;

import Implementation.helper.Calculator;
import Implementation.helper.Converter;
import Implementation.helper.SHA256;
import Implementation.protocol.additional.KDF;
import Implementation.protocol.data.Data_5G_SE_AV;
import Implementation.protocol.data.Data_AUTN;
import Implementation.protocol.messages.*;
import Implementation.structure.Entity;
import Implementation.structure.Message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class AUSF extends Entity {

    public UDM udm = null;
    public SEAF seaf = null;

    @Override
    public String getName() {
        return "AUSF";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof Nausf_UEAuthentication_Authenticate_Request && sender instanceof SEAF) {
            //Received Nausf_UEAuthentication_ Authenticate Request
            Nausf_UEAuthentication_Authenticate_Request authRequest = (Nausf_UEAuthentication_Authenticate_Request) message;
            SEAF seaf = (SEAF) sender;
            System.out.println(getName() + ": Received " + authRequest.getName() + " from " + seaf.getName());

            if (checkIfSeafIsEntitledToUseSnName(authRequest, seaf)) {
                Nudm_UEAuthentication_Get_Request getRequest = getGetRequest(authRequest, seaf);

                sendMessage(getRequest, this.udm);
            } else {
                //TODO: answer with 'serving network not authorized' to the seaf, as described on page 40.
            }
        } else if (message instanceof Nudm_Authentication_Get_Response && sender instanceof UDM) {
            //Received Nudm_Authentication_Get Response
            Nudm_Authentication_Get_Response getResponse = (Nudm_Authentication_Get_Response) message;
            UDM udm = (UDM) sender;
            System.out.println(getName() + ": Received " + getResponse.getName() + " from " + udm.getName());

            Data_5G_SE_AV fiveGSeAv = storeAuthDataAndCompute5GSeAv(getResponse, udm);
            if (fiveGSeAv != null) {
                Nausf_UEAuthentication_Authenticate_Response authResponse = getAuthResponse(getResponse, udm, fiveGSeAv);

                sendMessage(authResponse, this.seaf);
            }
        } else if (message instanceof Nausf_UEAuthentication_Confirmation_Request && sender instanceof SEAF) {
            //Received Nausf_UEAuthentication_ Authenticate Request (/Confirmation Request)
            Nausf_UEAuthentication_Confirmation_Request confirmRequest = (Nausf_UEAuthentication_Confirmation_Request) message;
            SEAF seaf = (SEAF) sender;
            System.out.println(getName() + ": Received " + confirmRequest.getName() + " from " + seaf.getName());

            Nausf_UEAuthentication_Confirmation_Response confirmResponse = null;
            if (verifyConfirmRequest(confirmRequest, seaf)) {
                confirmResponse = getConfirmResponse(confirmRequest, seaf);
            }

            if (confirmResponse == null) {//Always send back a message to the SEAF.
                confirmResponse = new Nausf_UEAuthentication_Confirmation_Response(false, null, null);
                //Consider authentication as unsuccessful.
                System.err.println("  " + getName() + " is considering the authentication as unsuccessful.");
            } else {
                //Consider authentication as successful.
                System.out.println("  " + getName() + " is considering the authentication as successful.");
            }
            sendMessage(confirmResponse, seaf);

        } else {
            String name = message == null ? null : message.getName();
            System.err.println(getName() + ": Received an unusual message: " + (name == null ? "" : name) + ". Ignoring it.");
        }
    }

    //This queue is for temporarily storing the SNN.
    private Queue<byte[]> SNNameQueue = new LinkedList<>();

    //This HashMap is for temporarily storing the Kseaf and the XRES* for a specific SNN.
    private HashMap<byte[], TemporaryData> temporaryXRESstarStorage = new HashMap<>();

    private static class TemporaryData {
        final byte[] Kseaf;
        final byte[] XRESstar;
        final byte[] SUPI;

        TemporaryData(byte[] Kseaf, byte[] XRESstar, byte[] SUPI) {
            this.Kseaf = Kseaf;
            this.XRESstar = XRESstar;
            this.SUPI = SUPI;
        }
    }

    /**
     * @param authRequest
     * @param seaf
     * @return true if SEAF is entitled to use the ServingNetworkName
     */
    private boolean checkIfSeafIsEntitledToUseSnName(Nausf_UEAuthentication_Authenticate_Request authRequest, SEAF seaf) {
        //3GPP TS 33.501 V15.34.1 0 Page 40
        SNNameQueue.add(authRequest.servingNetworkName);
        //TODO: Check if Seaf is entitled to use this SN-Name.
        return true;
    }

    private Nudm_UEAuthentication_Get_Request getGetRequest(Nausf_UEAuthentication_Authenticate_Request authRequest, SEAF seaf) {
        if (authRequest.SUCI != null) {
            return new Nudm_UEAuthentication_Get_Request(authRequest.SUCI, true, authRequest.servingNetworkName);
        } else if (authRequest.SUPI != null) {
            return new Nudm_UEAuthentication_Get_Request(authRequest.SUPI, false, authRequest.servingNetworkName);
        }
        return null;
    }

    private Data_5G_SE_AV storeAuthDataAndCompute5GSeAv(Nudm_Authentication_Get_Response getResponse, UDM udm) {
        //3GPP TS 33.501 V15.34.1 Page 44

        byte[] servingNetworkName = this.SNNameQueue.poll();
        if (servingNetworkName == null) {
            return null;
        }

        //Derive HXRESstar
        //3GPP TS 33.501 V15.34.1 Page 155
        byte[] P0 = getResponse.heAV.RAND;
        byte[] P1 = getResponse.heAV.XRESstar;
        byte[] S = Converter.concatenateBytes(P0, P1);

        byte[] HXRESstar = SHA256.encode(S);


        //Derive Kseaf
        //3GPP TS 33.501 V15.34.1 Page 155
        byte[] Fc = Converter.intToBytes(0x6C);
        byte[][] Pis = {
                servingNetworkName
        };
        byte[][] Lis = {
                null
        };
        byte[] Kseaf = KDF.deriveKey(getResponse.heAV.Kausf, Fc, Pis, Lis);

        //Temporary storing the Kseaf and the XRES*.
        TemporaryData temporaryData = new TemporaryData(Kseaf, getResponse.heAV.XRESstar, getResponse.SUPI);
        this.temporaryXRESstarStorage.put(servingNetworkName, temporaryData);

        Data_AUTN AUTN = new Data_AUTN(getResponse.heAV.AUTN.SQNxorAK,
                getResponse.heAV.AUTN.AMF, getResponse.heAV.AUTN.MAC);
        return new Data_5G_SE_AV(getResponse.heAV.RAND, AUTN, HXRESstar);
    }

    private Nausf_UEAuthentication_Authenticate_Response getAuthResponse(Nudm_Authentication_Get_Response getResponse, UDM udm, Data_5G_SE_AV fiveGSeAv) {
        Data_5G_SE_AV av = new Data_5G_SE_AV(fiveGSeAv.RAND, fiveGSeAv.AUTN, fiveGSeAv.HXRESstar);
        return new Nausf_UEAuthentication_Authenticate_Response(av);
    }

    /**
     * @param confirmRequest
     * @param seaf
     * @return true if the verification was successful.
     */
    private boolean verifyConfirmRequest(Nausf_UEAuthentication_Confirmation_Request confirmRequest, SEAF seaf) {
        TemporaryData temporaryData = this.temporaryXRESstarStorage.get(seaf.servingNetworkName);
        if (temporaryData == null) {
            return false;
        }
        //TODO: Inform UDM about successful authentication, as described on page 45 step 11.
        return Calculator.equals(confirmRequest.RESstar, temporaryData.XRESstar);
    }

    private Nausf_UEAuthentication_Confirmation_Response getConfirmResponse(Nausf_UEAuthentication_Confirmation_Request confirmRequest, SEAF seaf) {
        TemporaryData temporaryData = this.temporaryXRESstarStorage.get(seaf.servingNetworkName);
        if (temporaryData == null) {
            return null;
        }
        this.temporaryXRESstarStorage.remove(seaf.servingNetworkName);
        return new Nausf_UEAuthentication_Confirmation_Response(true, temporaryData.Kseaf, temporaryData.SUPI);
    }
}
