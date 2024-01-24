package com.unibo.servicestatusbe.controller;

import com.unibo.servicestatusbe.dto.ResponseMessage;
import com.unibo.servicestatusbe.model.*;
import com.unibo.servicestatusbe.model.ServiceStatusDTO;
import com.unibo.servicestatusbe.service.WebSocketService;
import com.unibo.servicestatusbe.utils.UtilsCoapObserver;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommSystemConfig;

import java.security.Principal;
import java.util.List;
import java.util.Objects;


@RestController
public class WebSocketController {

    // Observer connection
    private static final String ctxName = "ctx_prototipo3";
    private static final String actorName = "transporttrolley";//"guicontroller"
    private static final String actorGui = "guicontroller";
    private static final String actorService = "coldstorageservice";
    private static Interaction2021 conn;
    public static Interaction2021 connTCP;
    // end Observer conf
    final SimpMessagingTemplate template;
    final Environment env;
    final ServiceConfigDTO serviceConfigDTO;
    final ServiceStatusDTO serviceStatusDTO = new ServiceStatusDTO();
    private final List<String> sessionList;
    private final WebSocketService service;

    // *********************************Observer connection
    public static CoapConnection connectCoap(String ip, int port) {
        try {
            CommSystemConfig.tracing = true;
            String path = ctxName + "/" + actorName;
            conn = new CoapConnection(ip + ":" + port, path);
            ColorsOut.out("[UtilsStatusGUI] connect Tcp conn:" + conn);
            ColorsOut.outappl("[UtilsStatusGUI] connect Coap conn:" + conn, ColorsOut.CYAN);
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] connect with GUIupdater ERROR:" + e.getMessage());
        }
        return (CoapConnection) conn;
    }

    public static void connectTCP(String ip, int port) {
        try {
            CommSystemConfig.tracing = true;
            connTCP = TcpClientSupport.connect(ip, port, 10);
            ColorsOut.out("[UtilsStatusGUI] connect Tcp conn:" + conn);
            ColorsOut.outappl("[UtilsStatusGUI] connect Tcp conn:" + conn, ColorsOut.CYAN);
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] connect with GUIupdater ERROR:" + e.getMessage());
        }
    }

    public static void sendMsg() {
        try {
            String msg = "msg(get_data, dispatch, ws_gui, " + actorGui + ", get_data(_), 1)";
            ColorsOut.outappl("[UtilsStatusGUI] sendMsg msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            connTCP.forward(msg);
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] sendMsg on:" + conn + " ERROR:" + e.getMessage());
        }
    }
    // TODO: finish modifications
    public static void sendStore(double w){
        try {
            String msg = "msg(storeFood, dispatch, coldstorageservice, " + actorService + ", storeFood("+w+"), 1)";
            ColorsOut.outappl("[UtilsStatusGUI] sendMsg msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            connTCP.forward(msg);
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] sendMsg on:" + conn + " ERROR:" + e.getMessage());
        }
    }
    public static void sendDeposit(int t){
        try {
            String msg = "msg(deposit, dispatch, coldstorageservice, " + actorService + ", deposit("+t+"), 1)";
            ColorsOut.outappl("[UtilsStatusGUI] sendMsg msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            connTCP.forward(msg);
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] sendMsg on:" + conn + " ERROR:" + e.getMessage());
        }
    }

    // ********************************************end Observer conf

    public WebSocketController(SimpMessagingTemplate template, Environment env, ServiceConfigDTO serviceConfigDTO, List<String> sessionList, WebSocketService service) {
        this.template = template;
        this.env = env;
        this.serviceConfigDTO = serviceConfigDTO;
        this.sessionList = sessionList;
        this.service = service;
        System.out.println("INITIAL SERVICE CONFIG -----------------------\n " + serviceConfigDTO.toJson());
    }

    @SubscribeMapping("/topic/message")
    public TextMessageDTO subscribe() {
        connectCoap("localhost", 8099).observeResource(new UtilsCoapObserver());
        connectTCP("localhost", 8099);
        sendMsg();

        System.out.println("Subscribed to /topic/message");

        TextMessageDTO message = new TextMessageDTO("Subscribed to /topic/message");
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

    @PostMapping(value = "/send-private", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendMessage(@RequestParam("id") String id, @RequestBody ServiceStatusDTO serviceStatusDTO) {
        if (serviceStatusDTO.getCurrentWeight() > serviceConfigDTO.getMaxWeight()) {
            return new ResponseEntity<>("Too much", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (serviceStatusDTO.getCurrentWeight() < 0) {
            return new ResponseEntity<>("Negative weight", null, HttpStatus.BAD_REQUEST);
        }
        template.convertAndSendToUser(id, "/queue/greetings", "CIAOOOOO");
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

    @PostMapping(value = "/store-food", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeFood(@RequestBody StoreFoodRequestDTO storeFoodRequestDTO) {
        if (storeFoodRequestDTO.getQuantity() > serviceConfigDTO.getMaxWeight()) {
            return new ResponseEntity<>("Too much", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (storeFoodRequestDTO.getQuantity() < 0) {
            return new ResponseEntity<>("Negative weight", null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Service Status successfully updated", null, HttpStatus.OK);
    }



    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        String destination = (String) Objects.requireNonNull(event.getMessage().getHeaders().get("simpDestination"));
        StompHeaderAccessor accessor;
        String sessionId;
        switch (destination) {
            case "/user/queue/store-food":
                accessor = StompHeaderAccessor.wrap(event.getMessage());
                sessionId = accessor.getMessageHeaders().get(SimpMessageHeaderAccessor.SESSION_ID_HEADER, String.class);
                sessionList.add(sessionId);
                System.out.println("Session Id" + sessionId + " subscribed to " + destination);
                assert sessionId != null;
                /* TODO:
                conn.sendMsg();
                conn.sendStore();
                * */
                template.convertAndSendToUser(sessionId, "/queue/responses", "CIAOOOO");
                break;
            case "/user/queue/deposit":
                accessor = StompHeaderAccessor.wrap(event.getMessage());
                sessionId = accessor.getMessageHeaders().get(SimpMessageHeaderAccessor.SESSION_ID_HEADER, String.class);
                /* TODO:
                conn.sendMsg();
                conn.sendDeposit();
                * */
                System.out.println("Session Id" + sessionId + " subscribed to " + destination);
                assert sessionId != null;
                template.convertAndSendToUser(sessionId, "/queue/responses", "CIAOOOO");
                break;
            case "/topic/message":
                break;
            default:
                System.out.println("Mammt");
        }
    }

    public static void sendToAll(String message) {
        try {
            ColorsOut.outappl("WebSocketController | sendToAll String: " + message, ColorsOut.CYAN);
            sendToAll(String.valueOf(new TextMessage(message)));
        } catch (Exception e) {
            ColorsOut.outerr("WebSocketController | sendToAll String ERROR:" + e.getMessage());
        }
    }

    @MessageMapping("/greetings")
    @SendTo("/queue/greetings")
    public void reply(@Payload String message, Principal user){
        template.convertAndSendToUser(user.getName(), "/queue/greetings", message);
    }

    @MessageMapping("/store-food/{sessionId}")
    @SendTo("/user/queue/store-food/{sessionId}")
    public TicketResponseDTO handleStoreFoodRequest(StoreFoodRequestDTO requestDTO, @PathVariable("sessionId") String sessionId) {
        if (requestDTO.getQuantity() < 0) {
            // return new ResponseEntity<>("Negative weight", null, HttpStatus.BAD_REQUEST);
        }
        if (requestDTO.getQuantity() > serviceConfigDTO.getMaxWeight() || serviceStatusDTO.getCurrentWeight() + requestDTO.getQuantity() > serviceConfigDTO.getMaxWeight()) {
            // return new ResponseEntity<>("Too much", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // UPDATE STATUS
        serviceConfigDTO.setCurrentWeight(serviceStatusDTO.getCurrentWeight()+requestDTO.getQuantity());
        TicketResponseDTO result = new TicketResponseDTO();
        result.setTicketNumber( serviceStatusDTO.getCurrentTicket());
        serviceStatusDTO.setCurrentTicket(serviceStatusDTO.getCurrentTicket()+1);
        template.convertAndSendToUser(sessionId, "/user/queue/store-food", result);
        return result;
    }

    @MessageMapping("/deposit/{sessionId}")
    @SendTo("/usr/queue/deposit/{sessionId}")
    public String HandleDeposit(TicketResponseDTO depositDTO, @PathVariable("sessionId") String sessionId){
        if (depositDTO.getTicketNumber() <0){
            return "invalid";
        }
        //update
        TicketResponseDTO result = new TicketResponseDTO();
        result.setTicketNumber( serviceStatusDTO.getCurrentTicket());
        return result.toString();
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        System.out.println("Session disconnected");
        System.out.println(event.getMessage());
        sessionList.remove(event.getSessionId());
    }

    @PostMapping("/send-private-message/{id}")
    public void sendPrivateMessage(@PathVariable("id") String id, @RequestBody ResponseMessage message) {
        service.sendNotificationToSpecificUser(id, message.getContent());
    }


}
