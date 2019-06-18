package Implementation.protocol.entities;

import Implementation.protocol.messages.*;
import Implementation.structure.Entity;
import Implementation.structure.Message;

public class SEAF  extends Entity {

    private AUSF ausf = null;
    private UE ue = null;

    @Override
    public String getName() {
        return "SEAF";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof Message_N1 && sender instanceof UE) {
            //Send 5G-AIR message to the AUSF
            Message_N1 n1 = (Message_N1) message;
            UE ue = (UE) sender;

            Message_5G_AIR fiveGAir = get5gAirFromN1(n1, ue);

            sendMessage(fiveGAir, this.ausf);
        } else if (message instanceof Message_5G_AIA && sender instanceof AUSF) {
            //Check expiry timer and then send Auth req to the UE
            Message_5G_AIA fiveGAia = (Message_5G_AIA) message;
            AUSF ausf = (AUSF) sender;

            if (checkExpiryTimer(fiveGAia, ausf)) {
                Message_Auth_Req authReq = getAuthReqFrom5gAia(fiveGAia, ausf);

                sendMessage(authReq, this.ue);
            }
        } else if (message instanceof Message_Auth_Resp && sender instanceof UE) {
            //Calculate HXRES*, compare it and if comparison successful send 5G-AC to the AUSF
            Message_Auth_Resp authResp = (Message_Auth_Resp) message;
            UE ue = (UE) sender;

            if (calculateHxresAndCompare(authResp, ue)) {
                Message_5G_AC fiveGAc = get5gAcFromAuthResp(authResp, ue);

                sendMessage(fiveGAc, this.ausf);
            }
        } else if (message instanceof Message_5G_ACA && sender instanceof AUSF) {
            //Authentication was successful.
            System.out.println("Authentication was successful.");
        } else {
            System.err.println(getName() + ": Received an unusual message. Ignoring it.");
        }
    }

    private Message_5G_AIR get5gAirFromN1(Message_N1 n1, UE ue) {
        //TODO
        return null;
    }

    /**
     *
     * @param fiveGAia
     * @return true if timer is not expired
     */
    private boolean checkExpiryTimer(Message_5G_AIA fiveGAia, AUSF ausf) {
        //TODO
        return true;
    }

    private Message_Auth_Req getAuthReqFrom5gAia(Message_5G_AIA fiveGAia, AUSF ausf) {
        //TODO
        return null;
    }

    /**
     *
     * @param authResp
     * @param ue
     * @return true if calculated HXRES equals the previously stored one.
     */
    private boolean calculateHxresAndCompare(Message_Auth_Resp authResp, UE ue) {
        //TODO
        return true;
    }

    private Message_5G_AC get5gAcFromAuthResp(Message_Auth_Resp authResp, UE ue) {
        //TODO
        return null;
    }
}
