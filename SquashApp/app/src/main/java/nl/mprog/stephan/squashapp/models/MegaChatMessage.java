package nl.mprog.stephan.squashapp.models;

/**
 * A message for the mega chat.
 */
public class MegaChatMessage {

    String userName;       // Poster of message
    String message;        // Message
    Long timeStamp;        // Time message is posted

    public MegaChatMessage(){
        // Empty for Firebase
    }

    /**
     * Initialize message.
     */
    public void setValues(String userName, String message, Long timeStamp){
        this.userName = userName;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getMessage(){
        return this.message;
    }

    public String getUserName(){
        return this.userName;
    }

    public Long getTimeStamp(){
        return this.timeStamp;
    }
}
