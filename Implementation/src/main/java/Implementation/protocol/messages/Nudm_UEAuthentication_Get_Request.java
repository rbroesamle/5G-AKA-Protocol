package Implementation.protocol.messages;

import Implementation.structure.Message;

public class Nudm_UEAuthentication_Get_Request implements Message {
    //3GPP TS 33.501 V15.34.1 Page 40

    //SUCI or SUPI
    public final byte[] SUCI;
    public final byte[] SUPI;

    //Serving Network Name
    public final byte[] servingNetworkName;

    @Override
    public String getName() {
        return "Nudm_UEAuthentication_Get Request";
    }

    public Nudm_UEAuthentication_Get_Request(byte[] suciOrSupi, boolean isSuci, byte[] servingNetworkName) {
        if (isSuci) {
            this.SUCI = suciOrSupi;
            this.SUPI = null;
        } else {
            this.SUCI = null;
            this.SUPI = suciOrSupi;
        }
        this.servingNetworkName = servingNetworkName;
    }
}
