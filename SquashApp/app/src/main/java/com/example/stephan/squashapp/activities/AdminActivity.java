package com.example.stephan.squashapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephan.squashapp.adapters.EditTrainingAdapter;
import com.example.stephan.squashapp.helpers.CalenderPicker;
import com.example.stephan.squashapp.helpers.FirebaseConnector;
import com.example.stephan.squashapp.models.Training;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity implements FirebaseConnector.AsyncResponse,
        CalenderPicker.AsyncResponse{

    private TextView errorGetTrainingsAdmin;
    private EditTrainingAdapter myAdapter;
    private FirebaseConnector firebase = new FirebaseConnector();
    private SwipeRefreshLayout refresh;
    private CalenderPicker calendarPicker = new CalenderPicker(AdminActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Get views.
        ListView listView = (ListView) findViewById(R.id.ListViewAdminTraining);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refreshContainerAdmin);
        errorGetTrainingsAdmin = (TextView) findViewById(R.id.errorGetTrainingsAdmin);

        // Set adapter.
        myAdapter = new EditTrainingAdapter(this, new ArrayList<Training>());
        listView.setAdapter(myAdapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDatabase();
            }
        });
        // Get trainings.
        updateDatabase();

        // Set action to add button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarPicker.newTraining(AdminActivity.this);
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
        refresh.setRefreshing(true);
        errorGetTrainingsAdmin.setVisibility(View.GONE);

        myAdapter.clear();
        firebase.getTraingen(this, errorGetTrainingsAdmin);
    }

    /**
     * When data is loaded, this function will be called.
     *
     * Set new trainingslist to adapter
     */
    public void processFinish(ArrayList<Training> output){
        myAdapter.setTrainingList(output);
        refresh.setRefreshing(false);
    }


    public void newTrainingDateSelected(final Calendar date, final Calendar end){
        final Training training = new Training();

        // make layout
        final View layout = LayoutInflater.from(this).inflate(R.layout.alertdialog_add_training, null);

        // Remove items from layout.
        Button dateButton = (Button) layout.findViewById(R.id.dateButton);
        Button startButton = (Button) layout.findViewById(R.id.startButton);
        Button endButton = (Button) layout.findViewById(R.id.endButton);

        final TextView datePicked = (TextView) layout.findViewById(R.id.datePicked);
        final TextView startPicked = (TextView) layout.findViewById(R.id.startPicked);
        final TextView endPicked = (TextView) layout.findViewById(R.id.endPicked);

        String dateFormatted =
                new SimpleDateFormat("EEE, MMM d, ''yy", Locale.US).format(date.getTime());
        String startFormatted =
                String.valueOf(date.get(Calendar.HOUR_OF_DAY)) + ":" +
                        String.format( Locale.US, "%02d", date.get(Calendar.MINUTE));
        String endFormatted =
                String.valueOf(end.get(Calendar.HOUR_OF_DAY)) + ":" +
                        String.format( Locale.US, "%02d", end.get(Calendar.MINUTE));

        datePicked.setText(dateFormatted);
        startPicked.setText(startFormatted);
        endPicked.setText(endFormatted);


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPicker.changeDate(training, datePicked);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPicker.changeStart(training,startPicked);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPicker.changeEnd(training, endPicked);
            }
        });

        // Find view.
        final EditText editTrainer = (EditText) layout.findViewById(R.id.trainer);
        final EditText editInfo = (EditText) layout.findViewById(R.id.info);
        final EditText editMax = (EditText) layout.findViewById(R.id.maxPlayers);

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Add training")
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

                training.newTraining(trainer, date.getTimeInMillis(), info, end.getTimeInMillis(), max);
                myAdapter.add(training);

                // Add training online.
                Log.d("update", "call");
                firebase.updateAllTrainings(myAdapter.getAll(), errorGetTrainingsAdmin);
                updateDatabase();

                Toast.makeText(AdminActivity.this, "Training is added!", Toast.LENGTH_SHORT).show();

                // Cancel dialog
                alert11.cancel();
            }
        });
    }
}