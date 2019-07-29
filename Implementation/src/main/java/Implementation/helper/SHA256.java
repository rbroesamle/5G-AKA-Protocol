package Implementation.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    /**
     * Encode SHA256
     * @param data Data
     * @return SHA256 encoded data.
     */
    public static byte[] encode(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
