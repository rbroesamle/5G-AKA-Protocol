package Implementation.helper;

import java.util.Arrays;

public class Calculator {

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

    public static boolean equals(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }
}
