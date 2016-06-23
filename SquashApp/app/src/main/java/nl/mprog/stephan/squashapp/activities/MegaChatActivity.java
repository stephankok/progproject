package nl.mprog.stephan.squashapp.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import nl.mprog.stephan.squashapp.adapters.MegaChatAdapter;
import nl.mprog.stephan.squashapp.models.MegaChatMessage;

/**
 *
 */
public class MegaChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText messageEditText;
    private MegaChatAdapter adapter;
    private SwipeRefreshLayout refreshContainerChat;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference()
            .child("MessageBoard").child("MainBoard");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mega_chat);

        // Check if really logged in
        if(user == null){
            Toast.makeText(MegaChatActivity.this, "You must be logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Get view
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        Button sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        ListView megaChatListView = (ListView) findViewById(R.id.megaChatListView);

        // Set adapter
        adapter = new MegaChatAdapter(this, new ArrayList<MegaChatMessage>());

        // Set adapter and OnClickListener
        try{
            megaChatListView.setAdapter(adapter);
            sendMessageButton.setOnClickListener(this);
        }
        catch (NullPointerException e){
            Toast.makeText(MegaChatActivity.this, "Please restart app", Toast.LENGTH_SHORT).show();
        }

        // add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set an on new message added listener that will update the adapter
        setNewMessageListener();
    }

    /**
     * Set back button.
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

    /**
     * Add new message.
     */
    private void addMessage(){
        if(messageEditText.getText().toString().isEmpty()){
            messageEditText.setError("Required");
            return;
        }

        // Create new message
        MegaChatMessage message = new MegaChatMessage();

        // Add message and time
        Long timeStamp = Calendar.getInstance().getTimeInMillis();
        message.setValues(user.getDisplayName(), messageEditText.getText().toString(), timeStamp);

        // Add message
        addMessage(message);
        messageEditText.setText("");
    }

    /**
     * Set on click listener.
     */
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

    /**
     * Add message to firebase.
     */
    private void addMessage(MegaChatMessage message){
        rootRef.push().setValue(message);
    }

    /**
     * When a message is added on database, show to user.
     */
    private void setNewMessageListener(){
        // Show 100 last messages
        rootRef.limitToLast(100).addChildEventListener(new ChildEventListener() {
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
