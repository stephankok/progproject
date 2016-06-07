package com.example.stephan.squashapp.helpers;

import android.util.Log;

import com.example.stephan.squashapp.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Stephan on 6-6-2016.
 *
 */
public class FirebaseConnector {

    // firebase
    private DatabaseReference rootRef;

    private AsyncResponse delegate = null;       // initialize to null;

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

    public void getTraingen(final AsyncResponse delegate) {
        // get data
        final ArrayList<Training> newTrainingslist = new ArrayList<Training>();
        rootRef.child("trainingen").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.v("hello", "hoi");
                        for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                            // get the trainingen
                            Training newTraining = childSnapShot.getValue(Training.class);
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

    public void updateRegisteredPlayers(Training training, int pos){
        Log.d("pos", String.valueOf(pos));

        rootRef.child("trainingen").child(String.valueOf(pos + 1)).child("currentPlayers")
                .setValue(training.getCurrentPlayers());

        rootRef.child("trainingen").child(String.valueOf(pos + 1)).child("registeredPlayers")
                .setValue(training.getRegisteredPlayers());

    }

    /**
     * Add training to database
     */
    public void addTraining(Training training){
        rootRef.child("trainingen").child(training.getChildRef().toString()).setValue(training);
    }

}

// firebase maak class aan