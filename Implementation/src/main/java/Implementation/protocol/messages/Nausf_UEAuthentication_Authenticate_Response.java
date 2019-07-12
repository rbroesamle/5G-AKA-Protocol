package Implementation.protocol.messages;

import Implementation.protocol.data.Data_5G_SE_AV;
import Implementation.structure.Message;

public class Nausf_UEAuthentication_Authenticate_Response implements Message {
    //3GPP TS 33.501 V15.34.1 Page 44

    //5G SE AV
    public final Data_5G_SE_AV seAV;

    @Override
    public String getName() {
        return "Nausf_UEAuthentication_Authenticate Response";
    }

    public Nausf_UEAuthentication_Authenticate_Response(Data_5G_SE_AV seAV) {
        this.seAV = seAV;
    }
}
