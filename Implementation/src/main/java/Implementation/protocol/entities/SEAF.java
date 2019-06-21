package Implementation.protocol.entities;

import Implementation.protocol.messages.*;
import Implementation.structure.Entity;
import Implementation.structure.Message;

public class SEAF  extends Entity {

    public AUSF ausf = null;
    public UE ue = null;

    @Override
    public String getName() {
        return "SEAF";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof N1_Registration_Request && sender instanceof UE) {
            //Received N1 Registration Request
            N1_Registration_Request n1 = (N1_Registration_Request) message;
            UE ue = (UE) sender;

            Nausf_UEAuthentication_Authenticate_Request authRequest = getAuthRequest(n1, ue);

            sendMessage(authRequest, this.ausf);
        } else if (message instanceof Nausf_UEAuthentication_Authenticate_Response && sender instanceof AUSF) {
            //Received Nausf_UEAuthentication_ Authenticate Response
            Nausf_UEAuthentication_Authenticate_Response authResponse = (Nausf_UEAuthentication_Authenticate_Response) message;
            AUSF ausf = (AUSF) sender;

            if (checkExpiryTimer(authResponse, ausf)) {
                Authentication_Request authRequest = getAuthRequest(authResponse, ausf);

                sendMessage(authRequest, this.ue);
            }
        } else if (message instanceof Authentication_Response && sender instanceof UE) {
            //Received Authentication Response
            Authentication_Response authResponse = (Authentication_Response) message;
            UE ue = (UE) sender;

            if (calculateHxresAndCompare(authResponse, ue)) {
                Nausf_UEAuthentication_Confirmation_Request confirmRequest = getConfirmRequest(authResponse, ue);

                sendMessage(confirmRequest, this.ausf);
            }
        } else if (message instanceof Nausf_UEAuthentication_Confirmation_Response && sender instanceof AUSF) {
            //Received Nausf_UEAuthentication_ Authenticate Response (/Confirmation Response)
            System.out.println("Authentication was successful.");
        } else {
            System.err.println(getName() + ": Received an unusual message. Ignoring it.");
        }
    }

    private Nausf_UEAuthentication_Authenticate_Request getAuthRequest(N1_Registration_Request n1, UE ue) {
        //TODO
        return new Nausf_UEAuthentication_Authenticate_Request();
    }

    /**
     *
     * @param authResponse
     * @param ausf
     * @return true if timer is not expired
     */
    private boolean checkExpiryTimer(Nausf_UEAuthentication_Authenticate_Response authResponse, AUSF ausf) {
        //TODO
        return true;
    }

    private Authentication_Request getAuthRequest(Nausf_UEAuthentication_Authenticate_Response authResponse, AUSF ausf) {
        //TODO
        return new Authentication_Request();
    }

    /**
     *
     * @param authResponse
     * @param ue
     * @return true if calculated HXRES equals the previously stored one.
     */
    private boolean calculateHxresAndCompare(Authentication_Response authResponse, UE ue) {
        //TODO
        return true;
    }

    private Nausf_UEAuthentication_Confirmation_Request getConfirmRequest(Authentication_Response authResponse, UE ue) {
        //TODO
        return new Nausf_UEAuthentication_Confirmation_Request();
    }
}
