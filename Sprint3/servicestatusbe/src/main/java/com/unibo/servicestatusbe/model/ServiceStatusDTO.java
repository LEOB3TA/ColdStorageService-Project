package com.unibo.servicestatusbe.model;

import lombok.Data;

@Data
public class ServiceStatusDTO {
    private double currentWeight;
    private double maxWeight;
    private String status;
    private Position position;
    private int rejectedRequests;
}
