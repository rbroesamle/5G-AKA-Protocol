package Implementation.protocol.data;

public class Data_5G_HE_AV {

    public final byte[] RAND;
    public final Data_AUTN AUTN;
    public final byte[] XRESstar;//XRES*
    public final byte[] Kausf;

    public Data_5G_HE_AV(byte[] RAND, Data_AUTN AUTN, byte[] XRESstar, byte[] Kausf) {
        this.RAND = RAND;
        this.AUTN = AUTN;
        this.XRESstar = XRESstar;
        this.Kausf = Kausf;
    }

}
