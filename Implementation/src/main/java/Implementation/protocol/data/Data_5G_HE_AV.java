package Implementation.protocol.data;

public class Data_5G_HE_AV {

    public final byte[] RAND;
    public final AUTN AUTN;
    public final byte[] XRESstar;//XRES*
    public final byte[] Kausf;

    public Data_5G_HE_AV(byte[] RAND, AUTN AUTN, byte[] XRESstar, byte[] Kausf) {
        this.RAND = RAND;
        this.AUTN = AUTN;
        this.XRESstar = XRESstar;
        this.Kausf = Kausf;
    }

    public static class AUTN {
        public final byte[] SQNxorAK;
        public final byte[] AMF;
        public final byte[] MAC;

        public AUTN(byte[] SQNxorAK, byte[] AMF, byte[] MAC) {
            this.SQNxorAK = SQNxorAK;
            this.AMF = AMF;
            this.MAC = MAC;
        }
    }
}
