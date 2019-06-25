package Implementation.protocol.messages;

import Implementation.protocol.data.Data_5G_SE_AV;
import Implementation.structure.Message;

public class Nausf_UEAuthentication_Authenticate_Response implements Message {
    //3GPP TS 33.501 V15.34.1 Page 44

    //5G SE AV
    public Data_5G_SE_AV seAv;

    @Override
    public String getName() {
        return "Nausf_UEAuthentication_Authenticate Response";
    }
}
