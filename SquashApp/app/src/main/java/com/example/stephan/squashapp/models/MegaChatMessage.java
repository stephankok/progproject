package com.example.stephan.squashapp.models;

/**
 * Created by Stephan on 15-6-2016.
 */
public class MegaChatMessage {

    String userName;
    String message;
    String timeStamp;

    public MegaChatMessage(){
    }

    public void setValues(String userName, String message, String timeStamp){
        this.userName = userName;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getMessage(){
        return this.message;
    }

    public String getUser(){
        return this.userName;
    }

    public String getDate(){
        return this.timeStamp;
    }
}
