package Implementation.protocol.messages;

import Implementation.structure.Message;

//TODO
//In the documentation this message is called 'Nausf_UEAuthentication Authenticate Response'.
//It was renamed because an earlier message has the same name.
public class Nausf_UEAuthentication_Confirmation_Response implements Message {
    //3GPP TS 33.501 V15.34.1 Page 45

    //Indicating wether the authentication was successful.
    public final boolean wasSuccessful;

    //Kseaf
    public final byte[] Kseaf;

    //SUPI: Might be null.
    public final byte[] SUPI;

    @Override
    public String getName() {
        return "Nausf_UEAuthentication Authenticate Response";
    }

    public Nausf_UEAuthentication_Confirmation_Response(boolean wasSuccessful, byte[] Kseaf, byte[] SUPI) {
        this.wasSuccessful = wasSuccessful;
        this.Kseaf = Kseaf;
        this.SUPI = SUPI;
    }
}
