package Implementation.protocol.messages;

import Implementation.protocol.data.interfaces.DataForNausf_UEAuth_Auth_Req;
import Implementation.protocol.data.Data_Serving_Network_Name;
import Implementation.structure.Message;

public class Nausf_UEAuthentication_Authenticate_Request implements Message {
    //3GPP TS 33.501 V0.7.0 Page 31

    //SUCI or SUPI
    public DataForNausf_UEAuth_Auth_Req authenticationInformation;

    //Serving Netowkr Name
    public Data_Serving_Network_Name servingNetworkName;

    @Override
    public String getName() {
        return "5G AIR";
    }
}
