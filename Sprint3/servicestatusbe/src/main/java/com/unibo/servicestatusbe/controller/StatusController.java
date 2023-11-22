package com.unibo.servicestatusbe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class StatusController {
    @Value("${container.max_weight}")
    private double maxWeight;
    @Value("${container.current_weight}")
    private double currentWeight;
    @Value("${room.rows}")
    private int rows;
    @Value("${room.cols}")
    private int cols;
    @Value("${room.home}")
    private int[] roomHome;
    @Value("${room.indoor}")
    private int[] roomIndoor;
    @Value("${room.coldroom}")
    private int[] roomColdroom;
}
