package com.unibo.servicestatusbe.controller;

import com.unibo.servicestatusbe.model.ServiceConfigDTO;
import com.unibo.servicestatusbe.model.ServiceStatusDTO;
import com.unibo.servicestatusbe.model.TextMessageDTO;
import com.unibo.servicestatusbe.utils.UtilsCoapObserver;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommSystemConfig;

@RestController
public class WebSocketController {

    // Observer connection
    private static final String ctxName = "ctx_prototipo3";
    private static final String actorName = "transporttrolley";//"guicontroller"
    private static Interaction2021 conn;
    public static Interaction2021 connTCP;
    // end Observer conf
    final SimpMessagingTemplate template;
    final Environment env;
    final ServiceConfigDTO serviceConfigDTO;
    // *********************************Observer connection
    public static CoapConnection connectCoap(String ip, int port){
        try{
            CommSystemConfig.tracing = true;
            String path = ctxName + "/" + actorName;
            conn = new CoapConnection(ip + ":" + port, path);
            ColorsOut.out("[UtilsStatusGUI] connect Tcp conn:" + conn );
            ColorsOut.outappl("[UtilsStatusGUI] connect Coap conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("[UtilsStatusGUI] connect with GUIupdater ERROR:"+e.getMessage());
        }
        return (CoapConnection) conn;
    }

    public static void connectTCP(String ip, int port){
        try {
            CommSystemConfig.tracing = true;
            connTCP = TcpClientSupport.connect(ip, port, 10);
            ColorsOut.out("[UtilsStatusGUI] connect Tcp conn:" + conn );
            ColorsOut.outappl("[UtilsStatusGUI] connect Tcp conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("[UtilsStatusGUI] connect with GUIupdater ERROR:"+e.getMessage());
        }
    }

    public static void sendMsg() {
        try {
            String msg = "msg(get_data, dispatch, ws_gui, " + actorName + ", get_data(_), 1)";
            ColorsOut.outappl("[UtilsStatusGUI] sendMsg msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            connTCP.forward(msg);
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] sendMsg on:" + conn + " ERROR:" + e.getMessage());
        }
    }

    // ********************************************end Observer conf

    public WebSocketController(SimpMessagingTemplate template, Environment env, ServiceConfigDTO serviceConfigDTO) {
        this.template = template;
        this.env = env;
        this.serviceConfigDTO = serviceConfigDTO;
        System.out.println("INITIAL SERVICE CONFIG -----------------------\n " + serviceConfigDTO.toJson());

    }

    @SubscribeMapping("/topic/message")
    public TextMessageDTO subscribe() {
        connectCoap("localhost", 8099).observeResource(new UtilsCoapObserver());
        connectTCP("localhost",8099);
        sendMsg();

        System.out.println("Subscribed to /topic/message");

        TextMessageDTO message =  new TextMessageDTO("Subscribed to /topic/message");
        template.convertAndSend("/topic/message", message);
        return message;
    }

    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendMessage(@RequestBody ServiceStatusDTO serviceStatusDTO) {
        if (serviceStatusDTO.getCurrentWeight() > serviceConfigDTO.getMaxWeight()) {
            return new ResponseEntity<>("Too much", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (serviceStatusDTO.getCurrentWeight() < 0) {
            return new ResponseEntity<>("Negative weight", null, HttpStatus.BAD_REQUEST);
        }
        template.convertAndSend("/topic/message", serviceStatusDTO);
        return new ResponseEntity<>("Service Status successfully updated", null, HttpStatus.OK);
    }

    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload TextMessageDTO textMessageDTO) {
        // receive message from client
    }

    @SendTo("/topic/message")
    public TextMessageDTO broadcastMessage(@Payload TextMessageDTO textMessageDTO) {
        return textMessageDTO;
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        System.out.println("Subscribed to /topic/message");
        template.convertAndSend("/topic/message", serviceConfigDTO.toJson());

    }
    public static void sendToAll(String message){
        try{
            ColorsOut.outappl("WebSocketController | sendToAll String: " + message, ColorsOut.CYAN);
            sendToAll( new TextMessage(message)) ;
        }catch( Exception e ){
            ColorsOut.outerr("WebSocketController | sendToAll String ERROR:"+e.getMessage());
        }
    }

    public static void sendToAll(TextMessage message){
        /*
        TODO: To send to all
        Iterator<WebSocketSession> iter =session.iterator();
        while( iter.hasNext() ){
            try{
                WebSocketSession session = iter.next();
                ColorsOut.outappl("WebSocketHandler | sendToAll " +
                        message.getPayload() + " for session " + session.getRemoteAddress() , ColorsOut.MAGENTA);
                //synchronized(session){
                session.sendMessage(message);
                //}
            }catch(Exception e){
                ColorsOut.outerr("WebSocketHandler | TextMessage ERROR:"+e.getMessage());
            }
        }
         */

        System.out.println(message.getPayload());
    }

}
