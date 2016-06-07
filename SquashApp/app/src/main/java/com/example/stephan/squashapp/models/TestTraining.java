package com.example.stephan.squashapp.models;

import java.util.Map;

/**
 * Created by Stephan on 7-6-2016.
 */
public class TestTraining {


    private String trainer;
    private String date;
    private String shortInfo;
    private String start;
    private String end;
    private Long maxPlayers;
    private Long currentPlayers;
    private Long childRef;
    private Map<String, String> registeredPlayers;

    public void TestTraining(){
        // empty for firebase
    }

    public String getTrainer(){
        return this.trainer;
    }

    public String getDate(){
        return this.date;
    }

    public String getShortInfo(){
        return this.shortInfo;
    }

    public String getStart(){
        return this.start;
    }

    public String getEnd(){
        return this.end;
    }

    public Long getMaxPlayers(){
        return this.maxPlayers;
    }

    public Long getCurrentPlayers(){
        return this.currentPlayers;
    }

    public Long getChildRef(){
        return this.childRef;
    }

    public Map<String, String> getRegisteredPlayers(){
        return this.registeredPlayers;
    }
}
