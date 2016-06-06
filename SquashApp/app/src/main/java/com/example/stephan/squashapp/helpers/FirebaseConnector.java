package com.example.stephan.squashapp.helpers;

import android.util.Log;

import com.example.stephan.squashapp.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stephan on 6-6-2016.
 *
 */
public class FirebaseConnector {

    // firebase
    private DatabaseReference rootRef;

    public AsyncResponse delegate = null;       // initialize to null;

    /**
     * Function in the activity to give the information.
     * ! So these functions must be present!
     */
    public interface AsyncResponse{
        void processFinish(ArrayList<Training> output);
    }

    public FirebaseConnector(DatabaseReference rootRef){
        this.rootRef = rootRef;
    }

    public void setResponse(AsyncResponse delegate){
        this.delegate = delegate;
    }

    public void getTraingen() {
        final ArrayList<Training> newTrainingslist = new ArrayList<Training>();
        rootRef.child("trainingen").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.v("hello", "hoi");
                        for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                            Log.v("childclass", "now");
                            Integer child = childSnapShot.child("child").getValue(Integer.class);
                            String trainer = childSnapShot.child("by").getValue(String.class);
                            String date = childSnapShot.child("date").getValue(String.class);
                            String info = childSnapShot.child("info").getValue(String.class);
                            String start = childSnapShot.child("start").getValue(String.class);
                            String end = childSnapShot.child("end").getValue(String.class);
                            Integer max = childSnapShot.child("max").getValue(Integer.class);
                            Integer current = childSnapShot.child("current").getValue(Integer.class);
                            Log.v("childclass", "verder");

                            ArrayList<String> registered = new ArrayList<>();
                            for (DataSnapshot childRegistered : childSnapShot.child("registered").getChildren()) {
                                String player = childRegistered.getValue(String.class);
                                Log.v("player", player);
                                registered.add(player);
                            }

                            Training newTraining = new Training(child, date, info, start,
                                    end, trainer, max, current, registered);


                            newTrainingslist.add(newTraining);
                        }

                        delegate.processFinish(newTrainingslist);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("database", "error");
                        Log.v("getUser:onCancelled", databaseError.toString());
                    }
                });
    }

    public void registerPlayer(Training training){
        rootRef.child("trainingen").child(training.get_child()).child("current")
                .setValue(training.get_current());

        HashMap<String, Object> result = new HashMap<>();
        for (int i = 0; i < training.get_players().size(); i++) {
            String registeredID = "player" + i;
            Log.v("test", training.get_players().toString());
            result.put(registeredID, training.get_players().get(i));
        }
        rootRef.child("trainingen").child(training.get_child()).child("registered")
                .updateChildren(result);

    }

    public void deRegisterPlayer(Training training){
        rootRef.child("trainingen").child(training.get_child()).child("current")
                .setValue(training.get_current());

        HashMap<String, Object> result = new HashMap<>();
        for (int i = 0; i < training.get_players().size(); i++) {
            String registeredID = "player" + i;
            result.put(registeredID, training.get_players().get(i));
        }
        rootRef.child("trainingen").child(training.get_child()).child("registered")
                .setValue(result);
    }

    /**
     * Add training to database
     */
    public void addTraining(Integer number, String date, String info, String start, String end,
                            String trainer, Integer max){
        // add new trainings node
        rootRef.child("total").setValue(number);

        // all trainings items
        HashMap<String, Object> result = new HashMap<>();
        result.put("child", number);
        result.put("by", trainer);
        result.put("current", 0);
        result.put("date", date);
        result.put("start", start);
        result.put("max", max);
        result.put("end", end);
        result.put("info", info);

        // add training to node
        rootRef.child("trainingen").child(String.valueOf(number)).updateChildren(result);
    }
}
