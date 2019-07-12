package Implementation.protocol.entities;

import Implementation.protocol.data.Data_5G_HE_AV;
import Implementation.protocol.messages.Nudm_UEAuthentication_Get_Request;
import Implementation.protocol.messages.Nudm_Authentication_Get_Response;
import Implementation.structure.Entity;
import Implementation.structure.Message;

public class UDM extends Entity {

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
            Data_5G_HE_AV aVs = generateAVsAndInvokeSIDF(getRequest, ausf);

            Nudm_Authentication_Get_Response authInfoResp = getGetResponse(getRequest, ausf);

            sendMessage(authInfoResp, ausf);
        } else {
            String name = message == null ? null : message.getName();
            System.err.println(getName() + ": Received an unusual message: " + (name == null ? "" : name) + ". Ignoring it.");
        }
    }

    private Data_5G_HE_AV generateAVsAndInvokeSIDF(Nudm_UEAuthentication_Get_Request getRequest, AUSF ausf) {
        //TODO: Invoke the SIDF, as decribed on page 40.
        //TODO: Generate the AVs, as describes on page 44.
        return null;
    }

    private Nudm_Authentication_Get_Response getGetResponse(Nudm_UEAuthentication_Get_Request getRequest, AUSF ausf) {
        //TODO
        return new Nudm_Authentication_Get_Response();
    }

}
