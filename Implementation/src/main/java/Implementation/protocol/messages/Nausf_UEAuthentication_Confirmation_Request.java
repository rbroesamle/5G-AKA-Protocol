package Implementation.protocol.messages;

import Implementation.structure.Message;

//In the documentation this message is called 'Nausf_UEAuthentication Authenticate Request'.
//It was renamed because an earlier message has the same name.
public class Nausf_UEAuthentication_Confirmation_Request implements Message {
    @Override
    public String getName() {
        return "5G AC";
    }
}
