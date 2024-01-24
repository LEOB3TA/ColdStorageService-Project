package com.unibo.servicestatusbe.dto;

import lombok.Data;

@Data
public class ResponseMessage {
    private String content;

    public ResponseMessage(String content) {
        this.content = content;
    }
}
