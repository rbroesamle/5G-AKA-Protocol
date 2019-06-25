package Implementation.protocol.messages;

import Implementation.protocol.data.Data_Serving_Network_Name;
import Implementation.protocol.data.interfaces.DataForNudm_UEAuth_Get_Req;
import Implementation.structure.Message;

public class Nudm_UEAuthentication_Get_Request implements Message {
    //3GPP TS 33.501 V15.34.1 Page 40

    //SUCI or SUPI
    public DataForNudm_UEAuth_Get_Req authentication;

    //Serving Network Name
    public Data_Serving_Network_Name servingNetworkName;

    @Override
    public String getName() {
        return "Nudm_UEAuthentication_Get Request";
    }
}
