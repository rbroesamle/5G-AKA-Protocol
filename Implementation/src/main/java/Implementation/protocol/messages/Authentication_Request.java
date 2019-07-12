package Implementation.protocol.messages;

import Implementation.protocol.data.Data_5G_UE_AV;
import Implementation.structure.Message;

public class Authentication_Request implements Message {
    //3GPP TS 33.501 V15.34.1 Page 44 & 45

    //RAND, AUTN
    public Data_5G_UE_AV ueAV;

    //TODO: Include ngKSI

    //TODO: Include ABBA


    @Override
    public String getName() {
        return "Authentication Request";
    }
}
