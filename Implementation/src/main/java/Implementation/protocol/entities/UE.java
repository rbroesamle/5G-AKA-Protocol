package Implementation.protocol.entities;

import Implementation.protocol.messages.Authentication_Request;
import Implementation.protocol.messages.Authentication_Response;
import Implementation.protocol.messages.N1_Registration_Request;
import Implementation.structure.Entity;
import Implementation.structure.Message;

public class UE extends Entity {

    @Override
    public String getName() {
        return "UE";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof Authentication_Request && sender instanceof SEAF) {
            //Calculate Auth Resp and send it back to the SEAF
            Authentication_Request authReq = (Authentication_Request) message;
            SEAF seaf = (SEAF) sender;

            Authentication_Response authResp = calculateAuthResp(authReq, seaf);

            sendMessage(authResp, sender);
        } else {
            System.err.println(getName() + ": Received an unusual message. Ignoring it.");
        }
    }

    public void initiateAuthentication(SEAF seaf) {
        N1_Registration_Request n1 = new N1_Registration_Request();
        sendMessage(n1, seaf);
    }

    private Authentication_Response calculateAuthResp(Authentication_Request authReq, SEAF seaf) {
        //TODO
        return new Authentication_Response();
    }
}
