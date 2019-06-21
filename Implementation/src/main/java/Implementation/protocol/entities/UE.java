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
            //Received Authentication Request
            Authentication_Request authRequest = (Authentication_Request) message;
            SEAF seaf = (SEAF) sender;

            Authentication_Response authResponse = calculateAuthResponse(authRequest, seaf);

            sendMessage(authResponse, sender);
        } else {
            System.err.println(getName() + ": Received an unusual message. Ignoring it.");
        }
    }

    public void initiateAuthentication(SEAF seaf) {
        N1_Registration_Request n1 = new N1_Registration_Request();
        sendMessage(n1, seaf);
    }

    private Authentication_Response calculateAuthResponse(Authentication_Request authRequest, SEAF seaf) {
        //TODO
        return new Authentication_Response();
    }
}
