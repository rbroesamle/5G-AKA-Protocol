package Implementation.protocol.data;

public class Data_AUTN {

    public final byte[] SQNxorAK;
    public final byte[] AMF;
    public final byte[] MAC;

    public Data_AUTN(byte[] SQNxorAK, byte[] AMF, byte[] MAC) {
        this.SQNxorAK = SQNxorAK;
        this.AMF = AMF;
        this.MAC = MAC;
    }
}
