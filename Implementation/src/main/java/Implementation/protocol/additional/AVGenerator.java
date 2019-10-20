package Implementation.protocol.additional;

import Implementation.helper.Calculator;
import Implementation.helper.Converter;
import Implementation.helper.Generator;

public class AVGenerator {//Authentication Vector Generator

    public static AV generate(byte[] K, byte[] AMF) {
        //3GPP TS 33.102 V15.1.0 Page 25

        byte[] SQN = generateSQN();
        byte[] RAND = generateRAND();

        byte[] sqnRandAmf = Converter.concatenateBytes(SQN, RAND, AMF);

        byte[] MAC = MAF.f1(K, sqnRandAmf);
        byte[] xRES = MAF.f2(K, RAND);
        byte[] CK = KGF.f3(K, RAND);
        byte[] IK = KGF.f4(K, RAND);
        byte[] AK = KGF.f5(K, RAND);

        byte[] SQNxorAK = Calculator.xor(SQN, AK);

        return new AV(RAND, xRES, CK, IK, AK, SQNxorAK, AMF, MAC);
    }

    private static byte[] generateSQN() {
        //MARK: Deviation 1
        return Generator.randomBytes(ParameterLength.SQN);
    }

    private static byte[] generateRAND() {
        return Generator.randomBytes(ParameterLength.RAND);
    }

    public static class AV {
        public final byte[] RAND;
        public final byte[] XRES;
        public final byte[] CK;
        public final byte[] IK;
        public final byte[] AK;
        public final byte[] SQNxorAK;
        public final byte[] AMF;
        public final byte[] MAC;

        AV(byte[] RAND, byte[] XRES, byte[] CK, byte[] IK, byte[] AK, byte[] SQNxorAK, byte[] AMF, byte[] MAC) {
            this.RAND = RAND;
            this.XRES = XRES;
            this.CK = CK;
            this.IK = IK;
            this.AK = AK;
            this.SQNxorAK = SQNxorAK;
            this.AMF = AMF;
            this.MAC = MAC;
        }
    }
}
