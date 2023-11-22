package com.unibo.servicestatusbe.controller;

import com.unibo.servicestatusbe.model.ServiceConfigDTO;
import com.unibo.servicestatusbe.model.ServiceStatusDTO;
import com.unibo.servicestatusbe.model.TextMessageDTO;
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
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@RestController
public class WebSocketController {
    final SimpMessagingTemplate template;
    final Environment env;
    final ServiceConfigDTO serviceConfigDTO;

    public WebSocketController(SimpMessagingTemplate template, Environment env, ServiceConfigDTO serviceConfigDTO) {
        this.template = template;
        this.env = env;
        this.serviceConfigDTO = serviceConfigDTO;
        System.out.println(serviceConfigDTO);
    }

    @SubscribeMapping("/topic/message")
    public TextMessageDTO subscribe() {
        System.out.println("Subscribed to /topic/message");

        TextMessageDTO message =  new TextMessageDTO("Subscribed to /topic/message");
        template.convertAndSend("/topic/message", message);
        return message;
    }

    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendMessage(@RequestBody ServiceStatusDTO serviceStatusDTO) {
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

}
