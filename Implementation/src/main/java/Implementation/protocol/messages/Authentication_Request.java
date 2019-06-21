package Implementation.protocol.messages;

import Implementation.structure.Message;

public class Authentication_Request implements Message {
    @Override
    public String getName() {
        return "Auth Req";
    }
}
