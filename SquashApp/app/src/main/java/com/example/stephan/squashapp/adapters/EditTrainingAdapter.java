package com.example.stephan.squashapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.stephan.squashapp.activities.R;
import com.example.stephan.squashapp.helpers.FirebaseConnector;
import com.example.stephan.squashapp.models.Training;

import java.util.ArrayList;

/**
 * Created by Stephan on 4-6-2016.
 *
 */
public class EditTrainingAdapter extends ArrayAdapter<Training>{

    private ArrayList<Training> trainingList;  // the items.
    private Context context;
    private FirebaseConnector firebase = new FirebaseConnector();

    /**
     * Initialize adapter
     */
    public EditTrainingAdapter(Context context, ArrayList<Training> trainingList) {
        super(context, R.layout.single_training, trainingList);
        this.context = context;
        this.trainingList = trainingList;
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
        final TextView subjectOfTraining = (TextView) view.findViewById(R.id.subjectOfTraining);

        // In admin menu the delete button must be visable.
        delete.setVisibility(View.VISIBLE);

        // Get training.
        final Training item = trainingList.get(position);

        // Concatenate text
        String timeText =  item.getFormattedStart() + " until " + item.getFormattedEnd();
        String currentPlayersText = "Participants: " + item.getCurrentPlayers();
        String maxPlayersText = "Max participants: " + item.getMaxPlayers();
        String trainerText = "By " + item.getTrainer();

        // Update view.
        date.setText(item.getFormattedDate());
        info.setText(item.getShortInfo());
        time.setText(timeText);
        currentPlayers.setText(currentPlayersText);
        maxPlayers.setText(maxPlayersText);
        trainer.setText(trainerText);
        subjectOfTraining.setText(item.getSubjectOfTraining());

        // Delete selected training.
        delete.setOnClickListener(deleteButtonClicked(position));

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
}
