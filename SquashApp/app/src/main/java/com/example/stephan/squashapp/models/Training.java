package com.example.stephan.squashapp.models;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Stephan on 1-6-2016.
 */
public class Training {

    private String trainer;
    private Long date;
    private String shortInfo;
    private Long end;
    private Long maxPlayers;
    private Long currentPlayers;
    private Map<String,Object> registeredPlayers;

    public void Training(){
        // empty for firebase
    }

    public void newTraining(String trainer, Long date, String shortInfo, Long end,
                         Long maxPlayers){
        this.trainer = trainer;
        this.date = date;
        this.shortInfo = shortInfo;
        this.end = end;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = 0L;
        this.registeredPlayers = new android.support.v4.util.ArrayMap<>();
    }

    public String getTrainer(){
        return this.trainer;
    }

    public Long getDate(){
        return this.date;
    }

    public String getShortInfo(){
        return this.shortInfo;
    }

    public Long getEnd(){
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

    public void changeDate(Long date){
        this.date = date;
    }

    public void changeEnd(Long end){
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
        calendar.setTimeInMillis(date);

        String dateFormatted =
                new SimpleDateFormat("EEE, MMM d, ''yy", Locale.US).format(calendar.getTime());
        return dateFormatted;
    }

    @Exclude
    public String getFormattedStart(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        String startFormatted =
                String.valueOf(calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        String.format(Locale.US, "%02d", calendar.get(Calendar.MINUTE)));
        return startFormatted;
    }

    @Exclude
    public String getFormattedEnd(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(end);

        String endFormatted =
                String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                        String.format( Locale.US, "%02d", calendar.get(Calendar.MINUTE));
        return endFormatted;
    }

}
