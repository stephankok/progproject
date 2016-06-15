package com.example.stephan.squashapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stephan.squashapp.activities.R;
import com.example.stephan.squashapp.models.MegaChatMessage;

import java.util.ArrayList;

/**
 * Created by Stephan on 1-6-2016.
 *
 */

public class ChatAdapter extends ArrayAdapter<MegaChatMessage> {

    ArrayList<MegaChatMessage> messages;  // the items.
    Context context;

    /**
     * Initialize adapter
     */
    public ChatAdapter(Context context, ArrayList<MegaChatMessage> messages) {
        super(context, R.layout.single_message, messages);

        this.context = context;
        this.messages = messages;
    }

    public void push(MegaChatMessage message){
        this.messages.add(0,message);
        notifyDataSetChanged();
    }

    /**
     * Initialize View.
     */
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_message, parent, false);
        }

        TextView messageShow = (TextView) view.findViewById(R.id.messageShow);
        TextView userOfMessage = (TextView) view.findViewById(R.id.userOfMessage);

        MegaChatMessage message = messages.get(position);

        messageShow.setText(message.getMessage());
        userOfMessage.setText(message.getUser() + "\n" + message.getDate());


        return view;
    }

}

// ContextMenu?
