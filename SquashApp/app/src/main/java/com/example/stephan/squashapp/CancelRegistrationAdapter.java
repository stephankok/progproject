package com.example.stephan.squashapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stephan on 1-6-2016.
 *
 */
public class CancelRegistrationAdapter extends ArrayAdapter<String>{

    ArrayList<String> playerList;  // the players.
    Training theTraining;
    Context context;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

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
        final EditText player = (EditText) view.findViewById(R.id.playerName);
        final ImageView delete = (ImageView) view.findViewById(R.id.deletePlayer);

        // set text
        String text = playerList.get(position);
        player.setText(text);
        player.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                theTraining.change_player_name(player.getText().toString(), position);

                HashMap<String, Object> result = new HashMap<>();
                Log.v("size", String.valueOf(theTraining.get_players().size()));
                for (int i = 0; i < theTraining.get_players().size(); i++){
                    String registeredID = "player"+i;
                    Log.v("test", theTraining.get_players().toString());
                    result.put(registeredID, theTraining.get_players().get(i));
                }
                rootRef.child(theTraining.get_child()).child("registered").setValue(result);
            }
        });

        // add delete function
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theTraining.delete_player(position);

                rootRef.child(theTraining.get_child()).child("current").setValue(theTraining.get_current());

                HashMap<String, Object> result = new HashMap<>();
                Log.v("size", String.valueOf(theTraining.get_players().size()));
                for (int i = 0; i < theTraining.get_players().size(); i++){
                    String registeredID = "player"+i;
                    Log.v("test", theTraining.get_players().toString());
                    result.put(registeredID, theTraining.get_players().get(i));
                }
                rootRef.child(theTraining.get_child()).child("registered").setValue(result);
                notifyDataSetChanged();
            }
        });



        return view;
    }
}
