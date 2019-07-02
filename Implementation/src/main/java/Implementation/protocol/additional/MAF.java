package Implementation.protocol.additional;

import Implementation.helper.Converter;
import Implementation.helper.HmacSHA256;

//Message authentication function
//3GPP TS 33.102 V15.1.0 Page 26
public class MAF {

    public static byte[] f3(byte[] key, byte[] data) {
        //3GPP TS 33.102 V15.1.0 Page 26
        //TODO: Find the correct algorithm
        byte[] longVersion = Converter.expandBytesToLength(HmacSHA256.encode(key, data), 16);
        byte[] result = new byte[ParameterLength.CK];
        for (int i = 0; i < result.length; i++) {
            result[i] = longVersion[i];
        }
        return result;
    }

    public static byte[] f4(byte[] key, byte[] data) {
        //3GPP TS 33.102 V15.1.0 Page 26
        //TODO: Find the correct algorithm
        byte[] longVersion = Converter.expandBytesToLength(HmacSHA256.encode(key, data), 16);
        byte[] result = new byte[ParameterLength.IK];
        for (int i = 0; i < result.length; i++) {
            result[i] = longVersion[i];
        }
        return result;
    }

    public static byte[] f5(byte[] key, byte[] data) {
        //3GPP TS 33.102 V15.1.0 Page 26
        //TODO: Find the correct algorithm
        byte[] longVersion = Converter.expandBytesToLength(HmacSHA256.encode(key, data), 6);
        byte[] result = new byte[ParameterLength.AK];
        for (int i = 0; i < result.length; i++) {
            result[i] = longVersion[i];
        }
        return result;
    }
}
