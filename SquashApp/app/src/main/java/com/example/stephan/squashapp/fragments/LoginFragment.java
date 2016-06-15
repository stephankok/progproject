package com.example.stephan.squashapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.stephan.squashapp.activities.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Stephan on 13-6-2016.
 */
public class LoginFragment extends Fragment {

    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView loginError;
    private ActionProcessButton signInButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // newInstance constructor for creating fragment with arguments
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mEmailField = (EditText) view.findViewById(R.id.field_email);
        mPasswordField = (EditText) view.findViewById(R.id.field_password);
        loginError = (TextView) view.findViewById(R.id.loginError);

        // Buttons
        signInButton = (ActionProcessButton) view.findViewById(R.id.email_sign_in_button);

        // set special mode
        signInButton.setMode(ActionProcessButton.Mode.ENDLESS);

        // Set onClick listener.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginError.setVisibility(View.GONE);
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();

        // Check if really logged in
        if(user != null){
            //finish();
        }

        return view;
    }

    private void signIn(String email, String password) {
        // check if correct form
        if (!validateForm()) {
            return;
        }

        signInButton.setProgress(1);
        // sign in
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            loginError.setText(task.getException().getMessage());
                            signInButton.setProgress(-1);
                        }
                        else{
                            signInButton.setProgress(100);
                            getActivity().finish();
                            //finish();
                        }

                        // done

                    }
                });
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
}