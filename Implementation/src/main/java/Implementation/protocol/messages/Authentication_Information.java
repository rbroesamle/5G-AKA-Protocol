package Implementation.protocol.messages;

import Implementation.structure.Message;

//TODO
//This message is for informing the UDM about the (un)successful authentication.
public class Authentication_Information implements Message {

    public final boolean authenticationSuccessful;

    @Override
    public String getName() {
        return "Authentication Information";
    }

    public Authentication_Information(boolean authenticationSuccessful) {
        this.authenticationSuccessful = authenticationSuccessful;
    }
}
