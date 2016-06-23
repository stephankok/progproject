package nl.mprog.stephan.squashapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import nl.mprog.stephan.squashapp.activities.R;
import nl.mprog.stephan.squashapp.models.Training;

/**
 * Show to available trainings.
 */

public class UserTrainingAdapter extends ArrayAdapter<Training> {

    ArrayList<Training> trainingList;  // Trainings
    Context context;                   // Show information
    int offset;                        // Amount of hidden Trainings
    /**
     * Initialize adapter.
     */
    public UserTrainingAdapter(Context context, ArrayList<Training> trainingList) {
        super(context, R.layout.single_training, trainingList);
        this.context = context;
        this.trainingList = trainingList;
    }

    /**
     * Overwrite trainings list.
     */
    public void setTrainingList(ArrayList<Training> trainingList){
        this.trainingList.clear();
        this.offset = 0;

        for (Training training: trainingList) {
            // If the end of the training + 1/2 a day has passed, dont show to users
            if ((training.getDate() + 43200000L) > Calendar.getInstance().getTimeInMillis()){
                this.trainingList.add(training);
            }
            else{
                // Hidden Training
                this.offset += 1;
            }
        }

        // Done update adapter
        notifyDataSetChanged();
    }

    public int getOffset(){
        return this.offset;
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
        final TextView subjectOfTraining = (TextView) view.findViewById(R.id.subjectOfTraining);

        // Get Training
        Training item = trainingList.get(position);

        // Get information
        String timeText =  item.getFormattedStart() + " until " + item.getFormattedEnd();
        String currentPlayerText = "Participants: " + item.getCurrentPlayers();
        String maxPlayersText = "Max participants: " + item.getMaxPlayers();
        String trainerText = "By " + item.getTrainer();

        // Set text
        date.setText(item.getFormattedDate());
        info.setText(item.getShortInfo());
        time.setText(timeText);
        currentPlayers.setText(currentPlayerText);
        maxPlayers.setText(maxPlayersText);
        trainer.setText(trainerText);
        subjectOfTraining.setText(item.getSubjectOfTraining());

        return view;
    }
}
