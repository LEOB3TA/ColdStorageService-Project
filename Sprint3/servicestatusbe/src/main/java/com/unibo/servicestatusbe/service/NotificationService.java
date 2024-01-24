package com.unibo.servicestatusbe.service;

import com.unibo.servicestatusbe.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate template;

    @Autowired
    public NotificationService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendGlobalNotification() {
        ResponseMessage message = new ResponseMessage("Global Notification");
        this.template.convertAndSend("/topic/global-notifications", message);
    }

    public void sendPrivateNotification(final String id) {
        ResponseMessage message = new ResponseMessage("Private Notification");
        this.template.convertAndSendToUser(id, "/topic/private-notifications", message);
    }

}
