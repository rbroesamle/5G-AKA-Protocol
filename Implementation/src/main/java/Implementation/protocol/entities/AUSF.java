package Implementation.protocol.entities;

import Implementation.protocol.messages.*;
import Implementation.structure.Entity;
import Implementation.structure.Message;

public class AUSF extends Entity {

    private ARPF arpf = null;
    private SEAF seaf = null;

    @Override
    public String getName() {
        return "AUSF";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof Message_5G_AIR && sender instanceof SEAF) {
            //Received 5G-AIR, sending Auth Info Req to the ARPF
            Message_5G_AIR fiveGAir = (Message_5G_AIR) message;
            SEAF seaf = (SEAF) sender;            System.err.println(getName() + ": Received an unusual message. Ignoring it.");


            Message_Auth_Info_Req authInfoReq = getAuthInfoReqFrom5gAir(fiveGAir, seaf);

            sendMessage(authInfoReq, this.arpf);
        } else if (message instanceof Message_Auth_Info_Resp && sender instanceof ARPF) {
            //Storing the XRES*, calculating the HXRES* and dend 5G_AIA to the SEAF.
            Message_Auth_Info_Resp authInfoResp = (Message_Auth_Info_Resp) message;
            ARPF arpf = (ARPF) sender;

            HXRES hxres = storeXresAndCalculateHxresFromAuthInfoResp(authInfoResp, arpf);
            if (hxres != null) {
                Message_5G_AIA fiveGAia = get5gAiaFromAuthInfoRespAndHxres(authInfoResp, arpf, hxres);

                sendMessage(fiveGAia, this.seaf);
            }
        } else if (message instanceof Message_5G_AC && sender instanceof SEAF) {
            //Verify the 5G-AC and send 5G-ACA back
            Message_5G_AC fiveGAc = (Message_5G_AC) message;
            SEAF seaf = (SEAF) sender;

            if (verify5gAc(fiveGAc, seaf)) {
                Message_5G_ACA fiveGAca = get5GAcaFrom5gAc(fiveGAc, seaf);

                sendMessage(fiveGAca, seaf);
            }
        } else {
            System.err.println(getName() + ": Received an unusual message. Ignoring it.");
        }
    }

    private Message_Auth_Info_Req getAuthInfoReqFrom5gAir(Message_5G_AIR fiveGAir, SEAF seaf) {
        //TODO
        return null;
    }

    private HXRES storeXresAndCalculateHxresFromAuthInfoResp(Message_Auth_Info_Resp authInfoResp, ARPF arpf) {
        //TODO
        return null;
    }

    private Message_5G_AIA get5gAiaFromAuthInfoRespAndHxres(Message_Auth_Info_Resp authInfoResp, ARPF arpf, HXRES hxres) {
        //TODO
        return null;
    }

    /**
     *
     * @param fiveGAc
     * @param seaf
     * @return true if the verification was successful.
     */
    private boolean verify5gAc(Message_5G_AC fiveGAc, SEAF seaf) {
        //TODO
        return true;
    }

    private Message_5G_ACA get5GAcaFrom5gAc(Message_5G_AC fiveGAc, SEAF seaf) {
        //TODO
        return null;
    }


    private class HXRES {

    }
}
