package Implementation.protocol.messages;

import Implementation.structure.Message;

public class Authentication_Failure implements Message {
    //3GPP TS 33.501 V15.34.1 Page 46

    @Override
    public String getName() {
        return "Authentication Failure";
    }
}
