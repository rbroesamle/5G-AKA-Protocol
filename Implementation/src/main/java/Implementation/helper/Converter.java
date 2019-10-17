package Implementation.helper;

import java.math.BigInteger;

public class Converter {

    /**
     * Bytes to HEX
     * @param data A byte array
     * @return A string containing a hex representation of the data
     */
    public static String bytesToHex(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (byte b : data) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }

    /**
     * Int to bytes
     * @param data An integer
     * @return A byte array containing the integer from data.
     */
    public static byte[] intToBytes(int data) {
        BigInteger bigInteger = BigInteger.valueOf(data);
        return shrinkBytes(bigInteger.toByteArray());
    }

    /**
     * Bytes to int
     * @param data A byte array
     * @return An integer containing the contents of the data.
     */
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

    /**
     * Shrink Bytes
     * @param data A byte array
     * @return The data with all preceding zero bytes removed.
     */
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

    /**
     * Expand bytes to length
     * @param data A byte array
     * @param length Length
     * @return Add preceding zeros until the length Length has been reached.
     */
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

    /**
     * Concatenate Bytes
     * @param data Data
     * @return A byte array with the contents of Data in one array.
     */
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

    /**
     * Split bytes
     * @param data Data
     * @param lengths Lengths
     * @return Spliting the data from right to left into arrays of size Length.
     */
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
