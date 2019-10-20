package Implementation.protocol.additional;

import Implementation.helper.Converter;
import Implementation.helper.HmacSHA256;

//3GPP TS 33.102 V15.1.0 Page 26
public class MAF {//Message authentication functions

    public static byte[] f1(byte[] key, byte[] data) {
        //3GPP TS 33.102 V15.1.0 Page 26
        //TODO: Find the correct algorithm
        byte[] longVersion = Converter.expandBytesToLength(HmacSHA256.encode(key, data), 8);
        byte[] result = new byte[ParameterLength.MAC];
        System.arraycopy(longVersion, 0, result, 0, result.length);
        return result;
    }

    public static byte[] f2(byte[] key, byte[] data) {
        //3GPP TS 33.102 V15.1.0 Page 26
        //TODO: Find the correct algorithm
        return HmacSHA256.encode(key, data);
    }
}
