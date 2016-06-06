package com.example.stephan.squashapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephan.squashapp.activities.R;
import com.example.stephan.squashapp.helpers.SwipeDetectorClass;
import com.example.stephan.squashapp.models.Training;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stephan on 1-6-2016.
 *
 */

public class UserTrainingAdapter extends ArrayAdapter<Training> {

    ArrayList<Training> trainingList;  // the items.
    Context context;
    SwipeDetectorClass swipeDetector;

    // firebase reference
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    /**
     * Initialize adapter
     */
    public UserTrainingAdapter(Context context, ArrayList<Training> trainingList) {
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

        // find Views.
        final TextView date = (TextView) view.findViewById(R.id.date);
        final TextView info = (TextView) view.findViewById(R.id.info);
        final TextView time = (TextView) view.findViewById(R.id.time);
        final TextView trainer = (TextView) view.findViewById(R.id.trainer);
        final TextView cp = (TextView) view.findViewById(R.id.currentPlayers);
        final TextView mp = (TextView) view.findViewById(R.id.maxPlayers);
        final ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteImage);


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


        view.setOnTouchListener(new SwipeDetectorClass(context) {

            @Override
            public void onClick() {
                super.onClick();
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
                Log.d("click", "click");
                if (item.get_current() < item.get_max()) {
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
                                                        .register_player(playerName);
                                                rootRef.child("trainingen")
                                                        .child(item.get_child())
                                                        .child("current").setValue(item.get_current());
                                                HashMap<String, Object> result = new HashMap<>();
                                                for (int i = 0; i < item.get_players().size(); i++) {
                                                    String registeredID = "player" + i;
                                                    Log.v("test", item.get_players().toString());
                                                    result.put(registeredID, item.get_players().get(i));
                                                }
                                                rootRef.child("trainingen")
                                                        .child(item.get_child())
                                                        .child("registered").updateChildren(result);


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

            @Override
            public void onDoubleClick() {
                super.onDoubleClick();
                Toast.makeText(context, "fs", Toast.LENGTH_SHORT).show();
                // your on onDoubleClick here
            }

            @Override
            public void onLongClick() {
                super.onLongClick();

                Toast.makeText(context, "Long clock", Toast.LENGTH_SHORT).show();
                Log.d("long", "click");
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
                // your on onLongClick here
            }

            @Override
            public void onSwipeUp() {
                super.onSwipeUp();
                Toast.makeText(context, "WAAAH", Toast.LENGTH_SHORT).show();
                // your swipe up here
            }

            @Override
            public void onSwipeDown() {
                super.onSwipeDown();
                Toast.makeText(context, "WAAAH", Toast.LENGTH_SHORT).show();
                // your swipe down here.
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                deleteButton.setVisibility(ImageButton.VISIBLE);

                Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();
                Log.d("swipe", "left");
                // your swipe left here.
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                deleteButton.setVisibility(ImageButton.INVISIBLE);
                Toast.makeText(context, "right", Toast.LENGTH_SHORT).show();
                Log.d("swipe", "right");
                // your swipe right here.
            }
        });


//        view.setOnTouchListener(new SwipeDetectorClass(context) {
//            public void onSwipeTop() {
//                Toast.makeText(context, "top", Toast.LENGTH_SHORT).show();
//                Log.v("swiped", "swiped");
//            }
//
//            public void onSwipeRight() {
//                Toast.makeText(context, "right", Toast.LENGTH_SHORT).show();
//                Log.v("swiped", "swiped");
//            }
//
//            public void onSwipeLeft() {
//                Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();
//                Log.v("swiped", "swiped");
//            }
//
//            public void onSwipeBottom() {
//                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show();
//                Log.v("swiped", "swiped");
//            }
//        });
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.v("clicked", "Clicked");
//                if (item.get_current() < item.get_max()) {
//
//                    // make layout
//                    LayoutInflater li = LayoutInflater.from(context);
//                    final View layout = li.inflate(R.layout.alertdialog_register, null);
//
//                    String message = "Do you want to register for the training of "
//                            + item.get_date() + " by " + item.get_trainer();
//                    // make dialog
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//                    builder1.setTitle("Registration")
//                            .setMessage(message)
//                            .setCancelable(true)
//                            .setView(layout)
//                            .setPositiveButton(
//                                    "Register",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            TextView name =
//                                                    (TextView) layout.findViewById(R.id.name);
//
//                                            String playerName = name.getText().toString();
//
//                                            if (playerName.isEmpty()) {
//                                                Toast.makeText(
//                                                        context,
//                                                        "FAILED:\nYou must provide a name",
//                                                        Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                trainingList.get(position)
//                                                        .register_player(playerName);
//                                                rootRef.child("trainingen")
//                                                        .child(item.get_child())
//                                                        .child("current").setValue(item.get_current());
//                                                HashMap<String, Object> result = new HashMap<>();
//                                                for (int i = 0; i < item.get_players().size(); i++) {
//                                                    String registeredID = "player" + i;
//                                                    Log.v("test", item.get_players().toString());
//                                                    result.put(registeredID, item.get_players().get(i));
//                                                }
//                                                rootRef.child("trainingen")
//                                                        .child(item.get_child())
//                                                        .child("registered").updateChildren(result);
//
//
//                                                notifyDataSetChanged();
//                                                String text = "" + playerName +
//                                                        " you are registerd";
//                                                Toast.makeText(context, text, Toast.LENGTH_SHORT)
//                                                        .show();
//                                            }
//                                            dialog.cancel();
//                                        }
//                                    });
//
//                    builder1.setNegativeButton(
//                            "Cancel",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                } else {
//                    Toast.makeText(context, "Full", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                ArrayList<String> playerList = trainingList.get(position).get_players();
//
//                // make layout
//                LayoutInflater li = LayoutInflater.from(context);
//                final View layout = li.inflate(R.layout.alertdialog_cancel_registration, null);
//
//                ListView listView =
//                        (ListView) layout.findViewById(R.id.cancelListView);
//                CancelRegistrationAdapter adapter =
//                        new CancelRegistrationAdapter(context, item.get_players(), item);
//                listView.setAdapter(adapter);
//
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(context)
//                    .setView(layout)
//                    .setTitle("Change Registration")
//                    .setCancelable(true)
//                        .setNeutralButton("done", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alert11 = builder1.create();
//                alert11.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        notifyDataSetChanged();
//                    }
//                });
//                alert11.show();
//                return false;
//            }
//        });

        return view;
    }

}

