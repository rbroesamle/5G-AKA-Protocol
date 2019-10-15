package Implementation.protocol.entities;

import Implementation.App;
import Implementation.protocol.additional.SIDF;
import Implementation.protocol.messages.Authentication_Request;
import Implementation.protocol.messages.N1_Registration_Request;
import Implementation.structure.Entity;
import Implementation.structure.Message;

import java.security.PublicKey;

public class EvilUE extends UE {

    private final byte[] SUPI_victim;

    public EvilUE(byte[] K_attacker, byte[] SUPI_attacker, PublicKey publicKey, byte[] SUPI_victim) {
        super(K_attacker, SUPI_attacker, publicKey);
        this.SUPI_victim = SUPI_victim;
    }

    @Override
    public void initiateAuthentication(SEAF seaf) {
        N1_Registration_Request n1 = new N1_Registration_Request(SIDF.concealSUPI(SUPI_victim, this.publicKey));
        sendMessage(n1, seaf);
        super.initiateAuthentication(seaf);
    }

    private boolean firstMessage = true;

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (firstMessage && message instanceof Authentication_Request && sender instanceof SEAF) {
            firstMessage = false;
        } else if (message instanceof Authentication_Request && sender instanceof SEAF) {
            if (App.DETAILED_AUTH_INFO) {
                System.out.println(getName() + ": Ignoring all further authenticate messages.");
            }
            return;
        }
        super.onReceiveMessage(message, sender);
    }
}
