package com.example.stephan.squashapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

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

        // update user
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Toast.makeText(UserAccountActivity.this, "Not signed in.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
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
                user.updateEmail("user@example.com")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserAccountActivity.this, "Succesfully changed email", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(UserAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.changeUsernameButton:

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName("Jane Q. User")
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserAccountActivity.this, "Succesfully changed username", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(UserAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.changePasswordButton:
                String newPassword = "test1234";
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserAccountActivity.this, "Successfully changed password to test1234", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(UserAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                Toast.makeText(UserAccountActivity.this, "Not implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteAccountButton:
                // ask password again
                user.delete().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(UserAccountActivity.this, "Successfully removed your account!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(UserAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                // make sure to cancel all subscibsions.
                break;
            case R.id.signOutButton:
                auth.signOut();
                Toast.makeText(UserAccountActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }


    }
}
