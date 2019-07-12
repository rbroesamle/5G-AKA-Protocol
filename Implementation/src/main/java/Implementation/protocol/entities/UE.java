package Implementation.protocol.entities;

import Implementation.protocol.additional.SIDF;
import Implementation.protocol.messages.Authentication_Request;
import Implementation.protocol.messages.Authentication_Response;
import Implementation.protocol.messages.N1_Registration_Request;
import Implementation.structure.Entity;
import Implementation.structure.Message;

import java.security.PublicKey;

public class UE extends Entity {

    private final byte[] SUPI;
    private final PublicKey publicKey;

    public UE(byte[] SUPI, PublicKey publicKey) {
        this.SUPI = SUPI;
        this.publicKey = publicKey;
    }

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
            String name = message == null ? null : message.getName();
            System.err.println(getName() + ": Received an unusual message: " + (name == null ? "" : name) + ". Ignoring it.");
        }
    }

    public void initiateAuthentication(SEAF seaf) {
        N1_Registration_Request n1 = new N1_Registration_Request(SIDF.concealSUPI(this.SUPI, this.publicKey));
        sendMessage(n1, seaf);
    }

    private Authentication_Response calculateAuthResponse(Authentication_Request authRequest, SEAF seaf) {
        //TODO
        //TODO: Verify freshness, as described on page 45.
        return new Authentication_Response();
    }
}
