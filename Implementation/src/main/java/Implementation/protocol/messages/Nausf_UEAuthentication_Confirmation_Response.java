package Implementation.protocol.messages;

import Implementation.structure.Message;

//In the documentation this message is called 'Nausf_UEAuthentication Authenticate Response'.
//It was renamed because an earlier message has the same name.
public class Nausf_UEAuthentication_Confirmation_Response implements Message {
    //3GPP TS 33.501 V15.34.1 Page 45

    //TODO: Include Kseaf

    @Override
    public String getName() {
        return "Nausf_UEAuthentication Authenticate Response";
    }
}