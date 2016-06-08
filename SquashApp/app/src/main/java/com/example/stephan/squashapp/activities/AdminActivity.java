package com.example.stephan.squashapp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.example.stephan.squashapp.adapters.EditTrainingAdapter;
import com.example.stephan.squashapp.helpers.FirebaseConnector;
import com.example.stephan.squashapp.models.Training;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminActivity extends AppCompatActivity implements FirebaseConnector.AsyncResponse{

    ListView mListView;
    EditTrainingAdapter myAdapter;
    ProgressDialog progressDialog;
    FirebaseConnector firebase =
            new FirebaseConnector(FirebaseDatabase.getInstance().getReference());
    DatePickerDialog.OnDateSetListener myDateListener;
    Calendar calendar;

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
        mListView = (ListView) findViewById(R.id.ListViewAdminTraining);
        myAdapter = new EditTrainingAdapter(this, new ArrayList<Training>());
        mListView.setAdapter(myAdapter);
       // getData();
        updateDatabase();

        calendar = Calendar.getInstance();

        myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                AddTraining2(arg1, arg2, arg3);
            }
        };
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
        Log.d("done", "done");
        myAdapter.notifyDataSetChanged();
        progressDialog.cancel();
    }


    protected Dialog onCreateDialog(int year, int month, int day) {
        return new DatePickerDialog(this, myDateListener, year, month, day);

    }
    /**
     * Add training
     *
     * Update change to firebase
     */
    public void AddTraining(){
        onCreateDialog(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void AddTraining2(final int year, final int month, final int day){
        // make layout
        LayoutInflater li = LayoutInflater.from(this);
        final View layout = li.inflate(R.layout.add_training, null);

        // get all items
        final EditText editDate = (EditText) layout.findViewById(R.id.date);
        editDate.setVisibility(View.GONE);
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

                                String date  = String.valueOf(year) + " " + String.valueOf(month) +
                                        " " + String.valueOf(day);
                                String info = editInfo.getText().toString();
                                String start = editStart.getText().toString();
                                String end = editEnd.getText().toString();
                                String trainer = editTrainer.getText().toString();
                                Long max = Long.parseLong(editMax.getText().toString());

                                Log.d("before","add");
                                Training training = new Training();
                                training.newTraining(trainer, date, info, start, end, max);

                                // add training online
                                firebase.updateSingleTraining(training, myAdapter.getAmountOfTrainingen());


                                myAdapter.add(training);
                                myAdapter.notifyDataSetChanged();
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
