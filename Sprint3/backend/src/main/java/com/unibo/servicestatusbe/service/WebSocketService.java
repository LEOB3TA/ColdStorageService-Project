package com.unibo.servicestatusbe.service;

import com.unibo.servicestatusbe.model.ServiceConfigDTO;
import com.unibo.servicestatusbe.model.StoreFoodRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate template;
    final ServiceConfigDTO serviceConfigDTO;
    //private final NotificationService notificationService;

    @Autowired
    public WebSocketService(SimpMessagingTemplate template, ServiceConfigDTO serviceConfigDTO) {
        this.template = template;
        this.serviceConfigDTO = serviceConfigDTO;
    }

    public void sendNotificationToSpecificUser(final String id, final String message) {
        this.template.convertAndSendToUser(id, "/topic/private-messages", message);
    }

    public ResponseEntity<String> storeFood(String id, StoreFoodRequestDTO storeFoodRequestDTO) {
        if (storeFoodRequestDTO.getQuantity() > serviceConfigDTO.getMaxWeight()) {
            return new ResponseEntity<>("Too much", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (storeFoodRequestDTO.getQuantity() < 0) {
            return new ResponseEntity<>("Negative weight", null, HttpStatus.BAD_REQUEST);
        }
        sendNotificationToSpecificUser(id, "Food stored");
        return new ResponseEntity<>("Service Status successfully updated", null, HttpStatus.OK);
    }


}
