package Implementation.protocol.messages;

import Implementation.protocol.data.DataFor5GAir;
import Implementation.protocol.data.Data_Serving_Network_Name;
import Implementation.structure.Message;

public class Message_5G_AIR implements Message {
    //3GPP TS 33.501 V0.7.0 Page 31

    //SUCI or SUPI
    public DataFor5GAir authenticationInformation;

    //Serving Netowkr Name
    public Data_Serving_Network_Name servingNetworkName;

    @Override
    public String getName() {
        return "5G AIR";
    }
}
