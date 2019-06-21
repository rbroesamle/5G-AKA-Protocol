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
            //Send 5G-AIR message to the AUSF
            N1_Registration_Request n1 = (N1_Registration_Request) message;
            UE ue = (UE) sender;

            Nausf_UEAuthentication_Authenticate_Request fiveGAir = get5gAirFromN1(n1, ue);

            sendMessage(fiveGAir, this.ausf);
        } else if (message instanceof Nausf_UEAuthentication_Authenticate_Response && sender instanceof AUSF) {
            //Check expiry timer and then send Auth req to the UE
            Nausf_UEAuthentication_Authenticate_Response fiveGAia = (Nausf_UEAuthentication_Authenticate_Response) message;
            AUSF ausf = (AUSF) sender;

            if (checkExpiryTimer(fiveGAia, ausf)) {
                Authentication_Request authReq = getAuthReqFrom5gAia(fiveGAia, ausf);

                sendMessage(authReq, this.ue);
            }
        } else if (message instanceof Authentication_Response && sender instanceof UE) {
            //Calculate HXRES*, compare it and if comparison successful send 5G-AC to the AUSF
            Authentication_Response authResp = (Authentication_Response) message;
            UE ue = (UE) sender;

            if (calculateHxresAndCompare(authResp, ue)) {
                Nausf_UEAuthentication_Confirmation_Request fiveGAc = get5gAcFromAuthResp(authResp, ue);

                sendMessage(fiveGAc, this.ausf);
            }
        } else if (message instanceof Nausf_UEAuthentication_Confirmation_Response && sender instanceof AUSF) {
            //Authentication was successful.
            System.out.println("Authentication was successful.");
        } else {
            System.err.println(getName() + ": Received an unusual message. Ignoring it.");
        }
    }

    private Nausf_UEAuthentication_Authenticate_Request get5gAirFromN1(N1_Registration_Request n1, UE ue) {
        //TODO
        return new Nausf_UEAuthentication_Authenticate_Request();
    }

    /**
     *
     * @param fiveGAia
     * @return true if timer is not expired
     */
    private boolean checkExpiryTimer(Nausf_UEAuthentication_Authenticate_Response fiveGAia, AUSF ausf) {
        //TODO
        return true;
    }

    private Authentication_Request getAuthReqFrom5gAia(Nausf_UEAuthentication_Authenticate_Response fiveGAia, AUSF ausf) {
        //TODO
        return new Authentication_Request();
    }

    /**
     *
     * @param authResp
     * @param ue
     * @return true if calculated HXRES equals the previously stored one.
     */
    private boolean calculateHxresAndCompare(Authentication_Response authResp, UE ue) {
        //TODO
        return true;
    }

    private Nausf_UEAuthentication_Confirmation_Request get5gAcFromAuthResp(Authentication_Response authResp, UE ue) {
        //TODO
        return new Nausf_UEAuthentication_Confirmation_Request();
    }
}
