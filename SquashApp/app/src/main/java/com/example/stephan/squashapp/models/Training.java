package com.example.stephan.squashapp.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

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
    private Long childRef;
    private List<Object> registeredPlayers;

    @Exclude
    private ArrayList<String> registeredArray;


    public void Training(){
        // empty for firebase
    }

    public void newTraining(String trainer, String date, String shortInfo, String start, String end,
                         Long maxPlayers, Long childRef){
        this.trainer = trainer;
        this.date = date;
        this.shortInfo = shortInfo;
        this.start = start;
        this.end = end;
        this.maxPlayers = maxPlayers;
        this.childRef = childRef;
        this.currentPlayers = 0L;
        this.registeredPlayers = new ArrayList<>();
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
            this.registeredPlayers = new ArrayList<>();
        }
        return this.currentPlayers;
    }

    public Long getChildRef(){
        return this.childRef;
    }

    public List<Object> getRegisteredPlayers(){
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

    public void registerPlayer(String player){
        if(registeredPlayers == null){
            this.registeredPlayers = new ArrayList<>();
        }
        this.currentPlayers++;
        this.registeredPlayers.add(player);
    }

    public void deletePlayer(int pos){
        this.currentPlayers--;
        this.registeredPlayers.remove(pos);
    }

}
