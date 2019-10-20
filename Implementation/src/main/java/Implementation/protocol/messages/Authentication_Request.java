package Implementation.protocol.messages;

import Implementation.protocol.data.Data_AUTN;
import Implementation.structure.Message;

public class Authentication_Request implements Message {
    //3GPP TS 33.501 V15.34.1 Page 44 & 45

    //RAND, AUTN
    public final byte[] RAND;
    public final Data_AUTN AUTN;

    //MARK: Deviation 17
    //Include ngKSI
    //Include ABBA


    @Override
    public String getName() {
        return "Authentication Request";
    }

    public Authentication_Request(byte[] RAND, Data_AUTN AUTN) {
        this.RAND = RAND;
        this.AUTN = AUTN;
    }
}
