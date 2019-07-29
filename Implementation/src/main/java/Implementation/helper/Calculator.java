package Implementation.helper;

import java.util.Arrays;

public class Calculator {

    /**
     * XOR
     * @param a First byte array
     * @param b Second byte array
     * @return a xor b
     */
    public static byte[] xor(byte[] a, byte[] b) {
        int length;
        byte[] x;
        byte[] y;
        if (a.length == b.length) {
            length = a.length;
            x = a;
            y = b;
        } else if (a.length > b.length) {
            length = a.length;
            x = a;
            y = Converter.expandBytesToLength(b, length);
        } else {
            length = b.length;
            x = Converter.expandBytesToLength(a, length);
            y = b;
        }
        //Compute XOR.
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = (byte) ((x[i] ^ y[i]) & 0xff);
        }
        return result;
    }

    /**
     * Equals
     * @param a First byte array
     * @param b Second byte array
     * @return true if all elements of the first array equal the ones of the second array.
     */
    public static boolean equals(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }
}
