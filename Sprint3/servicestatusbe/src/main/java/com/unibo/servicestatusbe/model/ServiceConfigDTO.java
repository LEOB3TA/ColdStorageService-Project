package com.unibo.servicestatusbe.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Data
@Configuration
public class ServiceConfigDTO {
    @Value("${container.max_weight:0.0}")
    private double maxWeight;
    @Value("${container.current_weight:100.0}")
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

    /*
    public void setRoomHome(int[] roomHome) {
        this.roomHome.setX(roomHome[0]);
        this.roomHome.setY(roomHome[1]);
    }


    public void setRoomHome(String roomHome) {
        String[] roomHomeArray = roomHome.split(",");
        this.roomHome.setX(Integer.parseInt(roomHomeArray[0]));
        this.roomHome.setY(Integer.parseInt(roomHomeArray[1]));
    }

    public void setRoomIndoor(int[] roomIndoor) {
        Position[] positions = new Position[roomIndoor.length / 2];
        //this.roomIndoor = roomIndoor;
    }

     */

    //toJson
    public String toJson() {
        fromIntArrayToJson(roomIndoor);
        return "{" +
                "\"maxWeight\":" + maxWeight +
                ", \"currentWeight\":" + currentWeight +
                ", \"rows\":" + rows +
                ", \"cols\":" + cols +
                ", \"roomHome\":" + Arrays.stream(roomHome) +
                ", \"roomIndoor\":" + roomIndoor +
                ", \"roomColdroom\":" + roomColdroom +
                "}";
    }

    public void fromIntArrayToJson(int[] array) {
        int chunk = 2; // chunk size to divide
        int[][] result = new int[array.length / chunk][chunk];
        for(int i=0;i<array.length;i+=chunk){
            System.out.println(Arrays.toString(Arrays.copyOfRange(array, i, Math.min(array.length,i+chunk))));
            result[i / chunk] = Arrays.copyOfRange(array, i, Math.min(array.length,i+chunk));
        }

        /*
        Result
        "[
            "position": {
                "x": 1,
                "y": 2
            },
            "position": {
                "x": 3,
                "y": 4
            },
            "position": {
                "x": 5,
                "y": 6
            }
        ]"
         */
    }
}
