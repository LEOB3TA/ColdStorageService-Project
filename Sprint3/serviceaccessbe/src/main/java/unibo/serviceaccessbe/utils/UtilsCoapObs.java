package unibo.serviceaccessbe.utils;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.basicomm23.utils.ColorsOut;
import unibo.serviceaccessbe.WSConf;

public class UtilsCoapObs  implements CoapHandler {
    @Override
    public void onLoad(CoapResponse response) {
        ColorsOut.outappl("UtilsCoapObserver changed!" + response.getResponseText(), ColorsOut.GREEN);
        WSConf.wshandler.sendToAll(""+response.getResponseText());
    }

    @Override
    public void onError() {
        ColorsOut.outerr("UtilsCoapObserver observe error!");
    }
}

