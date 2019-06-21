package Implementation.protocol.entities;

import Implementation.protocol.messages.Message_Auth_Req;
import Implementation.protocol.messages.Message_Auth_Resp;
import Implementation.protocol.messages.Message_N1;
import Implementation.structure.Entity;
import Implementation.structure.Message;

public class UE extends Entity {

    @Override
    public String getName() {
        return "UE";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof Message_Auth_Req && sender instanceof SEAF) {
            //Calculate Auth Resp and send it back to the SEAF
            Message_Auth_Req authReq = (Message_Auth_Req) message;
            SEAF seaf = (SEAF) sender;

            Message_Auth_Resp authResp = calculateAuthResp(authReq, seaf);

            sendMessage(authResp, sender);
        } else {
            System.err.println(getName() + ": Received an unusual message. Ignoring it.");
        }
    }

    public void initiateAuthentication(SEAF seaf) {
        Message_N1 n1 = new Message_N1();
        sendMessage(n1, seaf);
    }

    private Message_Auth_Resp calculateAuthResp(Message_Auth_Req authReq, SEAF seaf) {
        //TODO
        return new Message_Auth_Resp();
    }
}
