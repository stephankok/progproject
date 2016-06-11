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
 * Created by Stephan on 1-6-2016.
 *
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
        final TextView cp = (TextView) view.findViewById(R.id.currentPlayers);
        final TextView mp = (TextView) view.findViewById(R.id.maxPlayers);

        // The training
        final Training item = trainingList.get(position);

        // Set text.
        date.setText(item.getDate());
        info.setText(item.getShortInfo());
        String timeText = item.getStart() + " until " + item.getEnd();
        time.setText(timeText);
        cp.setText("Registered: " + item.getCurrentPlayers());
        mp.setText("Max players: " + item.getMaxPlayers());
        trainer.setText("By: " + item.getTrainer());

        return view;
    }

}

// ContextMenu?
