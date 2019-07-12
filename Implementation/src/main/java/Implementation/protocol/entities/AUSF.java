package Implementation.protocol.entities;

import Implementation.helper.Converter;
import Implementation.helper.SHA256;
import Implementation.protocol.additional.KDF;
import Implementation.protocol.data.Data_5G_SE_AV;
import Implementation.protocol.data.Data_AUTN;
import Implementation.protocol.messages.*;
import Implementation.structure.Entity;
import Implementation.structure.Message;

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

            Data_5G_SE_AV fiveGSeAv = storeAuthDataAndCompute5GSeAv(getResponse, udm);
            if (fiveGSeAv != null) {
                Nausf_UEAuthentication_Authenticate_Response authResponse = getAuthResponse(getResponse, udm, fiveGSeAv);

                sendMessage(authResponse, this.seaf);
            }
        } else if (message instanceof Nausf_UEAuthentication_Confirmation_Request && sender instanceof SEAF) {
            //Received Nausf_UEAuthentication_ Authenticate Request (/Confirmation Request)
            Nausf_UEAuthentication_Confirmation_Request confirmRequest = (Nausf_UEAuthentication_Confirmation_Request) message;
            SEAF seaf = (SEAF) sender;

            if (verifyConfirmRequest(confirmRequest, seaf)) {
                Nausf_UEAuthentication_Confirmation_Response confirmResponse = getConfirmResponse(confirmRequest, seaf);

                sendMessage(confirmResponse, seaf);
            }
        } else {
            String name = message == null ? null : message.getName();
            System.err.println(getName() + ": Received an unusual message: " + (name == null ? "" : name) + ". Ignoring it.");
        }
    }

    private Queue<Byte[]> SNNameQueue = new LinkedList<>();


    /**
     *
     * @param authRequest
     * @param seaf
     * @return true if SEAF is entitled to use the ServingNetworkName
     */
    private boolean checkIfSeafIsEntitledToUseSnName(Nausf_UEAuthentication_Authenticate_Request authRequest, SEAF seaf) {
        //3GPP TS 33.501 V15.34.1 0 Page 40
        Byte[] SNName = new Byte[authRequest.servingNetworkName.length];
        for (int i = 0; i < authRequest.servingNetworkName.length; i++) {
            SNName[i] = authRequest.servingNetworkName[i];
        }
        SNNameQueue.add(SNName);
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
        //TODO: Store the XRES* temporarily together with the received SUCI or SUPI. And maybe store the KAUSF. See page 44

        Byte[] SNName = this.SNNameQueue.poll();
        if (SNName == null) {
            return null;
        }
        byte[] servingNetworkName = new byte[SNName.length];
        for (int i = 0; i< SNName.length; i++) {
            servingNetworkName[i] = SNName[i];
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
        //TODO: Store this key somewhere... See Page 44. Message-Nr.: 4 & 5


        Data_AUTN AUTN = new Data_AUTN(getResponse.heAV.AUTN.SQNxorAK,
                getResponse.heAV.AUTN.AMF, getResponse.heAV.AUTN.MAC);
        return new Data_5G_SE_AV(getResponse.heAV.RAND, AUTN, HXRESstar);
    }

    private Nausf_UEAuthentication_Authenticate_Response getAuthResponse(Nudm_Authentication_Get_Response getResponse, UDM udm, Data_5G_SE_AV fiveGSeAv) {
        Data_5G_SE_AV av = new Data_5G_SE_AV(fiveGSeAv.RAND, fiveGSeAv.AUTN, fiveGSeAv.HXRESstar);
        return new Nausf_UEAuthentication_Authenticate_Response(av);
    }

    /**
     *
     * @param confirmRequest
     * @param seaf
     * @return true if the verification was successful.
     */
    private boolean verifyConfirmRequest(Nausf_UEAuthentication_Confirmation_Request confirmRequest, SEAF seaf) {
        //TODO
        return true;
    }

    private Nausf_UEAuthentication_Confirmation_Response getConfirmResponse(Nausf_UEAuthentication_Confirmation_Request confirmRequest, SEAF seaf) {
        //TODO
        return new Nausf_UEAuthentication_Confirmation_Response();
    }
}
