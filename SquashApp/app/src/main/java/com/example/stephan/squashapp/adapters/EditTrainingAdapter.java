package com.example.stephan.squashapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephan.squashapp.activities.R;
import com.example.stephan.squashapp.helpers.FirebaseConnector;
import com.example.stephan.squashapp.models.Training;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Stephan on 4-6-2016.
 */
public class EditTrainingAdapter extends ArrayAdapter<Training>{
    ArrayList<Training> trainingList;  // the items.
    Context context;

    FirebaseConnector firebase =
            new FirebaseConnector(FirebaseDatabase.getInstance().getReference());
    /**
     * Initialize adapter
     */
    public EditTrainingAdapter(Context context, ArrayList<Training> trainingList) {
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

    public Integer getAmountOfTrainingen(){
        return this.trainingList.size();
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

        // find Views.
        final ImageButton delete = (ImageButton) view.findViewById(R.id.deleteTraining);
        final TextView date = (TextView) view.findViewById(R.id.date);
        final TextView info = (TextView) view.findViewById(R.id.info);
        final TextView time = (TextView) view.findViewById(R.id.time);
        final TextView trainer = (TextView) view.findViewById(R.id.trainer);
        final TextView cp = (TextView) view.findViewById(R.id.currentPlayers);
        final TextView mp = (TextView) view.findViewById(R.id.maxPlayers);


        final Training item = trainingList.get(position);

        date.setText(item.getDate());
        info.setText(item.getShortInfo());
        String timeText = item.getStart() + " until " + item.getEnd();
        time.setText(timeText);
        cp.setText("Registered: " + item.getCurrentPlayers());
        mp.setText("Max players: " + item.getMaxPlayers());
        trainer.setText("By: " + item.getTrainer());

        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                trainingList.remove(position);
                                firebase.updateAllTrainingen(trainingList);
                                notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete the training of " + item.getDate() + "?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Todo: Show players registered", Toast.LENGTH_SHORT).show();
                Iterator items = item.getRegisteredPlayers().values().iterator();
                ArrayList<String> players = new ArrayList<String>();
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

        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // make layout
                LayoutInflater li = LayoutInflater.from(context);
                final View layout = li.inflate(R.layout.add_training, null);

                // get all items
                final EditText editDate = (EditText) layout.findViewById(R.id.date);
                final EditText editStart = (EditText) layout.findViewById(R.id.startTime);
                final EditText editEnd = (EditText) layout.findViewById(R.id.endTime);
                final EditText editTrainer = (EditText) layout.findViewById(R.id.trainer);
                final EditText editInfo = (EditText) layout.findViewById(R.id.info);
                final EditText editMax = (EditText) layout.findViewById(R.id.maxPlayers);

                editDate.setText(item.getDate());
                editStart.setText(item.getStart());
                editEnd.setText(item.getEnd());
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

                                        item.changeDate(editDate.getText().toString());
                                        item.changeStart(editStart.getText().toString());
                                        item.changeEnd(editEnd.getText().toString());
                                        item.changeShortInfo(editInfo.getText().toString());
                                        item.changeTrainer(editTrainer.getText().toString());
                                        item.changeMaxPlayers(
                                                Long.parseLong(editMax.getText().toString()));

                                        firebase.updateSingleTraining(item, position);

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
                return false;
            }
        });

        return view;
    }
}
