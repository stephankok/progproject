package com.example.stephan.squashapp.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterNewUser extends AppCompatActivity {

    // views
    private EditText emailEdit;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText controlPasswordEdit;

    // check if minimum requirements are done
    private Integer resultCode;
    private Dialog dialog;

    // connect to firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        // Views
        emailEdit = (EditText) findViewById(R.id.emailEditText);
        usernameEdit = (EditText) findViewById(R.id.usernameEditText);
        passwordEdit = (EditText) findViewById(R.id.passwordEditText);
        controlPasswordEdit = (EditText) findViewById(R.id.controlPasswordEditText);

        // get current state
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Toast.makeText(RegisterNewUser.this, "Already logged in", Toast.LENGTH_SHORT).show();
            finish();
        }


        // add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(RegisterNewUser.this, "singed in", Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Toast.makeText(RegisterNewUser.this, "singed out", Toast.LENGTH_SHORT).show();
                }
                // [START_EXCLUDE]
            }
        };

        // make sure users has correct google play service
        resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getApplicationContext());
    }

    @Override
    public void onResume(){
        super.onResume();
        if (resultCode == ConnectionResult.SUCCESS) {
            Toast.makeText(RegisterNewUser.this, "Update succesfull" +
                    " you can login now.", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("result", resultCode.toString());
            Toast.makeText(RegisterNewUser.this, "Failed please update google play service",
                    Toast.LENGTH_SHORT).show();
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                //This dialog will help the user update to the latest GooglePlayServices
                dialog.show();
            }
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
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void createAccount(View v){
        // check if correct form
        if (!validateForm()) {
            return;
        }

        String email = emailEdit.getText().toString();
        final String userName = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        // make dialog.
        showProgressDialog();

        // Create user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                                    .Builder()
                                    .setDisplayName(userName)
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(RegisterNewUser.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterNewUser.this, "Succesfully created" +
                                            "user " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterNewUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // done
                        hideProgressDialog();
                    }
                });
    }



    /**
     * Show progress.
     */
    private void showProgressDialog(){
        dialog = new Dialog(this);
        dialog.setTitle("Connecting...");
        dialog.show();
    }

    /**
     * Hide progress.
     */
    private void hideProgressDialog(){
        dialog.cancel();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailEdit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Required.");
            valid = false;
        } else if(!email.contains("@")) {
            emailEdit.setError("It must be a valid email adress.");
            valid = false;
        }
        else{
            emailEdit.setError(null);
        }

        String username = usernameEdit.getText().toString();
        if(TextUtils.isEmpty(username)){
            usernameEdit.setError("Required.");
        }

        String password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("Required.");
            valid = false;
        }
        else if(password.length() < 6){
            passwordEdit.setError("Minimal 6 characters.");
            valid = false;
        }
        else {
            passwordEdit.setError(null);
        }

        String controlPassword = controlPasswordEdit.getText().toString();
        if(controlPassword.compareTo(password) != 0){
            controlPasswordEdit.setError("Passwords dont match");
            valid = false;
        }

        return valid;
    }

}
