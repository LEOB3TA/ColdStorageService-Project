package unibo.serviceaccessbe.controller;

import org.springframework.web.bind.annotation.*;
import unibo.serviceaccessbe.utils.UtilsStatus;

import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceAccessController {

    private final Logger logger = Logger.getLogger(ServiceAccessController.class.getName());

    /**
     * This method is called when the user wants to store food in the css
     * @param quantity
     * @throws Exception
     */
    @GetMapping("/store-food")
    public void storeFood(@RequestParam(value = "quantity", defaultValue = "0") double quantity) throws Exception {
        // double quantityDouble = Double.parseDouble(quantity);
        if (quantity > 0) {
            logger.info("storeFood request received of quantity: " + quantity);

            // TODO: send request to an Actor through sendDataDispatcher via Qak
            final String msg = "msg(storeFood, request, ws_gui, guicontroller, storeFood(" + quantity + "), 1)"; // TODO: cambiare ws_gui con il nome del nuovo attore QAK
            UtilsStatus.connTCP.request(msg);

        } else {
            // TODO: handle error
            logger.warning("storeFood request rejected");
        }

    }

    /*
      IDEA DI BASE
      Inviare richieste e utilizzare il pattern observer per aggiornare il FE
     */

    @GetMapping("/send-ticket")
    public void ticketEvalutation(@RequestParam(value = "id") int ticketId) throws Exception {
        logger.info("sendTicket request received");
        if (ticketId > 0) { // TODO: CHECK IF 0 IS ACCEPTED
        final String msg = "msg(sendTicket, request, ws_gui, guicontroller, sendTicket(" + ticketId + "), 1)"; // TODO: cambiare ws_gui con il nome del nuovo attore QAK
            UtilsStatus.connTCP.request(msg);
        }
    }

    @GetMapping("/deposit")
    public void deposit(@RequestParam(value = "id") int ticketId) throws Exception {
        logger.info("deposit request received");
        if (ticketId > 0) { // TODO: CHECK IF 0 IS ACCEPTED
            final String msg = "msg(deposit, request, ws_gui, guicontroller, deposit(" + ticketId + "), 1)"; // TODO: cambiare ws_gui con il nome del nuovo attore QAK
            UtilsStatus.connTCP.request(msg);
        }
    }
    /*
    Alternativa unico mapping + observers
    @GetMapping("/")
    public String entry(Model viewmodel){
        return buildThePage(viewmodel);
    }

    @PostMapping("/getData")
    public String update(Model viewmodel, @RequestParam String ipaddr){
        logger.info("deposit request received");
        final String msg = "msg(getData, dispatch, ws_gui, guicontroller, getData(), 1)"; // TODO: cambiare ws_gui con il nome del nuovo attore QAK
        UtilsStatus.connTCP.forward(msg);

    }

     */
}
