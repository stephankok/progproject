package com.example.stephan.squashapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.example.stephan.squashapp.helpers.FirebaseConnector;
import com.example.stephan.squashapp.models.Training;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements FirebaseConnector.AsyncResponse{

    EditTrainingAdapter adapter;                // show trainings
    Integer amountOfTrainingen;
    ProgressDialog progressDialog;
    FirebaseConnector firebase =
            new FirebaseConnector(FirebaseDatabase.getInstance().getReference());

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

        // make sure it when getting data it will respond to this activity
        firebase.setResponse(this);
       // getData();
        updateDatabase();
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
    * Call class that will call firebase to get data
    */
    private void updateDatabase(){
        adapter.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        firebase.getTraingen();
    }

    /**
     * When data is loaded, this function will be called.
     *
     * Set new trainingslist to adapter
     */
    public void processFinish(ArrayList<Training> output){
        adapter.setTrainingList(output);
        Log.d("done", "done");
        adapter.notifyDataSetChanged();
        progressDialog.cancel();
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
                            // get input

                            String date  = editDate.getText().toString();
                            String info = editInfo.getText().toString();
                            String start = editStart.getText().toString();
                            String end = editEnd.getText().toString();
                            String trainer = editTrainer.getText().toString();
                            Integer max = Integer.parseInt(editMax.getText().toString());

                            amountOfTrainingen = adapter.getAmountOfTrainingen() + 1;

                            Log.d("before","add");
                            Training training = new Training(amountOfTrainingen, date, info, start,
                                    end, trainer, max, 0, new ArrayList<String>());

                            // add training online
                            firebase.addTraining(amountOfTrainingen, date, info, start, end,
                                    trainer, max);


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
