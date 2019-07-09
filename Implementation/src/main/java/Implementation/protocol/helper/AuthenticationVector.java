package Implementation.protocol.helper;

import Implementation.helper.Calculator;
import Implementation.helper.Converter;
import Implementation.helper.Generator;
import Implementation.protocol.additional.KDF;
import Implementation.protocol.additional.MAF;
import Implementation.protocol.additional.ParameterLength;

import java.util.Arrays;

public class AuthenticationVector {

    public static byte[] generate(byte[] K, byte[] AMF) {
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

    public static Values reconstructValues(byte[] K, byte[] AV) {
        //3GPP TS 33.102 V15.1.0 Page 27

        byte[][] splitRAND = Converter.splitBytes(AV, ParameterLength.RAND, AV.length + ParameterLength.RAND);
        if (splitRAND.length < 2) {
            System.out.println("AuthenticationVector: Couldn't separate RAND from the AV.");
            return null;
        }
        byte[] RAND = splitRAND[0];
        byte[] afterSplitRAND = splitRAND[1];

        int autnLength = ParameterLength.SQN + ParameterLength.AMF + ParameterLength.MAC;
        byte[][] splitAUTN = Converter.splitBytes(afterSplitRAND, afterSplitRAND.length - autnLength, autnLength);
        if (splitAUTN.length < 2) {
            System.out.println("AuthenticationVector: Couldn't separate AUTN from the AV.");
            return null;
        }
        byte[] AUTN = splitAUTN[1];
        byte[] afterSplitAUTN = splitAUTN[0];

        byte[][] splitIK = Converter.splitBytes(afterSplitAUTN, afterSplitAUTN.length - ParameterLength.IK, ParameterLength.IK);
        if (splitIK.length < 2) {
            System.out.println("AuthenticationVector: Couldn't separate IK from the AV.");
            return null;
        }
        byte[] IK = splitIK[1];
        byte[] afterSplitIK = splitIK[0];

        byte[][] splitCK = Converter.splitBytes(afterSplitIK, afterSplitIK.length - ParameterLength.CK, ParameterLength.CK);
        if (splitCK.length < 2) {
            System.out.println("AuthenticationVector: Couldn't separate CK from the AV.");
            return null;
        }
        byte[] CK = splitCK[1];
        byte[] RES = splitCK[0];

        //Split AUTN
        byte[][] separateAUTN = Converter.splitBytes(AUTN, ParameterLength.SQN, ParameterLength.AMF, ParameterLength.MAC);
        if (separateAUTN.length < 3) {
            System.out.println("AuthenticationVector: Couldn't separate AUTN into its components.");
            return null;
        }
        byte[] SQNxorAK = separateAUTN[0];
        byte[] AMF = separateAUTN[1];
        byte[] MAC = separateAUTN[2];

        //Check if all arrays are of the correct size.
        if (RAND.length != ParameterLength.RAND ||
                CK.length != ParameterLength.CK ||
                IK.length != ParameterLength.IK ||
                SQNxorAK.length != ParameterLength.SQN ||
                AMF.length != ParameterLength.AMF ||
                MAC.length != ParameterLength.MAC) {
            System.out.println("AuthenticationVector: The calculated Components are not of the correct length.");
            return null;
        }
        //All arrays are of the correct size.

        //Calculate the SQN
        byte[] AK = MAF.f5(K, RAND);
        byte[] SQN = Calculator.xor(SQNxorAK, AK);

        //Verify MAC
        byte[] XMAC = KDF.f1(K, Converter.concatenateBytes(SQN, RAND, AMF));
        if (!Arrays.equals(MAC, XMAC)) {
            System.out.println("AuthenticationVector: Comparison of MAC and XMAC failed.");
            return null;
        }

        //Verify SQN
        //TODO: Verify that SQN is in the correct range. See 3GPP TS 33.102 V15.1.0 Page 27

        return new Values(RAND, RES, CK, IK, AK, SQN, AMF);
    }

    public static class Values {
        public final byte[] RAND;
        public final byte[] RES;
        public final byte[] CK;
        public final byte[] IK;
        public final byte[] AK;
        public final byte[] SQN;
        public final byte[] AMF;

        Values(byte[] RAND, byte[] RES, byte[] CK, byte[] IK, byte[] AK, byte[] SQN, byte[] AMF) {
            this.RAND = RAND;
            this.RES = RES;
            this.CK = CK;
            this.IK = IK;
            this.AK = AK;
            this.SQN = SQN;
            this.AMF = AMF;
        }
    }
}
