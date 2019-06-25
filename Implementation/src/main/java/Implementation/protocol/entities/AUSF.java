package Implementation.protocol.entities;

import Implementation.protocol.data.Data_5G_SE_AV;
import Implementation.protocol.messages.*;
import Implementation.structure.Entity;
import Implementation.structure.Message;

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
            System.err.println(getName() + ": Received an unusual message. Ignoring it.");
        }
    }

    /**
     *
     * @param authRequest
     * @param seaf
     * @return true if SEAF is entitled to use the ServingNetworkName
     */
    private boolean checkIfSeafIsEntitledToUseSnName(Nausf_UEAuthentication_Authenticate_Request authRequest, SEAF seaf) {
        //3GPP TS 33.501 V15.34.1 0 Page 40
        //TODO: Store the SN-Name temporarily, as described on page 40.
        //TODO
        return true;
    }

    private Nudm_UEAuthentication_Get_Request getGetRequest(Nausf_UEAuthentication_Authenticate_Request authRequest, SEAF seaf) {
        //TODO
        return new Nudm_UEAuthentication_Get_Request();
    }

    private Data_5G_SE_AV storeAuthDataAndCompute5GSeAv(Nudm_Authentication_Get_Response getResponse, UDM udm) {
        //3GPP TS 33.501 V15.34.1 Page 44
        //TODO: Store the XRES* temporarily together with the received SUCI or SUPI. And maybe store the KAUSF. See page 44
        //TODO: Generate 5G SE AV from 5G HE AV, as described on page 44.
        //TODO
        return new Data_5G_SE_AV();
    }

    private Nausf_UEAuthentication_Authenticate_Response getAuthResponse(Nudm_Authentication_Get_Response getResponse, UDM udm, Data_5G_SE_AV fiveGSeAv) {
        //TODO
        return new Nausf_UEAuthentication_Authenticate_Response();
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
