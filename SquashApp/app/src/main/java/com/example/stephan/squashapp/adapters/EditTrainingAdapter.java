package com.example.stephan.squashapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.stephan.squashapp.activities.R;
import com.example.stephan.squashapp.helpers.CalenderPicker;
import com.example.stephan.squashapp.helpers.FirebaseConnector;
import com.example.stephan.squashapp.models.Training;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Stephan on 4-6-2016.
 *
 */
public class EditTrainingAdapter extends ArrayAdapter<Training>{

    private ArrayList<Training> trainingList;  // the items.
    private Context context;
    private FirebaseConnector firebase = new FirebaseConnector();
    private CalenderPicker calendarPicker;

    /**
     * Initialize adapter
     */
    public EditTrainingAdapter(Context context, ArrayList<Training> trainingList) {
        super(context, R.layout.single_training, trainingList);
        this.context = context;
        this.trainingList = trainingList;
        calendarPicker = new CalenderPicker(context);
    }

    /**
     * Initialize View.
     */
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_training, parent, false);
        }

        // Find views.
        final ImageButton delete = (ImageButton) view.findViewById(R.id.deleteTraining);
        final TextView date = (TextView) view.findViewById(R.id.date);
        final TextView info = (TextView) view.findViewById(R.id.info);
        final TextView time = (TextView) view.findViewById(R.id.time);
        final TextView trainer = (TextView) view.findViewById(R.id.trainer);
        final TextView currentPlayers = (TextView) view.findViewById(R.id.currentPlayers);
        final TextView maxPlayers = (TextView) view.findViewById(R.id.maxPlayers);

        // In admin menu the delete button must be visable.
        delete.setVisibility(View.VISIBLE);

        // Get training.
        final Training item = trainingList.get(position);

        // Concatenate text
        String timeText =  item.getFormattedStart() + " until " + item.getFormattedEnd();
        String currentPlayersText = "Registered: " + item.getCurrentPlayers();
        String maxPlayersText = "Max players: " + item.getMaxPlayers();
        String trainerText = "By: " + item.getTrainer();

        // Update view.
        date.setText(item.getFormattedDate());
        info.setText(item.getShortInfo());
        time.setText(timeText);
        currentPlayers.setText(currentPlayersText);
        maxPlayers.setText(maxPlayersText);
        trainer.setText(trainerText);

        // Delete selected training.
        delete.setOnClickListener(deleteButtonClicked(position));

        // Show registered players.
        view.setOnClickListener(onClickShowUsers(position));

        // Edit the selected training.
        view.setOnLongClickListener(onLongClickEditTraining(position));

        return view;
    }

    /**
     * Overwrite the current trainingsList.
     */
    public void setTrainingList(ArrayList<Training> trainingList) {
        this.trainingList.clear();
        for (Training training : trainingList) {
            this.trainingList.add(training);
        }
        notifyDataSetChanged();
    }

    /**
     * Return tne size of trainingList.
     */
    public ArrayList<Training> getAll(){
        return this.trainingList;
    }

    private View.OnClickListener deleteButtonClicked(final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                trainingList.remove(position);
                                firebase.updateAllTrainings(trainingList,null);
                                notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete the training of " +
                        trainingList.get(position).getFormattedDate() + "?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        };
    }

    private View.OnClickListener onClickShowUsers(final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator items = trainingList.get(position).getRegisteredPlayers().values().iterator();
                ArrayList<String> players = new ArrayList<>();
                while(items.hasNext()){
                    Object element = items.next();
                    players.add(element.toString());
                }

                CharSequence[] cs = players.toArray(new CharSequence[players.size()]);

                AlertDialog.Builder builder = new AlertDialog.Builder(context)
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

    private View.OnLongClickListener onLongClickEditTraining(final int position){
        // Get item.
        final Training item = trainingList.get(position);

        // Return
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // make layout
                LayoutInflater li = LayoutInflater.from(context);
                final View layout = li.inflate(R.layout.alertdialog_add_training, null);

                // get all items
                final Button dateButton = (Button) layout.findViewById(R.id.dateButton);
                final Button startButton = (Button) layout.findViewById(R.id.startButton);
                final Button endButton = (Button) layout.findViewById(R.id.endButton);
                final EditText editTrainer = (EditText) layout.findViewById(R.id.trainer);
                final EditText editInfo = (EditText) layout.findViewById(R.id.info);
                final EditText editMax = (EditText) layout.findViewById(R.id.maxPlayers);

                final TextView datePicked = (TextView) layout.findViewById(R.id.datePicked);
                final TextView startPicked = (TextView) layout.findViewById(R.id.startPicked);
                final TextView endPicked = (TextView) layout.findViewById(R.id.endPicked);

                datePicked.setText(item.getFormattedDate());
                startPicked.setText(item.getFormattedStart());
                endPicked.setText(item.getFormattedEnd());

                dateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendarPicker.changeDate(item, datePicked);
                    }
                });

                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendarPicker.changeStart(item, startPicked);
                    }
                });

                endButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendarPicker.changeEnd(item, endPicked);
                    }
                });

                editInfo.setText(item.getShortInfo());
                editMax.setText(item.getMaxPlayers().toString());
                editTrainer.setText(item.getTrainer());

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("Edit Training")
                        .setCancelable(true)
                        .setView(layout)
                        .setPositiveButton(
                                "Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        item.changeShortInfo(editInfo.getText().toString());
                                        item.changeTrainer(editTrainer.getText().toString());
                                        item.changeMaxPlayers(
                                                Long.parseLong(editMax.getText().toString()));

                                        // TODO
                                        firebase.updateAllTrainings(trainingList, null);

                                        notifyDataSetChanged();
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
}
