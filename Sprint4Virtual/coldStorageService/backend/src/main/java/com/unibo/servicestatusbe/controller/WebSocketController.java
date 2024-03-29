package com.unibo.servicestatusbe.controller;
import com.unibo.servicestatusbe.model.*;
import com.unibo.servicestatusbe.service.WebSocketService;
import com.unibo.servicestatusbe.utils.UtilsCoapObserver;
import org.json.simple.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommSystemConfig;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


@RestController
public class WebSocketController {

    // Observer connection
    private static final String ctxName = "ctxcoldstorageservice";
    //private static final String ctxCSS = "";
    private static final String actorGui = "guicontroller";
    private static final String actorService = "coldstorageservice";
    private static Interaction2021 conn;
    public static Interaction2021 connTCP;
    private static String upd ="";
    // end Observer conf
    static SimpMessagingTemplate template;
    final Environment env;
    final ServiceConfigDTO serviceConfigDTO;
    //public static WeightDTO weightDTO;
    static final ServiceStatusDTO serviceStatusDTO = new ServiceStatusDTO();
    private static List<String> sessionList = Collections.emptyList() ;
    private final WebSocketService service;

    // *********************************Observer connection
    public CoapConnection connectCoap(String ip, int port) {
        try {
            CommSystemConfig.tracing = true;
            String path = ctxName + "/" + actorGui;
            conn = new CoapConnection(ip + ":" + port, path);
            ColorsOut.out("[UtilsStatusGUI] connect Tcp conn:" + conn);
            ColorsOut.outappl("[UtilsStatusGUI] connect Coap conn:" + conn, ColorsOut.CYAN);
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] connect with GUIupdater ERROR:" + e.getMessage());
        }
        return (CoapConnection) conn;
    }

    public void connectTCP(String ip, int port) {
        try {
            CommSystemConfig.tracing = true;
            connTCP = TcpClientSupport.connect(ip, port, 10);
            ColorsOut.out("[UtilsStatusGUI] connect Tcp conn:" + conn);
            ColorsOut.outappl("[UtilsStatusGUI] connect Tcp conn:" + conn, ColorsOut.CYAN);
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] connect with GUIupdater ERROR:" + e.getMessage());
        }
    }

    public static void getData() {
        try {
            String res = "";
            String msg = "msg(getData, dispatch, backend, " + actorGui + ", getData(_), 1)";
            ColorsOut.outappl("[UtilsStatusGUI] getData msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            //String coap = conn.receiveMsg();
            connTCP.forward(msg);
            //res = connTCP.receiveMsg();
            //ColorsOut.outappl("[UtilsStatusGui] update response"  +res, ColorsOut.YELLOW);
            //return res;
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] sendMsg on:" + conn + " ERROR:" + e.getMessage());
            //return "Error";
        }
    }

    public int sendStore(double w){
        try {
            String res = "";
            String msg = "msg(storeFood, request, backend, " + actorService + ", storeFood("+w+"), 1)";
            ColorsOut.outappl("[UtilsStatusGUI] sendStore msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            res = connTCP.request(msg);
            int start = res.lastIndexOf("(")+1;
            int end =  res.indexOf(")");
            //ColorsOut.outappl("[UtilsStatusGui] sendStore start: "  +start +" end " +end, ColorsOut.YELLOW);
            //getting the ticket
            int tic = Integer.parseInt(res.substring(start,end));
            ColorsOut.outappl("[UtilsStatusGui] sendStore ticket: "  +tic, ColorsOut.YELLOW);
            //update status
            return tic;
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] sendStore on:" + conn + " ERROR:" + e.getMessage());
            return -1;
        }
    }
    public String sendDeposit(int t){
        try {
            String res = "";
            String msg = "msg(sendTicket, request, backend, " + actorService + ", sendTicket("+t+"), 1)";
            ColorsOut.outappl("[UtilsStatusGUI] sendDeposit msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            res = connTCP.request(msg);
            return res;
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] sendDeposit on:" + conn + " ERROR:" + e.getMessage());
            return "errore";
        }
    }

    // ********************************************end Observer conf

    public WebSocketController(SimpMessagingTemplate template, Environment env, ServiceConfigDTO serviceConfigDTO, List<String> sessionList, WebSocketService service) {
        this.template = template;
        this.env = env;
        this.serviceConfigDTO = serviceConfigDTO;
        this.sessionList = sessionList;
        this.service = service;
        connectCoap("localhost", 8099).observeResource(new UtilsCoapObserver());
        connectTCP("localhost", 8099);
        System.out.println("INITIAL SERVICE CONFIG -----------------------\n " + serviceConfigDTO.toJson());
    }
    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        String destination = Objects.requireNonNull(event.getMessage().getHeaders().get("simpDestination")).toString();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getMessageHeaders().get(SimpMessageHeaderAccessor.SESSION_ID_HEADER, String.class);

        switch (destination) {
            case "/topic/updates":
                sessionList.add(sessionId);
                System.out.println("Session Id" + sessionId + " subscribed to " + destination);
                assert sessionId != null;
                //TODO: modify to avoid peso fantasma
                // protip use setCurr somewhere
                WeightDTO weightDTO = new WeightDTO(serviceStatusDTO.getCurrentWeight(), serviceConfigDTO.getMaxWeight());
                template.convertAndSend("/topic/updates", weightDTO);
                break;
            case "/user/queue/store-food":
                //sessionList.add(sessionId);
                System.out.println("Session Id" + sessionId + " subscribed to " + destination);
                assert sessionId != null;
                template.convertAndSendToUser(sessionId, "/queue/responses", "ticket");
                break;
            case "/user/queue/deposit":
                System.out.println("Session Id" + sessionId + " subscribed to " + destination);
                assert sessionId != null;
                template.convertAndSendToUser(sessionId, "/queue/responses", "CIAOOOO");
                break;
            case "/topic/status":
                System.out.println("Session subscribed to " + destination);
                assert sessionId != null;
                template.convertAndSend("/topic/status", serviceConfigDTO.toJson());
                break;
            default:
                System.out.println(destination);
                // System.out.println("Mammt");
        }
    }

    public static void sendToAll(String message) {
        try {
            upd = message;
            ColorsOut.outappl("WebSocketController | sendToAll String: " + message, ColorsOut.CYAN);
            //sendToAll();
            handleUpdates();
        } catch (Exception e) {
            ColorsOut.outerr("WebSocketController | sendToAll String ERROR:" + e.getMessage());
        }
    }

    @MessageMapping("/updates")
    @SendTo("/topic/updates")
    public static void handleUpdates(){
        //getData();
        System.out.println("Debug updates "+upd);
        String cleaned = upd.replace("GuiState(","").replace(")","");
        int arr = cleaned.lastIndexOf("=");
        String first = cleaned.substring(0, arr - 5);
        String pos = cleaned.substring(arr-5);
        //System.out.println(first+" co "+pos);
        String[] kvp = first.split(", ");
        String[] ttpos = pos.split("=");

        JSONObject obj = new JSONObject();

        for (String pair : kvp) {
            // Split the pair into key and value
            String[] kv = pair.split("=");
            //System.out.println(pair);
            String key = kv[0];
            String value = kv[1];

            if(key.equals("MAXW") || key.equals("CurrW")){
                Double val = Double.parseDouble(value);
                if(key.equals("CurrW")){
                    serviceStatusDTO.setCurrentWeight(val);
                }
                //weightDTO.setCurrentWeight(val);
                obj.put(key, val);
            }else if (key.startsWith("x")|| key.startsWith("y")||key.equals("rejected") || key.equals("ticketN")){
                int v = Integer.parseInt(value);
                obj.put(key, v);
            }else{
                obj.put(key, value);
            }
            // If the value is an array, parse it as such
//            if (value.startsWith("[") && value.endsWith("]")) {
//                value = value.substring(1, value.length() - 1); // remove brackets
//                String[] arrayValues = value.split(", "); // split by comma
//                JSONArray array = new JSONArray();
//                for (String v : arrayValues) {
//                    array.add(Double.valueOf(v)); // parse as double
//                }
//                obj.put(key, array);
//            } else {
//                // Otherwise, just put the value in the JSONObject
//                obj.put(key, value);
//            }

        }

        JSONObject positions = new JSONObject();
        String ttposArr = ttpos[1].substring(ttpos[1].indexOf("[")+1, ttpos[1].indexOf("]"));
        String[] ttposel = ttposArr.split(",");
//        for (String el : ttposel){
//            positions.add(Integer.parseInt(el.trim()));
//            System.out.println(el);
//        }
        for (int i=0; i<2;i++){
            if (i==0) {
                positions.put("x", Integer.parseInt(ttposel[i].trim()));
            }else{
                positions.put("y", Integer.parseInt(ttposel[i].trim()));
            }
        }

        obj.put(ttpos[0], positions);
        //System.out.println("Positions " +positions);
        String res = obj.toString();
        System.out.println("Update json "+ res);
        // template.convertAndSend("/topic/updates", res);
        WeightDTO weightDTO = new WeightDTO((Double) obj.get("CurrW"), (Double) obj.get("MAXW"));
        template.convertAndSend("/topic/updates",weightDTO);
        template.convertAndSend("/topic/status", res);
    }

    @MessageMapping("/store-food/{sessionId}")
    @SendTo("/user/queue/store-food/{sessionId}")
    public TicketResponseDTO handleStoreFoodRequest(StoreFoodRequestDTO requestDTO, @PathVariable("sessionId") String sessionId) {
        if (requestDTO.getQuantity() < 0) {
            // return new ResponseEntity<>("Negative weight", null, HttpStatus.BAD_REQUEST);
        }
        if (requestDTO.getQuantity() > serviceConfigDTO.getMaxWeight() || serviceStatusDTO.getCurrentWeight() + requestDTO.getQuantity() > serviceConfigDTO.getMaxWeight()) {
            // return new ResponseEntity<>("Too much", null, HttpStatus.INTERNAL_SERVER_ERROR);
            sendStore(requestDTO.getQuantity());
            TicketResponseDTO res = new TicketResponseDTO();
            res.setTicketNumber(-1);
            return res;
        }else {
            // quantity ok sending deposit
            int Ticket = sendStore(requestDTO.getQuantity());
            // UPDATE STATUS
            serviceStatusDTO.setCurrentWeight(serviceStatusDTO.getCurrentWeight() + requestDTO.getQuantity());
            TicketResponseDTO result = new TicketResponseDTO();
            result.setTicketNumber(Ticket);
            serviceStatusDTO.setCurrentTicket(Ticket);
            WeightDTO weightDTO = new WeightDTO(serviceStatusDTO.getCurrentWeight(), serviceConfigDTO.getMaxWeight());
            template.convertAndSendToUser(sessionId, "/user/queue/store-food", result);
            template.convertAndSend("/topic/updates", weightDTO);
            System.out.println(result);
            return result;
        }
    }

    @MessageMapping("/deposit/{sessionId}")
    @SendTo("/user/queue/deposit/{sessionId}")
    public TicketResponseDTO HandleDeposit(TicketResponseDTO depositDTO, @PathVariable("sessionId") String sessionId){
        if (depositDTO.getTicketNumber() <0){
            //return "invalid";
            depositDTO.setTicketNumber(-3);
            return depositDTO;
        }
        String res=sendDeposit(depositDTO.getTicketNumber());
        System.out.print("Risultato deposit "+res); // ritorna ticket valid/invalid
        //TODO: check on valid/invalid ticket
        int v = res.indexOf("(");
        int l = res.indexOf(",");
        String val = res.substring(v+1, l);
        //TicketResponseDTO result = new TicketResponseDTO();
        if(val.equals("ticketNotValid")){
            System.out.println(val);
            depositDTO.setTicketNumber( -2);
        }else if(val.equals("ticketExpired")){
            System.out.println(val);
            depositDTO.setTicketNumber( -1);
        }
        template.convertAndSendToUser(sessionId, "/usr/queue/deposit", depositDTO);
        //template.convertAndSend("/topic/updates", result);
        return depositDTO;
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        System.out.println("Session disconnected");
        System.out.println(event.getMessage());
        sessionList.remove(event.getSessionId());
    }
}
