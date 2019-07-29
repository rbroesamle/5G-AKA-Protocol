package Implementation.helper;

public class Generator {

    /**
     * Random bytes
     * @param length L
     * @return A byte array with random values of length L.
     */
    public static byte[] randomBytes(int length) {
        byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            int randomNumber = (int) (Math.random() * 256);
            byte[] randomBytesArray = Converter.expandBytesToLength(Converter.intToBytes(randomNumber), 1);
            array[i] = randomBytesArray[0];
        }
        return array;
    }
}
