package Implementation.protocol.messages;

import Implementation.protocol.data.interfaces.DataForRegistration_Request;
import Implementation.structure.Message;

public class N1_Registration_Request implements Message {
    //3GPP TS 33.501 V15.34.1 0 Page 40

    //SUCI or 5G-GUTI
    public DataForRegistration_Request authenticationInformation;

    @Override
    public String getName() {
        return "N1";
    }
}
