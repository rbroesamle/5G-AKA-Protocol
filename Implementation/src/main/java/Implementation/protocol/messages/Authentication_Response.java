package Implementation.protocol.messages;

import Implementation.structure.Message;

public class Authentication_Response implements Message {
    //3GPP TS 33.501 V15.34.1 Page 45

    //TODO: Include RES*
    public final byte[] RESstar;

    @Override
    public String getName() {
        return "Authentication Response";
    }

    public Authentication_Response(byte[] RESstar) {
        this.RESstar = RESstar;
    }
}
