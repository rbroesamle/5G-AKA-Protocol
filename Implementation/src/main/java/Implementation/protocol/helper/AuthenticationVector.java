package Implementation.protocol.helper;

import Implementation.helper.Calculator;
import Implementation.helper.Converter;
import Implementation.helper.Generator;
import Implementation.protocol.additional.KDF;
import Implementation.protocol.additional.MAF;
import Implementation.protocol.additional.ParameterLength;

public class AuthenticationVector {

    public static Values generate(byte[] K, byte[] AMF) {
        //3GPP TS 33.102 V15.1.0 Page 25

        byte[] SQN = generateSQN();
        byte[] RAND = generateRAND();

        byte[] sqnRandAmf = Converter.concatenateBytes(SQN, RAND, AMF);

        byte[] MAC = KDF.f1(K, sqnRandAmf);
        byte[] xRES = KDF.f2(K, RAND);
        byte[] CK = MAF.f3(K, RAND);
        byte[] IK = MAF.f4(K, RAND);
        byte[] AK = MAF.f5(K, RAND);

        byte[] SQNxorAK = Calculator.xor(SQN, AK);

        return new Values(RAND, xRES, CK, IK, AK, SQNxorAK, AMF, MAC);
    }

    private static byte[] generateSQN() {
        //TODO: Generate the correct SQN.
        return Generator.randomBytes(ParameterLength.SQN);
    }

    private static byte[] generateRAND() {
        return Generator.randomBytes(ParameterLength.RAND);
    }

    public static class Values {
        public final byte[] RAND;
        public final byte[] XRES;
        public final byte[] CK;
        public final byte[] IK;
        public final byte[] AK;
        public final byte[] SQNxorAK;
        public final byte[] AMF;
        public final byte[] MAC;

        Values(byte[] RAND, byte[] XRES, byte[] CK, byte[] IK, byte[] AK, byte[] SQNxorAK, byte[] AMF, byte[] MAC) {
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
