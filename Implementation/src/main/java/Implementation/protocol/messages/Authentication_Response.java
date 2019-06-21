package Implementation.protocol.messages;

import Implementation.structure.Message;

public class Authentication_Response implements Message {
    @Override
    public String getName() {
        return "Auth Resp";
    }
}
