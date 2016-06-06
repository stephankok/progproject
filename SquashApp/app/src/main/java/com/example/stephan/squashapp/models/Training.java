package com.example.stephan.squashapp.models;

import java.util.ArrayList;

/**
 * Created by Stephan on 1-6-2016.
 */
public class Training {

    private Integer childRef;
    private String date;
    private String shortInfo;
    private String start;
    private String end;
    private String trainer;
    private Integer maxPlayers;
    private Integer currentPlayers;
    private ArrayList<String> registeredPlayers;


    public Training(Integer childRef, String date, String shortInfo, String start, String end, String trainer, Integer maxPlayers, Integer currentPlayers, ArrayList<String> registeredPlayers){
        this.registeredPlayers = registeredPlayers;
        this.date = date;
        this.shortInfo = shortInfo;
        this.start = start;
        this.end = end;
        this.trainer = trainer;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = currentPlayers;
        this.childRef = childRef;
    }
    public ArrayList<String> get_players(){
        return registeredPlayers;
    }

    public String get_date(){
        return date;
    }

    public String get_child() { return String.valueOf(childRef); }

    public String get_info(){
        return shortInfo;
    }

    public String get_start(){
        return start;
    }

    public String get_end(){
        return end;
    }

    public String get_trainer(){
        return trainer;
    }

    public Integer get_max(){
        return maxPlayers;
    }

    public Integer get_current(){
        return currentPlayers;
    }

    public void change_date(String date){
        this.date = date;
    }

    public void change_info_short(String shortInfo){
        this.shortInfo = shortInfo;
    }

    public void change_max(Integer maxPlayers){
        this.maxPlayers = maxPlayers;
    }

    public void change_start(String start){
        this.start = start;
    }

    public void change_end(String end){
        this.end = end;
    }

    public void delete_player(int position){
        registeredPlayers.remove(position);
        currentPlayers--;
    }

    public void change_player_name(String player, int pos){
        registeredPlayers.set(pos, player);
    }

    public void change_trainer(String trainer){
        this.trainer = trainer;
    }

    public void register_player(String playerName){
        if(currentPlayers < maxPlayers){
            currentPlayers++;
            registeredPlayers.add(playerName);
        }
    }
}
