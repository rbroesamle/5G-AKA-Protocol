package Implementation.protocol.additional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SIDF {

    public static byte[] deconcealSUCI(byte[] SUCI, PrivateKey privateKey) {
        //3GPP TS 33.501 V15.34.1 Page 96

        //MARK: Deviation 7
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            return cipher.doFinal(SUCI);
        } catch (NoSuchAlgorithmException |
                IllegalBlockSizeException |
                BadPaddingException |
                InvalidKeyException |
                NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] concealSUPI(byte[] SUPI, PublicKey publicKey) {
        //3GPP TS 33.501 V15.34.1 Page 96

        //MARK: Deviation 8
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return cipher.doFinal(SUPI);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                BadPaddingException |
                IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
