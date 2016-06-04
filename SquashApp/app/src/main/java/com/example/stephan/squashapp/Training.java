package com.example.stephan.squashapp;

import java.util.ArrayList;

/**
 * Created by Stephan on 1-6-2016.
 */
public class Training {

    private String child;
    private String date;
    private String shortInfo;
    private String start;
    private String end;
    private String trainer;
    private Integer maxPlayers;
    private Integer currentPlayers;
    private ArrayList<String> registeredPlayers;


    public Training(String childRef, String dateOfTraining, String infoShort, String Start, String End, String Trainer, Integer maxplayers, Integer CurrentPlayers, ArrayList<String> registered){
        registeredPlayers = registered;
        date = dateOfTraining;
        shortInfo = infoShort;
        start = Start;
        end = End;
        trainer = Trainer;
        maxPlayers = maxplayers;
        currentPlayers = CurrentPlayers;
        child = childRef;
    }
    public ArrayList<String> get_players(){
        return registeredPlayers;
    }

    public String get_date(){
        return date;
    }

    public String get_child() { return child; }

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

    public void change_date(String dateOfTraining){
        date = dateOfTraining;
    }

    public void change_info_short(String infoShort){
        shortInfo = infoShort;
    }

    public void change_max(Integer max){
        maxPlayers = max;
    }

    public void change_start(String Start){
        start = Start;
    }

    public void change_end(String End){
        end = End;
    }

    public void delete_player(int position){
        registeredPlayers.remove(position);
        currentPlayers--;
    }

    public void change_player_name(String player, int pos){
        registeredPlayers.set(pos, player);
    }

    public void change_trainer(String Trainer){
        trainer = Trainer;
    }

    public void register_player(String firstname, String lastname){
        if(currentPlayers < maxPlayers){
            currentPlayers++;
            registeredPlayers.add(firstname + " " + lastname);
        }
    }
}
