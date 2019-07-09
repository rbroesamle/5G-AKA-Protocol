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

    //TODO: Maybe include the SN-Name in the N1 message.
    public final byte[] servingNetworkName;

    @Override
    public String getName() {
        return "UE";
    }

    public UE(byte[] SUPI, PublicKey publicKey, byte[] servingNetworkName) {
        this.SUPI = SUPI;
        this.publicKey = publicKey;
        this.servingNetworkName = servingNetworkName;
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
        N1_Registration_Request n1 = new N1_Registration_Request(SIDF.concealSUPI(this.SUPI, this.publicKey));
        sendMessage(n1, seaf);
    }

    private Authentication_Response calculateAuthResponse(Authentication_Request authRequest, SEAF seaf) {
        //TODO
        //TODO: Verify freshness, as described on page 45.
        return new Authentication_Response();
    }
}
