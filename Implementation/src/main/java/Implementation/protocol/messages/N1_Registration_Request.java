package Implementation.protocol.messages;

import Implementation.protocol.data.Data_5G_GUTI;
import Implementation.structure.Message;

public class N1_Registration_Request implements Message {
    //3GPP TS 33.501 V15.34.1 0 Page 40

    //SUCI or 5G-GUTI
    public final byte[] SUCI;
    public final Data_5G_GUTI fiveGGUTI;

    @Override
    public String getName() {
        return "N1 Registration Request";
    }

    public N1_Registration_Request(byte[] suci) {
        this.SUCI = suci;
        this.fiveGGUTI = null;
    }

    public N1_Registration_Request(Data_5G_GUTI guti) {
        this.fiveGGUTI = guti;
        this.SUCI = null;
    }
}
