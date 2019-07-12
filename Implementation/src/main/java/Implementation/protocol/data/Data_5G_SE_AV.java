package Implementation.protocol.data;

public class Data_5G_SE_AV {

    public final byte[] RAND;
    public final Data_AUTN AUTN;
    public final byte[] HXRESstar;//HXRES*

    public Data_5G_SE_AV(byte[] RAND, Data_AUTN AUTN, byte[] HXRESstar) {
        this.RAND = RAND;
        this.AUTN = AUTN;
        this.HXRESstar = HXRESstar;
    }
}
