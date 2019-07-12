package Implementation.protocol.data;

public class Data_5G_SE_AV {

    public final byte[] RAND;
    public final Data_5G_SE_AV.AUTN AUTN;
    public final byte[] HXRESstar;//HXRES*

    //This should be null... TODO: Maybe find a better solution to this...
    //See '3GPP TS 33.501 V15.34.1' Page 44 message number 4 & 5.
    public final byte[] Kseaf;

    public Data_5G_SE_AV(byte[] RAND, Data_5G_SE_AV.AUTN AUTN, byte[] HXRESstar, byte[] Kseaf) {
        this.RAND = RAND;
        this.AUTN = AUTN;
        this.HXRESstar = HXRESstar;
        this.Kseaf = Kseaf;
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
