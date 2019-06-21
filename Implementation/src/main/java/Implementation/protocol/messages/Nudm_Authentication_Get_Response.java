package Implementation.protocol.messages;

import Implementation.structure.Message;

public class Nudm_Authentication_Get_Response implements Message {
    @Override
    public String getName() {
        return "Auth Info Resp";
    }
}
