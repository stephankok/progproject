package com.example.stephan.squashapp.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephan.squashapp.models.Training;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

    public void updateAllTrainings(ArrayList<Training> trainingList, final TextView showError){
        Log.d("update", "begin" + trainingList.toString());

        sortList(trainingList);
        rootRef.child("trainingen").setValue(trainingList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    showError.setVisibility(View.VISIBLE);
                    showError.setText(task.getException().getMessage());
                }
            }
        });
        Log.d("update", "end" +  trainingList.toString());
    }

    private void sortList(ArrayList<Training> trainingArrayList){
        Collections.sort(trainingArrayList, new Comparator<Training>() {
            @Override
            public int compare(Training lhs, Training rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });
    }

    public void renameUser(final FirebaseUser user, final String newUserName,
                           final TextView currentUser, final Context context){
        // Get data.
        rootRef.child("trainingen").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                            // Get the trainingen.
                            final Training newTraining = childSnapShot.getValue(Training.class);

                            // Check if the player is registered.
                            if(newTraining.getRegisteredPlayers() != null) {
                                if (newTraining.getRegisteredPlayers().containsKey(user.getUid())) {

                                    // Change the name on database ref.
                                    childSnapShot.child("registeredPlayers").child(user.getUid())
                                            .getRef()
                                            .setValue(newUserName)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (!task.isSuccessful()) {
                                                        currentUser.setError(
                                                                task.getException().getMessage());
                                                    }
                                                }
                                            });
                                }
                            }

                            // All names changed, rename user.
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(newUserName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Update on UI.
                                                Toast.makeText(context, "Changed user name to " +
                                                        newUserName, Toast.LENGTH_SHORT).show();
                                                currentUser.setText(newUserName);
                                            }
                                            else{
                                                currentUser.setError(task.getException().getMessage());
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        currentUser.setError(databaseError.getMessage());
                    }
                });
    }

    /**
     * Remove user from database. But first remove all registrations.
     * @param user: current user.
     * @param accountErrorShow: display error.
     */
    public void deleteUser(final FirebaseUser user, final TextView accountErrorShow){
        // Get data.
        rootRef.child("trainingen").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                            // Get the trainingen.
                            final Training newTraining = childSnapShot.getValue(Training.class);

                            // Check if the player is registered.
                            if(newTraining.getRegisteredPlayers().containsKey(user.getUid())){

                                // Remove it from database ref.
                                childSnapShot.child("registeredPlayers").child(user.getUid())
                                        .getRef()
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(!task.isSuccessful()){
                                            accountErrorShow.setText(task.getException().getMessage());
                                        }
                                    }
                                });
                            }

                            // All registrations are deleted, delete user.
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        accountErrorShow.setText(task.getException().getMessage());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        accountErrorShow.setText(databaseError.getMessage());
                    }
                });
    }
}