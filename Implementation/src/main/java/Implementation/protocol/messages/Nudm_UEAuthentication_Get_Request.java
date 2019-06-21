package Implementation.protocol.messages;

import Implementation.structure.Message;

public class Nudm_UEAuthentication_Get_Request implements Message {
    @Override
    public String getName() {
        return "Auth Info Req";
    }
}
