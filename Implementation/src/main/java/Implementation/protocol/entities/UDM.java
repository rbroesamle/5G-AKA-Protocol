package Implementation.protocol.entities;

import Implementation.helper.Converter;
import Implementation.protocol.additional.KDF;
import Implementation.protocol.additional.SIDF;
import Implementation.protocol.data.Data_5G_HE_AV;
import Implementation.protocol.data.Data_AUTN;
import Implementation.protocol.helper.AuthenticationVector;
import Implementation.protocol.messages.Nudm_Authentication_Get_Response;
import Implementation.protocol.messages.Nudm_UEAuthentication_Get_Request;
import Implementation.structure.Entity;
import Implementation.structure.Message;

import java.security.PrivateKey;

public class UDM extends Entity {

    private final byte[] K;
    private final byte[] AMF;

    private final PrivateKey privateKey;

    public UDM(byte[] K, byte[] AMF, PrivateKey privateKey) {
        this.K = K;
        this.AMF = AMF;
        this.privateKey = privateKey;
    }

    @Override
    public String getName() {
        return "UDM";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof Nudm_UEAuthentication_Get_Request && sender instanceof AUSF) {
            //Received Nudm_UEAuthentication_ Get Request
            Nudm_UEAuthentication_Get_Request getRequest = (Nudm_UEAuthentication_Get_Request) message;
            AUSF ausf = (AUSF) sender;

            //Always choose 5G-AKA as authentication method.
            Data_5G_HE_AV AV = generateAVsAndInvokeSIDF(getRequest, ausf);

            Nudm_Authentication_Get_Response authInfoResp = getGetResponse(getRequest, ausf, AV);

            sendMessage(authInfoResp, ausf);
        } else {
            String name = message == null ? null : message.getName();
            System.err.println(getName() + ": Received an unusual message: " + (name == null ? "" : name) + ". Ignoring it.");
        }
    }

    private Data_5G_HE_AV generateAVsAndInvokeSIDF(Nudm_UEAuthentication_Get_Request getRequest, AUSF ausf) {
        AuthenticationVector.Values av = AuthenticationVector.generate(this.K, this.AMF);

        byte[] KEY = Converter.concatenateBytes(av.CK, av.IK);

        //Derive Kausf
        //3GPP TS 33.501 V15.34.1 Page 154
        byte[] Fc_Kausf = Converter.intToBytes(0x6A);
        byte[][] Pis_Kausf = {
                getRequest.servingNetworkName,
                av.SQNxorAK
        };
        int SQNxorAKLength = Converter.shrinkBytes(av.SQNxorAK).length == 0 ? 0x00 : 0x06;
        byte[][] Lis_Kausf = {
                null,
                Converter.intToBytes(SQNxorAKLength)
        };
        byte[] Kausf = KDF.deriveKey(KEY, Fc_Kausf, Pis_Kausf, Lis_Kausf);


        //Derive XRES*
        //3GPP TS 33.501 V15.34.1 Page 155
        byte[] Fc_XRESstar = Converter.intToBytes(0x6B);
        byte[][] Pis_XRESstar = {
                getRequest.servingNetworkName,
                av.RAND,
                av.XRES
        };
        int RANDLength = Converter.shrinkBytes(av.RAND).length == 0 ? 0x00 : 0x10;
        byte[][] Lis_XRESstar = {
                null,
                Converter.intToBytes(RANDLength),
                null
        };
        byte[] XRESstar = KDF.deriveKey(KEY, Fc_XRESstar, Pis_XRESstar, Lis_XRESstar);


        Data_AUTN AUTN = new Data_AUTN(av.SQNxorAK, av.AMF, av.MAC);
        return new Data_5G_HE_AV(av.RAND, AUTN, XRESstar, Kausf);
    }

    private Nudm_Authentication_Get_Response getGetResponse(Nudm_UEAuthentication_Get_Request getRequest,
                                                            AUSF ausf, Data_5G_HE_AV av) {
        byte[] SUPI = null;
        if (getRequest.SUCI != null) {
            SUPI = SIDF.deconcealSUCI(getRequest.SUCI, this.privateKey);
        }
        return new Nudm_Authentication_Get_Response(av, SUPI);
    }

}
