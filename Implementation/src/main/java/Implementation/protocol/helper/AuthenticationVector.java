package Implementation.protocol.helper;

import Implementation.helper.Calculator;
import Implementation.helper.Converter;
import Implementation.helper.Generator;
import Implementation.protocol.additional.KDF;
import Implementation.protocol.additional.MAF;
import Implementation.protocol.additional.ParameterLength;

public class AuthenticationVector {

    public static byte[] generate(byte[] K, byte[] AMF) {
        //3GPP TS 33.102 V15.1.0 Page 26

        byte[] SQN = generateSQN();
        byte[] RAND = generateRAND();

        byte[] sqnRandAmf = Converter.concatenateBytes(SQN, RAND, AMF);

        byte[] MAC = KDF.f1(K, sqnRandAmf);
        byte[] xRES = KDF.f2(K, RAND);
        byte[] CK = MAF.f3(K, RAND);
        byte[] IK = MAF.f4(K, RAND);
        byte[] AK = MAF.f5(K, RAND);

        byte[] SQNxorAK = Calculator.xor(SQN, AK);

        byte[] AUTN = Converter.concatenateBytes(SQNxorAK, AMF, MAC);

        return Converter.concatenateBytes(RAND, xRES, CK, IK, AUTN);
    }

    private static byte[] generateSQN() {
        //TODO: Generate the correct SQN.
        return Generator.randomBytes(ParameterLength.SQN);
    }

    private static byte[] generateRAND() {
        return Generator.randomBytes(ParameterLength.RAND);
    }

    public static Values reconstructValues(byte[] AV) {
        //TODO: Split the AVs into theire parts with the Converter.split() method.
        return null;
    }

    public class Values {
        final byte[] RES;
        final byte[] CK;
        final byte[] IK;

        public Values(byte[] RES, byte[] CK, byte[] IK) {
            this.RES = RES;
            this.CK = CK;
            this.IK = IK;
        }
    }
}
