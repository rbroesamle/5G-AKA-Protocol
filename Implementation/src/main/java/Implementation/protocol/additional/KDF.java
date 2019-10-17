package Implementation.protocol.additional;

import Implementation.helper.Converter;
import Implementation.helper.HmacSHA256;

import java.util.ArrayList;

public class KDF {//Key derivation function

    public static byte[] deriveKey(byte[] key, byte[] Fc, byte[][] Pis, byte[][] Lis) {
        //3GPP TS 33.220 V15.4.0 Page 46
        byte[] S = calculateS(Converter.expandBytesToLength(Fc, 2), Pis, Lis);
        return HmacSHA256.encode(key, S);
    }

    private static byte[] calculateS(byte[] Fc, byte[][] Pis, byte[][] Lis) {
        //3GPP TS 33.220 V15.4.0 Page 46 and 47

        ArrayList<Byte> listS = new ArrayList<>();

        //Add Fc to S.
        if (Fc.length == 2) {
            for (byte fc : Fc) {
                listS.add(fc);
            }
        } else {
            System.out.println("Error in KDF: Input parameter Fc is not of length 2!");
            return null;
        }

        if (Pis.length != Lis.length) {
            System.out.println("Error in KDF: Input parameter Pis and Lis are not of the same length!");
            return null;
        }

        //Add all Pis to S
        for (int i = 0; i < Pis.length; i++) {
            byte[] _Pi = Pis[i];
            byte[] _Li = Lis[i];
            byte[] Li;
            byte[] Pi;
            if (_Li != null && _Li.length <= 2 && Converter.bytesToInt(_Li) <= _Pi.length) {
                //Li is set. Expanding Pi to match the length Li.
                Li = Converter.expandBytesToLength(_Li, 2);
                Pi = Converter.expandBytesToLength(_Pi, Converter.bytesToInt(_Li));
            } else {
                //Calculating Li
                int count = _Pi.length;
                if (count > 65535) {
                    System.out.println("Error in KDF: Input parameter Pi is too long!");
                }
                Li = Converter.expandBytesToLength(Converter.intToBytes(count), 2);
                Pi = _Pi;
            }

            for (byte b : Pi) {
                listS.add(b);
            }
            for (byte b : Li) {
                listS.add(b);
            }
        }

        //Converting the listS back to an array.
        byte[] bytes = new byte[listS.size()];
        for (int i = 0; i < listS.size(); i++) {
            bytes[i] = listS.get(i);
        }
        return bytes;
    }
}
