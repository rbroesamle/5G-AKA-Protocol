package Implementation.helper;

import javax.xml.bind.DatatypeConverter;

public class Converter {

    public static String bytesToHex(byte[] data) {
        return DatatypeConverter.printHexBinary(data);
    }

    public static byte[] hexToBytes(String data) {
        return DatatypeConverter.parseHexBinary(data);
    }
}
