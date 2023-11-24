package com.unibo.servicestatusbe.utils;

import com.unibo.servicestatusbe.controller.WebSocketController;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.basicomm23.utils.ColorsOut;

public class UtilsCoapObserver implements CoapHandler {

    @Override
    public void onLoad(CoapResponse response) {
        ColorsOut.outappl("UtilsCoapObserver changed!" + response.getResponseText(), ColorsOut.GREEN);
        WebSocketController.sendToAll(""+response.getResponseText());
    }

    @Override
    public void onError() {

    }
}
