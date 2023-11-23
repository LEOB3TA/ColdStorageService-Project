package com.unibo.servicestatusbe.model;

import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
public class ServiceConfigDTO {
    @Value("${container.max_weight}")
    private double maxWeight;
    //@Value("${container.current_weight}")
    //private double currentWeight;
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

    //toJson
    public String toJson() {
        fromIntArrayToJson(roomIndoor);
        return "{" +
                "\"maxWeight\":" + maxWeight +
                //", \"currentWeight\":" + currentWeight +
                ", \"rows\":" + rows +
                ", \"cols\":" + cols +
                ", \"roomHome\":" + fromIntArrayToJson(roomHome) +
                ", \"roomIndoor\":" + fromIntArrayToJson(roomIndoor) +
                ", \"roomColdRoom\":" + fromIntArrayToJson(roomColdroom) +
                "}";
    }

    public String fromIntArrayToJson(int[] array) {
        JSONArray jsonArray =  new JSONArray();
        for (int i = 0; i < array.length; i += 2) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("x", array[i]);
            if (i + 1 < array.length) {
                jsonObject.put("y", array[i + 1]);
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }
}
