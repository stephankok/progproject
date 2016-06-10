package com.example.stephan.squashapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephan.squashapp.activities.R;
import com.example.stephan.squashapp.helpers.FirebaseConnector;
import com.example.stephan.squashapp.helpers.SwipeDetectorClass;
import com.example.stephan.squashapp.models.Training;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Stephan on 1-6-2016.
 *
 */

public class UserTrainingAdapter extends ArrayAdapter<Training> {

    ArrayList<Training> trainingList;  // the items.
    Context context;

    // firebase reference
    FirebaseConnector firebase =
            new FirebaseConnector(FirebaseDatabase.getInstance().getReference());

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

        /**
         *  Set onclick & onlongclick
         */
        view.setOnTouchListener(new SwipeDetectorClass(context) {
            /**
             * On click listener
             *
             * Subscribe for training
             */
            @Override
            public void onClick() {
                super.onClick();
                Log.d("click", "click");
                if (item.getCurrentPlayers() < item.getMaxPlayers()) {
                    // make layout
                    LayoutInflater li = LayoutInflater.from(context);
                    final View layout = li.inflate(R.layout.alertdialog_register, null);

                    String message = "Do you want to register for the training of "
                            + item.getDate() + " by " + item.getTrainer();

                    // make dialog
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setTitle("Registration")
                            .setMessage(message)
                            .setCancelable(true)
                            .setView(layout)
                            .setPositiveButton(
                                    "Register",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            TextView name =
                                                    (TextView) layout.findViewById(R.id.name);

                                            String playerName = name.getText().toString();

                                            if (playerName.isEmpty()) {
                                                Toast.makeText(
                                                        context,
                                                        "FAILED:\nYou must provide a name",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                trainingList.get(position)
                                                        .registerPlayer(playerName);

                                                // register online
                                                firebase.updateRegisteredPlayers(item, position);

                                                notifyDataSetChanged();
                                                String text = "" + playerName +
                                                        " you are registerd";
                                                Toast.makeText(context, text, Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                            dialog.cancel();
                                        }
                                    });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    Toast.makeText(context, "Full", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * On longClick listener.
             *
             * Unsubscribe from training
             *
             * uses CancelRegistrationAdapter
             */
            @Override
            public void onLongClick() {
                super.onLongClick();
                Log.d("long", "click");
                // make layout
                LayoutInflater li = LayoutInflater.from(context);
                final View layout = li.inflate(R.layout.alertdialog_cancel_registration, null);

                ListView listView =
                        (ListView) layout.findViewById(R.id.cancelListView);
                CancelRegistrationAdapter adapter =
                        new CancelRegistrationAdapter(context, item, position);
                listView.setAdapter(adapter);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context)
                    .setView(layout)
                    .setTitle("Change Registration")
                    .setCancelable(true)
                        .setNeutralButton("done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        notifyDataSetChanged();
                    }
                });
                alert11.show();
            }

            @Override
            public void onSwipeLeft(){
                Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}

// ContextMenu?
