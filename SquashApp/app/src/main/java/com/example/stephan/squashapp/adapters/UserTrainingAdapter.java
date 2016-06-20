package com.example.stephan.squashapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stephan.squashapp.activities.R;
import com.example.stephan.squashapp.models.Training;

import java.util.ArrayList;

/**
 * Show to available trainings.
 */

public class UserTrainingAdapter extends ArrayAdapter<Training> {

    ArrayList<Training> trainingList;  // the items.
    Context context;

    /**
     * Initialize adapter
     */
    public UserTrainingAdapter(Context context, ArrayList<Training> trainingList) {
        super(context, R.layout.single_training, trainingList);
        this.context = context;
        this.trainingList = trainingList;
    }

    /**
     * Overwrite trainingslist
     */
    public void setTrainingList(ArrayList<Training> trainingList){
        this.trainingList.clear();
        for(int i = 0; i < trainingList.size(); i++){
            this.trainingList.add(trainingList.get(i));
        }
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

        // Get views.
        final TextView date = (TextView) view.findViewById(R.id.date);
        final TextView info = (TextView) view.findViewById(R.id.info);
        final TextView time = (TextView) view.findViewById(R.id.time);
        final TextView trainer = (TextView) view.findViewById(R.id.trainer);
        final TextView currentPlayers = (TextView) view.findViewById(R.id.currentPlayers);
        final TextView maxPlayers = (TextView) view.findViewById(R.id.maxPlayers);

        // get Training
        Training item = trainingList.get(position);

        // get information
        String timeText =  item.getFormattedStart() + " until " + item.getFormattedEnd();
        String currentPlayerText = "Registered: " + item.getCurrentPlayers();
        String maxPlayersText = "Max players: " + item.getMaxPlayers();
        String trainerText = "By: " + item.getTrainer();

        // Set text.
        date.setText(item.getFormattedDate());
        info.setText(item.getShortInfo());
        time.setText(timeText);
        currentPlayers.setText(currentPlayerText);
        maxPlayers.setText(maxPlayersText);
        trainer.setText(trainerText);

        return view;
    }
}
