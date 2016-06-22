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
import android.widget.AdapterView;
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
import java.util.Iterator;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity implements FirebaseConnector.AsyncResponse,
        CalenderPicker.AsyncResponse{

    private TextView errorGetTrainingsAdmin;
    private EditTrainingAdapter myAdapter;
    private FirebaseConnector firebase = new FirebaseConnector();
    private SwipeRefreshLayout refresh;
    private CalenderPicker calendarPicker = new CalenderPicker(AdminActivity.this);
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Get views
        listView = (ListView) findViewById(R.id.ListViewAdminTraining);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refreshContainerAdmin);
        errorGetTrainingsAdmin = (TextView) findViewById(R.id.errorGetTrainingsAdmin);

        // Set adapter
        myAdapter = new EditTrainingAdapter(this, new ArrayList<Training>());
        listView.setAdapter(myAdapter);

        // Show registered players
        listView.setOnItemClickListener(onClickShowUsers());

        // Edit training item
        listView.setOnItemLongClickListener(onLongClickEditTraining());

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDatabase();
            }
        });

        // Get trainings
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
        if(!refresh.isRefreshing()) {
            refresh.post(new Runnable() {
                @Override
                public void run() {
                    refresh.setRefreshing(true);
                }
            });
        }
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
        training.changeDate(date.getTimeInMillis());
        training.changeEnd(end.getTimeInMillis());

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
        final EditText subjectOfTraining = (EditText) layout.findViewById(R.id.subjectOfTraining);

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
                if(falseInput(editInfo, editTrainer,
                        editMax, subjectOfTraining)){
                    return;
                }

                // Passed the test.

                // Get input.
                String info = editInfo.getText().toString();
                String trainer = editTrainer.getText().toString();
                Long max = Long.parseLong(editMax.getText().toString());
                String subject = subjectOfTraining.getText().toString();

                training.newTraining(trainer, date.getTimeInMillis(), info, end.getTimeInMillis(), max, subject);
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

    /**
     * Create a dialog to show registered players.
     * @return
     */
    private AdapterView.OnItemClickListener onClickShowUsers(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Iterator items = myAdapter.getItem(position).getRegisteredPlayers().values().iterator();
                ArrayList<String> players = new ArrayList<>();
                while(items.hasNext()){
                    Object element = items.next();
                    players.add(element.toString());
                }

                CharSequence[] cs = players.toArray(new CharSequence[players.size()]);

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this)
                        .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setItems(cs, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setTitle("Registered players");
                builder.show();
            }
        };
    }

    /**
     * Give option to edit available trainings.
     * @return
     */
    private AdapterView.OnItemLongClickListener onLongClickEditTraining(){
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get item
                final Training item = myAdapter.getItem(position);

                // Create a temp training, to only save changes when "Update" is pressed
                final Training tempTraining = new Training();
                tempTraining.newTraining(item.getTrainer(), item.getDate(), item.getShortInfo(),
                        item.getEnd(), item.getMaxPlayers(), item.getSubjectOfTraining());

                // make layout
                LayoutInflater li = LayoutInflater.from(AdminActivity.this);
                final View layout = li.inflate(R.layout.alertdialog_add_training, null);

                // get all items
                final Button dateButton = (Button) layout.findViewById(R.id.dateButton);
                final Button startButton = (Button) layout.findViewById(R.id.startButton);
                final Button endButton = (Button) layout.findViewById(R.id.endButton);
                final EditText editTrainer = (EditText) layout.findViewById(R.id.trainer);
                final EditText editInfo = (EditText) layout.findViewById(R.id.info);
                final EditText editMax = (EditText) layout.findViewById(R.id.maxPlayers);
                final EditText subjectOfTraining =
                        (EditText) layout.findViewById(R.id.subjectOfTraining);
                final TextView datePicked = (TextView) layout.findViewById(R.id.datePicked);
                final TextView startPicked = (TextView) layout.findViewById(R.id.startPicked);
                final TextView endPicked = (TextView) layout.findViewById(R.id.endPicked);


                // Set date
                datePicked.setText(tempTraining.getFormattedDate());
                startPicked.setText(tempTraining.getFormattedStart());
                endPicked.setText(tempTraining.getFormattedEnd());

                // Change date when button is pressed
                dateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendarPicker.changeDate(tempTraining, datePicked);
                    }
                });

                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendarPicker.changeStart(tempTraining, startPicked);
                    }
                });

                endButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendarPicker.changeEnd(tempTraining, endPicked);
                    }
                });

                // Set information
                editInfo.setText(item.getShortInfo());
                editMax.setText(item.getMaxPlayers().toString());
                editTrainer.setText(item.getTrainer());
                subjectOfTraining.setText(item.getSubjectOfTraining());

                // Create alert dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminActivity.this);
                builder1.setTitle("Edit Training")
                        .setCancelable(true)
                        .setView(layout)
                        .setPositiveButton(
                                "Update",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if(falseInput(editInfo, editTrainer,
                                                editMax, subjectOfTraining)){
                                            return;
                                        }

                                        // Update training.
                                        item.changeShortInfo(editInfo.getText().toString());
                                        item.changeTrainer(editTrainer.getText().toString());
                                        item.changeMaxPlayers(
                                                Long.parseLong(editMax.getText().toString()));
                                        item.changeDate(tempTraining.getDate());
                                        item.changeEnd(tempTraining.getEnd());
                                        item.changeSubjectOfTraining(
                                                subjectOfTraining.getText().toString());

                                        // Update firebase
                                        firebase.updateAllTrainings(myAdapter.getAll(),
                                                errorGetTrainingsAdmin);

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

                return true;
            }
        };
    }

    /**
     * Check user input.
     * @param editInfo
     * @param editTrainer
     * @param editMax
     * @param subjectOfTraining
     * @return
     */
    private boolean falseInput(EditText editInfo ,EditText editTrainer,
                               EditText editMax, EditText subjectOfTraining){
        boolean inputIsFalse = false;
        if (editInfo.getText().toString().isEmpty()) {
            editInfo.setError("Required");
            inputIsFalse = true;
        }
        if (editTrainer.getText().toString().isEmpty()) {
            editTrainer.setError("Required");
            inputIsFalse = true;
        }
        if (editMax.getText().toString().isEmpty()) {
            editMax.setError("Required");
            inputIsFalse = true;
        }
        if(subjectOfTraining.getText().toString().isEmpty()){
            subjectOfTraining.setError("Required");
            inputIsFalse =  true;
        }

        return inputIsFalse;
    }
}