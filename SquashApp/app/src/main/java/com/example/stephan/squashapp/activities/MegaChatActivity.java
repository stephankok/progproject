package com.example.stephan.squashapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stephan.squashapp.adapters.ChatAdapter;
import com.example.stephan.squashapp.models.MegaChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MegaChatActivity extends AppCompatActivity implements View.OnClickListener {

    Button sendMessageButton;
    EditText messageEditText;
    ChatAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference()
            .child("MessageBoard").child("MainBoard");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mega_chat);

        if(user == null){
            Toast.makeText(MegaChatActivity.this, "You must be logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        getMessages();
        setNewMessageListener();

        messageEditText = (EditText) findViewById(R.id.messageEditText);
        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);

        ListView megaChatListView = (ListView) findViewById(R.id.megaChatListView);
        adapter = new ChatAdapter(this, new ArrayList<MegaChatMessage>());
        megaChatListView.setAdapter(adapter);

        sendMessageButton.setOnClickListener(this);

        // add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Set back button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        // check witch item is pressed.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addMessage(){
        if(messageEditText.getText().toString().isEmpty()){
            messageEditText.setError("Required");
            return;
        }

        MegaChatMessage message = new MegaChatMessage();
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String timeFormatted = format.format(time);
        message.setValues(user.getDisplayName(), messageEditText.getText().toString(), timeFormatted);
        addMessage(message);

        messageEditText.setText("");
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.sendMessageButton:
                addMessage();
                break;
            default:
                break;
        }
    }

    public void addMessage(MegaChatMessage message){
        rootRef.push().setValue(message);
    }

    public void getMessages() {
        rootRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                            // get the trainingen
                            MegaChatMessage message = childSnapShot.getValue(MegaChatMessage.class);
                            adapter.push(message);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("database", "error");
                        Log.v("getUser:onCancelled", databaseError.toString());
                    }
                });
    }

    public void setNewMessageListener(){
        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MegaChatMessage message = dataSnapshot.getValue(MegaChatMessage.class);
                adapter.push(message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
