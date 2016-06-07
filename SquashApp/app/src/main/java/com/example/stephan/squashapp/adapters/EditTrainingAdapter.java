package com.example.stephan.squashapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.stephan.squashapp.activities.R;
import com.example.stephan.squashapp.models.Training;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stephan on 4-6-2016.
 */
public class EditTrainingAdapter extends ArrayAdapter<Training>{
    ArrayList<Training> trainingList;  // the items.
    Context context;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                                    HashMap<String, Object> result = new HashMap<>();
                                    result.put("trainer", editTrainer.getText().toString());
                                    result.put("date", editDate.getText().toString());
                                    result.put("start", editStart.getText().toString());
                                    result.put("maxPlayers", Integer.parseInt(editMax.getText().toString()));
                                    result.put("end", editEnd.getText().toString());
                                    result.put("shortInfo", editInfo.getText().toString());

                                    rootRef.child("trainingen").child(item.getChildRef().toString())
                                            .updateChildren(result);

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
            }

        });

        return view;
    }
}
