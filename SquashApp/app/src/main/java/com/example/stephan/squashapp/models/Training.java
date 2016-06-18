package com.example.stephan.squashapp.models;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Stephan on 1-6-2016.
 */
public class Training {

    private String trainer;
    private List<Integer> date;
    private String shortInfo;
    private List<Integer> start;
    private List<Integer> end;
    private Long maxPlayers;
    private Long currentPlayers;
    private Map<String,Object> registeredPlayers;

    public void Training(){
        // empty for firebase
    }

    public void newTraining(String trainer, List<Integer> date, String shortInfo, List<Integer> start, List<Integer> end,
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

    public List<Integer> getDate(){
        return this.date;
    }

    public String getShortInfo(){
        return this.shortInfo;
    }

    public List<Integer> getStart(){
        return this.start;
    }

    public List<Integer> getEnd(){
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

    public void changeDate(List<Integer> date){
        this.date = date;
    }

    public void changeStart(List<Integer> start){
        this.start = start;
    }

    public void changeEnd(List<Integer> end){
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

    @Exclude
    public String getFormattedDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.get(0), date.get(1), date.get(2));
        String dateFormatted =
                new SimpleDateFormat("EEE, MMM d, ''yy", Locale.US).format(calendar.getTime());
        return dateFormatted;
    }

    @Exclude
    public String getFormattedStart(){
        String startFormatted =
                String.valueOf(start.get(0)) + ":" +
                        String.format( Locale.US, "%02d", start.get(1));
        return startFormatted;
    }

    @Exclude
    public String getFormattedEnd(){
        String endFormatted =
                String.valueOf(end.get(0)) + ":" +
                        String.format( Locale.US, "%02d", end.get(1));
        return endFormatted;
    }

}
