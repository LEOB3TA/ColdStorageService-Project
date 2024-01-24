package com.unibo.servicestatusbe.dto;

import lombok.Data;

@Data
public class Message {
    final String content;

    public Message(String content) {
        this.content = content;
    }

}
