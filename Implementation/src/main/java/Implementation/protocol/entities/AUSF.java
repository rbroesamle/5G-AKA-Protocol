package Implementation.protocol.entities;

import Implementation.protocol.messages.*;
import Implementation.structure.Entity;
import Implementation.structure.Message;

public class AUSF extends Entity {

    public ARPF arpf = null;
    public SEAF seaf = null;

    @Override
    public String getName() {
        return "AUSF";
    }

    @Override
    public void onReceiveMessage(Message message, Entity sender) {
        if (message instanceof Nausf_UEAuthentication_Authenticate_Request && sender instanceof SEAF) {
            //Received 5G-AIR, sending Auth Info Req to the ARPF
            Nausf_UEAuthentication_Authenticate_Request fiveGAir = (Nausf_UEAuthentication_Authenticate_Request) message;
            SEAF seaf = (SEAF) sender;

            if (checkIfSeafIsEntitledToUseSnName(fiveGAir, seaf)) {
                Nudm_UEAuthentication_Get_Request authInfoReq = getAuthInfoReqFrom5gAir(fiveGAir, seaf);

                sendMessage(authInfoReq, this.arpf);
            }
        } else if (message instanceof Nudm_Authentication_Get_Response && sender instanceof ARPF) {
            //Storing the XRES*, calculating the HXRES* and dend 5G_AIA to the SEAF.
            Nudm_Authentication_Get_Response authInfoResp = (Nudm_Authentication_Get_Response) message;
            ARPF arpf = (ARPF) sender;

            HXRES hxres = storeXresAndCalculateHxresFromAuthInfoResp(authInfoResp, arpf);
            if (hxres != null) {
                Nausf_UEAuthentication_Authenticate_Response fiveGAia = get5gAiaFromAuthInfoRespAndHxres(authInfoResp, arpf, hxres);

                sendMessage(fiveGAia, this.seaf);
            }
        } else if (message instanceof Nausf_UEAuthentication_Confirmation_Request && sender instanceof SEAF) {
            //Verify the 5G-AC and send 5G-ACA back
            Nausf_UEAuthentication_Confirmation_Request fiveGAc = (Nausf_UEAuthentication_Confirmation_Request) message;
            SEAF seaf = (SEAF) sender;

            if (verify5gAc(fiveGAc, seaf)) {
                Nausf_UEAuthentication_Confirmation_Response fiveGAca = get5GAcaFrom5gAc(fiveGAc, seaf);

                sendMessage(fiveGAca, seaf);
            }
        } else {
            System.err.println(getName() + ": Received an unusual message. Ignoring it.");
        }
    }

    /**
     *
     * @param fiveGAir
     * @param seaf
     * @return true if SEAF is entitled to use the ServingNetworkName
     */
    private boolean checkIfSeafIsEntitledToUseSnName(Nausf_UEAuthentication_Authenticate_Request fiveGAir, SEAF seaf) {
        //3GPP TS 33.501 V0.7.0 Page 31
        //TODO
        return true;
    }

    private Nudm_UEAuthentication_Get_Request getAuthInfoReqFrom5gAir(Nausf_UEAuthentication_Authenticate_Request fiveGAir, SEAF seaf) {
        //TODO
        return new Nudm_UEAuthentication_Get_Request();
    }

    private HXRES storeXresAndCalculateHxresFromAuthInfoResp(Nudm_Authentication_Get_Response authInfoResp, ARPF arpf) {
        //TODO
        return new HXRES();
    }

    private Nausf_UEAuthentication_Authenticate_Response get5gAiaFromAuthInfoRespAndHxres(Nudm_Authentication_Get_Response authInfoResp, ARPF arpf, HXRES hxres) {
        //TODO
        return new Nausf_UEAuthentication_Authenticate_Response();
    }

    /**
     *
     * @param fiveGAc
     * @param seaf
     * @return true if the verification was successful.
     */
    private boolean verify5gAc(Nausf_UEAuthentication_Confirmation_Request fiveGAc, SEAF seaf) {
        //TODO
        return true;
    }

    private Nausf_UEAuthentication_Confirmation_Response get5GAcaFrom5gAc(Nausf_UEAuthentication_Confirmation_Request fiveGAc, SEAF seaf) {
        //TODO
        return new Nausf_UEAuthentication_Confirmation_Response();
    }


    private class HXRES {

    }
}
