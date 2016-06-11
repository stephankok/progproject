package com.example.stephan.squashapp.activities;

/**
 * Created by Stephan on 9-6-2016.
 *
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserSignInActivity extends MainActivity implements
        View.OnClickListener {

    private EditText mEmailField;
    private Dialog dialog;
    private EditText mPasswordField;
    private Button signOut;
    private Button signIn;
    private Button register;
    private Integer resultCode;
    private Button forgotPasswordButton;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);

        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);

        // Buttons
        signIn = (Button) findViewById(R.id.email_sign_in_button);
        register = (Button) findViewById(R.id.email_create_account_button);
        forgotPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);

        // Set onClick listener.
        signIn.setOnClickListener(this);
        register.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        // Check if really logged in
        if(user != null){
            finish();
        }

        // add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // make sure users has correct google play service
        resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getApplicationContext());
    }

    @Override
    public void onResume(){
        super.onResume();
        if (resultCode == ConnectionResult.SUCCESS) {
            Toast.makeText(UserSignInActivity.this, "Update succesfull" +
                    " you can login now.", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("result", resultCode.toString());
            Toast.makeText(UserSignInActivity.this, "Failed please update google play service",
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

    private void signIn(String email, String password) {
        // check if correct form
        if (!validateForm()) {
            return;
        }

        // make dialog
        showProgressDialog();

        // sign in
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(UserSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(UserSignInActivity.this, "Signed in."
                                    , Toast.LENGTH_SHORT).show();
                        }

                        // done
                        hideProgressDialog();
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void forgotPassword(){


        AlertDialog.Builder dialog = new AlertDialog.Builder(UserSignInActivity.this)
                .setTitle("Forgot password").setMessage("Do you want mail?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String emailAddress = "stephan_handbal@hotmail.com";
                        mAuth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UserSignInActivity.this, "Email send",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(UserSignInActivity.this, "Failed " +
                                                    "to send email."
                                                    , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialog.create().show();
    }

    /**
     * Validate given input.
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else if(!email.contains("@")) {
            mEmailField.setError("It must be a valid email adress.");
            valid = false;
        }
        else{
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        }
        else if(password.length() < 6){
            mPasswordField.setError("Minimal 6 characters.");
            valid = false;
        }
        else {
            mPasswordField.setError(null);
        }
        return valid;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_create_account_button:
                Intent register = new Intent(UserSignInActivity.this, RegisterNewUser.class);
                startActivity(register);
                finish();
                break;
            case R.id.email_sign_in_button:
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.forgotPasswordButton:
                forgotPassword();
                break;
        }
    }
}