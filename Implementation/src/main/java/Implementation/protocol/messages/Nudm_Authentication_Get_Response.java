package Implementation.protocol.messages;

import Implementation.protocol.data.Data_5G_HE_AV;
import Implementation.protocol.data.Data_SUPI;
import Implementation.structure.Message;

public class Nudm_Authentication_Get_Response implements Message {
    //3GPP TS 33.501 V15.34.1 Page 44

    //HE AV.
    public Data_5G_HE_AV heAv; //TODO: Return an indication that the 5G HE AV is to be used for 5G-AKA

    //If SUCI was sent in Request, return SUPI
    public Data_SUPI supi;

    @Override
    public String getName() {
        return "Auth Info Resp";
    }
}
