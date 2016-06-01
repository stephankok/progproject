package com.example.stephan.squashapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements HttpRequestHelperGET.AsyncResponse{

    HttpRequestHelperGET request;               // Connect to internet
    ProgressDialog progressDialog;              // Wait for data
    UserTrainingAdapter adapter;                // show trainings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


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

        ListView listview = (ListView) findViewById(R.id.ListViewTraining);
        adapter = new UserTrainingAdapter(this, new ArrayList<Training>());
        listview.setAdapter(adapter);

        getData();
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
     * Update data.
     * Connect to internet.
     */
    public void getData(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Connecting to server...");
            progressDialog.show();
        }
        request = new HttpRequestHelperGET(this);
        request.execute();

    }

    /**
     * Callback from asynctask
     */
    public void processFinish(ArrayList<Training> trainingsList){
        // update trainings
        adapter.clear();
        for(int i = 0; i < trainingsList.size(); i++){
            adapter.add(trainingsList.get(i));
        }
        adapter.notifyDataSetChanged();

        // cancel dialog
        if (progressDialog != null){
            progressDialog.cancel();
        }
    }

    /**
     * Add training
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
                                Training training = new Training(
                                        editDate.getText().toString(), editInfo.getText().toString(),
                                        editStart.getText().toString(), editEnd.getText().toString(),
                                        editTrainer.getText().toString(),
                                        Integer.parseInt(editMax.getText().toString()), 0,
                                        new ArrayList<String>());
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
