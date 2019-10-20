package Implementation.protocol.messages;

import Implementation.protocol.data.Data_5G_HE_AV;
import Implementation.structure.Message;

public class Nudm_Authentication_Get_Response implements Message {
    //3GPP TS 33.501 V15.34.1 Page 44

    //HE AV.
    public final Data_5G_HE_AV heAV;

    //MARK: Deviation 20
    //Return an indication that the 5G HE AV is to be used for 5G-AKA

    //If SUCI was sent in Request, return SUPI
    public final byte[] SUPI;

    @Override
    public String getName() {
        return "Nudm_Authentication_Get Response";
    }

    public Nudm_Authentication_Get_Response(Data_5G_HE_AV heAV, byte[] SUPI) {
        this.heAV = heAV;
        this.SUPI = SUPI;
    }
}
