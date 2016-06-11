package com.example.stephan.squashapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener{

    TextView userDetails;
    Button emailButton;
    Button usernameButton;
    Button passwordButton;
    Button deleteAccountButton;
    Button signOutButton;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        // Check if really logged in
        if(user == null){
            finish();
        }

        // get buttons
        emailButton = (Button) findViewById(R.id.changeEmailButton);
        usernameButton = (Button) findViewById(R.id.changeUsernameButton);
        passwordButton = (Button) findViewById(R.id.changePasswordButton);
        deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);
        signOutButton = (Button) findViewById(R.id.signOutButton);
        // set onClickListener
        emailButton.setOnClickListener(this);
        usernameButton.setOnClickListener(this);
        passwordButton.setOnClickListener(this);
        deleteAccountButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);

        // set details
        userDetails = (TextView) findViewById(R.id.userDetails);
        String userDetailsText = "Email: " + user.getEmail() +
                "\nUsername: " +user.getDisplayName();
        userDetails.setText(userDetailsText);

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

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.changeEmailButton:
                Toast.makeText(UserAccountActivity.this, "Not implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.changeUsernameButton:
                Toast.makeText(UserAccountActivity.this, "Not implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.changePasswordButton:
                Toast.makeText(UserAccountActivity.this, "Not implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteAccountButton:
                // make sure to cancel all subscibsions.
                Toast.makeText(UserAccountActivity.this, "Not implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signOutButton:
                auth.signOut();
                Toast.makeText(UserAccountActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }


    }
}
