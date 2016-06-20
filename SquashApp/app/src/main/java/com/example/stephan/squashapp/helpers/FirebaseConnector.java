package com.example.stephan.squashapp.helpers;

import android.view.View;
import android.widget.TextView;

import com.example.stephan.squashapp.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Stephan on 6-6-2016.
 *
 */
public class FirebaseConnector {

    // Root to database
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    private AsyncResponse delegate = null;       // initialize to null;

    /**
     * Function in the activity to give the information.
     * ! So these functions must be present!
     */
    public interface AsyncResponse{
        void processFinish(ArrayList<Training> output);
    }

    public FirebaseConnector(){
        
    }

    public void getTraingen(final AsyncResponse delegate, final TextView errorTextView) {
        // get data
        final ArrayList<Training> newTrainingslist = new ArrayList<>();
        rootRef.child("trainingen").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                            // get the trainingen
                            Training newTraining = childSnapShot.getValue(Training.class);
                            newTrainingslist.add(newTraining);
                        }

                        delegate.processFinish(newTrainingslist);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        errorTextView.setText(databaseError.getMessage());
                        errorTextView.setVisibility(View.VISIBLE);
                        delegate.processFinish(newTrainingslist);
                    }
                });
    }

    public void updateRegisteredPlayers(Training training, int pos){
        rootRef.child("trainingen").child(String.valueOf(pos)).child("currentPlayers")
                .setValue(training.getCurrentPlayers());

        rootRef.child("trainingen").child(String.valueOf(pos)).child("registeredPlayers")
                .setValue(training.getRegisteredPlayers());

    }

    public void updateAllTrainings(ArrayList<Training> trainingList){
        sortList(trainingList);
        rootRef.child("trainingen").setValue(trainingList);
    }

    private void sortList(ArrayList<Training> trainingArrayList){
        Collections.sort(trainingArrayList, new Comparator<Training>() {
            @Override
            public int compare(Training lhs, Training rhs) {
                for(int i = 0; i < 3; i++){
                    if(!lhs.getDate().get(i).equals(rhs.getDate().get(i))){
                        return lhs.getDate().get(i) - rhs.getDate().get(i);
                    }
                }
                return 0;
            }
        });
    }

}