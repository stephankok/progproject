package com.example.stephan.squashapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Stephan on 1-6-2016.
 *
 */
public class CancelRegistrationAdapter extends ArrayAdapter<String>{

    ArrayList<String> playerList;  // the players.
    Training theTraining;
    Context context;

    /**
     * Initialize adapter
     */
    public CancelRegistrationAdapter(Context context_of_screen, ArrayList<String> players, Training training) {
        super(context_of_screen, R.layout.single_player, players);

        context = context_of_screen;
        playerList = players;
        theTraining = training;
    }

    /**
     * Initialize View.
     */
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_player, parent, false);
        }

        // find Views.
        final TextView player = (TextView) view.findViewById(R.id.playerName);
        final ImageView delete = (ImageView) view.findViewById(R.id.deletePlayer);

        // set text
        String text = "" + (position + 1) + ". " + playerList.get(position);
        player.setText(text);

        // add delete function
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theTraining.delete_player(position);
                notifyDataSetChanged();
            }
        });



        return view;
    }
}
