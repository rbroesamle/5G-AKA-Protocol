package Implementation.protocol.entities;

import Implementation.protocol.messages.Message_Auth_Info_Req;
import Implementation.protocol.messages.Message_Auth_Info_Resp;
import Implementation.structure.Entity;
import Implementation.structure.Message;

public class ARPF extends Entity {

    @Override
    public String getName() {
        return "ARPF";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof Message_Auth_Info_Req && sender instanceof AUSF) {
            //Select 5G-AKA as authentication method, generate AVs and send Auth Info Resp to the AUSF.
            Message_Auth_Info_Req authInfoReq = (Message_Auth_Info_Req) message;
            AUSF ausf = (AUSF) sender;

            //Always choose 5G-AKA as authentication method.
            AVs aVs = generateAVs(authInfoReq, ausf);

            Message_Auth_Info_Resp authInfoResp = getAuthInfoRespFromAuthInfoReq(authInfoReq, ausf);

            sendMessage(authInfoResp, ausf);
        }
    }

    private AVs generateAVs(Message_Auth_Info_Req authInfoReq, AUSF ausf) {
        //TODO
        return null;
    }

    private Message_Auth_Info_Resp getAuthInfoRespFromAuthInfoReq(Message_Auth_Info_Req authInfoReq, AUSF ausf) {
        //TODO
        return null;
    }


    private class AVs {

    }
}
