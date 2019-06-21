package Implementation.protocol.entities;

import Implementation.protocol.messages.Nudm_UEAuthentication_Get_Request;
import Implementation.protocol.messages.Nudm_Authentication_Get_Response;
import Implementation.structure.Entity;
import Implementation.structure.Message;

public class ARPF extends Entity {

    @Override
    public String getName() {
        return "ARPF";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof Nudm_UEAuthentication_Get_Request && sender instanceof AUSF) {
            //Select 5G-AKA as authentication method, generate AVs and send Auth Info Resp to the AUSF.
            Nudm_UEAuthentication_Get_Request authInfoReq = (Nudm_UEAuthentication_Get_Request) message;
            AUSF ausf = (AUSF) sender;

            //Always choose 5G-AKA as authentication method.
            AVs aVs = generateAVs(authInfoReq, ausf);

            Nudm_Authentication_Get_Response authInfoResp = getAuthInfoRespFromAuthInfoReq(authInfoReq, ausf);

            sendMessage(authInfoResp, ausf);
        }
    }

    private AVs generateAVs(Nudm_UEAuthentication_Get_Request authInfoReq, AUSF ausf) {
        //TODO
        return null;
    }

    private Nudm_Authentication_Get_Response getAuthInfoRespFromAuthInfoReq(Nudm_UEAuthentication_Get_Request authInfoReq, AUSF ausf) {
        //TODO
        return new Nudm_Authentication_Get_Response();
    }


    private class AVs {

    }
}
