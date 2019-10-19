package Implementation.protocol.messages;

import Implementation.structure.Message;

//TODO
//In the documentation this message is called 'Nausf_UEAuthentication Authenticate Request'.
//It was renamed because an earlier message has the same name.
public class Nausf_UEAuthentication_Confirmation_Request implements Message {
    //3GPP TS 33.501 V15.34.1 Page 45

    //RES*
    public final byte[] RESstar;

    @Override
    public String getName() {
        return "Nausf_UEAuthentication Authenticate Request";
    }

    public Nausf_UEAuthentication_Confirmation_Request(byte[] RESstar) {
        this.RESstar = RESstar;
    }
}
