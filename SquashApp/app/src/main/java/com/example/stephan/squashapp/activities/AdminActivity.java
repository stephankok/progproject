package com.example.stephan.squashapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.stephan.squashapp.adapters.EditTrainingAdapter;
import com.example.stephan.squashapp.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    EditTrainingAdapter adapter;                // show trainings
    Integer amountOfTrainingen;

    // root to firebase
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Set action to add button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTraining();
            }
        });

        // add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // create listview
        ListView listview = (ListView) findViewById(R.id.ListViewTraining);
        adapter = new EditTrainingAdapter(this, new ArrayList<Training>());
        listview.setAdapter(adapter);

       // getData();
        getDatabase();
    }

    /**
     * Set back button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        // check witch item is pressed.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * get data from firebase
     */
    private void getDatabase(){
        Log.v("update", "updating");

        rootRef.child("total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amountOfTrainingen = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter.clear();
        rootRef.child("trainingen").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.v("hello",  "hoi");
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

                            Training new_training = new Training(child, date, info, start,
                                    end, trainer, max, current, registered);

                            adapter.add(new_training);
                            adapter.notifyDataSetChanged();
                            Log.v("childclass", "done");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("database", "error");
                        Log.v("getUser:onCancelled", databaseError.toString());
                    }
                });
    }

    /**
     * Add training
     *
     * Update change to firebase
     */
    public void AddTraining(){

        // make layout
        LayoutInflater li = LayoutInflater.from(this);
        final View layout = li.inflate(R.layout.add_training, null);

        // get all items
        final EditText editDate = (EditText) layout.findViewById(R.id.date);
        final EditText editStart = (EditText) layout.findViewById(R.id.startTime);
        final EditText editEnd = (EditText) layout.findViewById(R.id.endTime);
        final EditText editTrainer = (EditText) layout.findViewById(R.id.trainer);
        final EditText editInfo = (EditText) layout.findViewById(R.id.info);
        final EditText editMax = (EditText) layout.findViewById(R.id.maxPlayers);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Add Training")
                .setCancelable(true)
                .setView(layout)
                .setPositiveButton(
                    "Add",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            amountOfTrainingen++;
                            Training training =
                                new Training(amountOfTrainingen,
                                editDate.getText().toString(), editInfo.getText().toString(),
                                editStart.getText().toString(), editEnd.getText().toString(),
                                editTrainer.getText().toString(),
                                Integer.parseInt(editMax.getText().toString()), 0,
                                new ArrayList<String>());


                            rootRef.child("total").setValue(amountOfTrainingen);

                            HashMap<String, Object> result = new HashMap<>();
                            result.put("child", amountOfTrainingen);
                            result.put("by", editTrainer.getText().toString());
                            result.put("current", 0);
                            result.put("date", editDate.getText().toString());
                            result.put("start", editStart.getText().toString());
                            result.put("max", Integer.parseInt(editMax.getText().toString()));
                            result.put("end", editEnd.getText().toString());
                            result.put("info", editInfo.getText().toString());

                            rootRef.child("trainingen").child(String.valueOf(amountOfTrainingen))
                                    .updateChildren(result);


                            adapter.add(training);
                            adapter.notifyDataSetChanged();
                            dialog.cancel();

                        }
                    });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
