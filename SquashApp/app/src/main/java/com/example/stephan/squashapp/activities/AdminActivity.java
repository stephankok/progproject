package com.example.stephan.squashapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.stephan.squashapp.adapters.EditTrainingAdapter;
import com.example.stephan.squashapp.helpers.CalenderPicker;
import com.example.stephan.squashapp.helpers.FirebaseConnector;
import com.example.stephan.squashapp.models.Training;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements FirebaseConnector.AsyncResponse,
        CalenderPicker.AsyncResponse{

    private EditTrainingAdapter myAdapter;
    private ProgressDialog progressDialog;
    private FirebaseConnector firebase = new FirebaseConnector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Create listview.
        ListView listView = (ListView) findViewById(R.id.ListViewAdminTraining);
        myAdapter = new EditTrainingAdapter(this, new ArrayList<Training>());
        listView.setAdapter(myAdapter);

        // Get trainings.
        updateDatabase();

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
        myAdapter.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        firebase.getTraingen(this);
    }

    /**
     * When data is loaded, this function will be called.
     *
     * Set new trainingslist to adapter
     */
    public void processFinish(ArrayList<Training> output){
        myAdapter.setTrainingList(output);
        progressDialog.cancel();
    }


    public void newTrainingDateSelected(final List<Integer> date, final List<Integer> start, final List<Integer> end){
        // make layout
        LayoutInflater li = LayoutInflater.from(this);
        final View layout = li.inflate(R.layout.add_training, null);

        // Remove items from layout.
        Button changeDate = (Button) layout.findViewById(R.id.date);
        Button changeStart = (Button) layout.findViewById(R.id.startTime);
        Button changeEnd = (Button) layout.findViewById(R.id.endTime);
        changeDate.setVisibility(View.GONE);
        changeStart.setVisibility(View.GONE);
        changeEnd.setVisibility(View.GONE);

        // Find view.
        final EditText editTrainer = (EditText) layout.findViewById(R.id.trainer);
        final EditText editInfo = (EditText) layout.findViewById(R.id.info);
        final EditText editMax = (EditText) layout.findViewById(R.id.maxPlayers);

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Add Training")
                .setCancelable(true)
                .setView(layout)
                .setPositiveButton("Add",null);

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert11 = builder1.create();
        alert11.show();
        alert11.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for correct input
                boolean falseInput = false;
                if (editInfo.getText().toString().isEmpty()) {
                    editInfo.setError("Required");
                    falseInput = true;
                }
                if (editTrainer.getText().toString().isEmpty()) {
                    editTrainer.setError("Required");
                    falseInput = true;
                }
                if (editMax.getText().toString().isEmpty()) {
                    editMax.setError("Required");
                    falseInput = true;
                }
                if(falseInput){
                    return;
                }

                // Passed the test.

                // Get input.
                String info = editInfo.getText().toString();
                String trainer = editTrainer.getText().toString();
                Long max = Long.parseLong(editMax.getText().toString());

                Training training = new Training();
                training.newTraining(trainer, date, info, start, end, max);

                // Add training online.
                firebase.updateSingleTraining(training, myAdapter.getAmountOfTrainings());
                firebase.getTraingen(AdminActivity.this);

                // Cancel dialog
                alert11.cancel();
            }
        });
    }
    /**
     * Add training
     *
     * Update change to firebase
     */
    private void AddTraining(){
        new CalenderPicker(this, this);

    }
}
