package com.example.stephan.squashapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    public UserTrainingAdapter(Context context_of_screen, ArrayList<Training> trainings) {
        super(context_of_screen, R.layout.single_training, trainings);

        context = context_of_screen;
        trainingList = trainings;
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

        date.setText(item.get_date());
        info.setText(item.get_info());
        String timeText = item.get_start() + " until " + item.get_end();
        time.setText(timeText);
        cp.setText("Registered: " + item.get_current());
        mp.setText("Max players: " + item.get_max());

        String trainerName = item.get_trainer();


        if (!trainerName.isEmpty()) {
            trainer.setText("By: " + trainerName);
        } else {
            trainer.setText("");
        }


        // When clicked go to register
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.get_current() < item.get_max()){

                        // make layout
                        LayoutInflater li = LayoutInflater.from(context);
                        final View layout = li.inflate(R.layout.alertdialog_register, null);

                        String message = "Do you want to register for the training of "
                                + item.get_date() + " by " + item.get_trainer();
                        // make dialog
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle("Registration")
                                .setMessage(message)
                                .setCancelable(true)
                                .setView(layout)
                                .setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                TextView firstname =
                                                        (TextView) layout.findViewById(R.id.firstname);
                                                TextView lastname =
                                                        (TextView) layout.findViewById(R.id.lastname);

                                                String fn = firstname.getText().toString();
                                                String ln = lastname.getText().toString();

                                                if (fn.isEmpty() || ln.isEmpty()) {
                                                    Toast.makeText(
                                                            context,
                                                            "FAILED:\nYou must provide" +
                                                                    " a firstname and lastname",
                                                            Toast.LENGTH_SHORT).show();
                                                } else {
                                                    trainingList.get(position)
                                                            .register_player(fn, ln);
                                                    notifyDataSetChanged();
                                                    String text = "" + fn + " " + ln +
                                                            " you are registerd";
                                                    Toast.makeText(context, text, Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                                dialog.cancel();
                                            }
                                        });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    else{
                        Toast.makeText(context, "Full", Toast.LENGTH_SHORT).show();
                    }
                }
        });


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ArrayList<String> playerList = trainingList.get(position).get_players();

                // make layout
                LayoutInflater li = LayoutInflater.from(context);
                final View layout = li.inflate(R.layout.alertdialog_cancel_registration, null);

                ListView listView =
                        (ListView) layout.findViewById(R.id.cancelListView);
                CancelRegistrationAdapter adapter =
                        new CancelRegistrationAdapter(context, item.get_players(), item);
                listView.setAdapter(adapter);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context)
                    .setView(layout)
                    .setTitle("Cancel Registrion")
                    .setMessage("Long Click on a player to cancel registrion.");

                AlertDialog alert11 = builder1.create();
                alert11.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        notifyDataSetChanged();
                    }
                });
                alert11.show();
                return false;
            }
        });

        return view;
    }
}

