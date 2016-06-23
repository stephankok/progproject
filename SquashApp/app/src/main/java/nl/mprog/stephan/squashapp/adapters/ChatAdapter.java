package nl.mprog.stephan.squashapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nl.mprog.stephan.squashapp.activities.R;
import nl.mprog.stephan.squashapp.models.MegaChatMessage;

;

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

        // Time
        Date time = Calendar.getInstance().getTime();
        time.setTime(message.getTimeStamp());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        String messageInfo = message.getUserName() + "\n" + format.format(time);

        messageShow.setText(message.getMessage());
        userOfMessage.setText(messageInfo);


        return view;
    }

}

// ContextMenu?
