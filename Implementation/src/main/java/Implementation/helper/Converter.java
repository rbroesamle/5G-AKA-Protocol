package Implementation.helper;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;

public class Converter {

    public static String bytesToHex(byte[] data) {
        return DatatypeConverter.printHexBinary(data);
    }

    public static byte[] hexToBytes(String data) {
        return DatatypeConverter.parseHexBinary(data);
    }

    public static byte[] intToBytes(int data) {
        BigInteger bigInteger = BigInteger.valueOf(data);
        return shrinkBytes(bigInteger.toByteArray());
    }

    public static int bytesToInt(byte[] data) {
        if (data == null || data.length == 0) {
            return 0;
        }
        byte[] expandedData;
        if (data.length < 4) {
            expandedData = expandBytesToLength(data, 4);
        } else {
            expandedData = data;
        }
        return (0xff & expandedData[0]) << 24 |
                (0xff & expandedData[1]) << 16 |
                (0xff & expandedData[2]) << 8 |
                (0xff & expandedData[3]) << 0;
    }

    public static byte[] shrinkBytes(byte[] data) {
        int cap = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                cap = i;
            } else {
                break;
            }
        }

        if (cap < 0) {
            return data;
        }

        byte[] arr = new byte[data.length - cap - 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = data[cap + 1 + i];
        }

        return arr;
    }

    public static byte[] expandBytesToLength(byte[] data, int length) {
        if (data == null) {
            return new byte[length];
        }
        if (length < data.length) {
            return data;
        }
        byte[] arr = new byte[length];
        for (int i = 0; i < data.length && i < length; i++) {
            arr[length - 1 - i] = data[data.length - 1 - i];
        }
        return arr;
    }

    public static byte[] concatenateBytes(byte[]... data) {
        int length = 0;
        for (byte[] array : data) {
            length += array.length;
        }
        //Concatenate bytes.
        byte[] result = new byte[length];
        int index = 0;
        for (byte[] array : data) {
            for (byte b : array) {
                result[index] = b;
                index++;
            }
        }
        return result;
    }

    public static byte[][] splitBytes(byte[] data, int... lengths) {
        int index = 0;
        byte[][] result = new byte[lengths.length][];
        for (int i = 0; i < lengths.length; i++) {
            int length = lengths[i];
            if (data.length < index) {
                result[i] = new byte[0];
            } else if (data.length < index + length) {
                byte[] arr = new byte[data.length - index];
                System.arraycopy(data, index, arr, 0, data.length - index);
                result[i] = arr;
            } else {
                byte[] arr = new byte[length];
                System.arraycopy(data, index, arr, 0, length);
                result[i] = arr;
            }
            index += length;
        }
        return result;
    }
}
