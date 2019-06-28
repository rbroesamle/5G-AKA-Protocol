package Implementation.helper;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Converter {

    public static String bytesToHex(byte[] data) {
        return DatatypeConverter.printHexBinary(data);
    }

    public static byte[] hexToBytes(String data) {
        return DatatypeConverter.parseHexBinary(data);
    }

    public static byte[] stringToBytes(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }

    public static String bytesToString(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    public static byte[] intToBytes(int data) {
        BigInteger bigInteger = BigInteger.valueOf(data);
        return shrinkBytes(bigInteger.toByteArray());
    }

    public static int bytesToInt(byte[] data) {
        return ByteBuffer.wrap(data).getInt();
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
            System.out.println("Warning in Converter.expandBytes: array is longer than specified length!");
            return data;
        }
        byte[] arr = new byte[length];
        for (int i = 0; i < data.length && i < length; i++) {
            arr[length - 1 - i] = data[data.length - 1 - i];
        }
        return arr;
    }

    public static byte[] randomBytes(int length) {
        byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            int randomNumber = (int) (Math.random() * 256);
            byte[] randomBytesArray = expandBytesToLength(intToBytes(randomNumber), 1);
            array[i] = randomBytesArray[0];
        }
        return array;
    }
}
