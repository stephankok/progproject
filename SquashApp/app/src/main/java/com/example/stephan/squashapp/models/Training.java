package com.example.stephan.squashapp.models;

import java.util.Map;

/**
 * Created by Stephan on 1-6-2016.
 */
public class Training {

    private String trainer;
    private String date;
    private String shortInfo;
    private String start;
    private String end;
    private Long maxPlayers;
    private Long currentPlayers;
    private Map<String,Object> registeredPlayers;

    public void Training(){
        // empty for firebase
    }

    public void newTraining(String trainer, String date, String shortInfo, String start, String end,
                         Long maxPlayers){
        this.trainer = trainer;
        this.date = date;
        this.shortInfo = shortInfo;
        this.start = start;
        this.end = end;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = 0L;
        this.registeredPlayers = new android.support.v4.util.ArrayMap<>();
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
        if(registeredPlayers == null){
            this.registeredPlayers = new android.support.v4.util.ArrayMap<>();
        }
        return this.currentPlayers;
    }

    public Map<String, Object> getRegisteredPlayers(){
        return this.registeredPlayers;
    }

    public void changeDate(String date){
        this.date = date;
    }

    public void changeStart(String start){
        this.start = start;
    }

    public void changeEnd(String end){
        this.end = end;
    }

    public void changeShortInfo(String shortInfo){
        this.shortInfo = shortInfo;
    }

    public void changeTrainer(String trainer){
        this.trainer = trainer;
    }

    public void changeMaxPlayers(Long maxPlayers){
        this.maxPlayers = maxPlayers;
    }

    public void registerPlayer(String player, String id){
        if(registeredPlayers == null){
            this.registeredPlayers = new android.support.v4.util.ArrayMap<>();
        }
        this.currentPlayers++;
        this.registeredPlayers.put(player, id);
    }

    public void deletePlayer(String id){
        this.currentPlayers--;
        this.registeredPlayers.remove(id);
    }

}
