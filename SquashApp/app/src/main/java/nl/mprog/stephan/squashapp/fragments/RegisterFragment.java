package nl.mprog.stephan.squashapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import nl.mprog.stephan.squashapp.activities.R;

/**
 * Created by Stephan on 13-6-2016.
 */
public class RegisterFragment extends Fragment{

    private EditText emailEdit;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText controlPasswordEdit;
    private TextView registerError;
    private ActionProcessButton registerNewUserButton;
    private Context context;

    // connect to firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    // newInstance constructor for creating fragment with arguments
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        context = view.getContext();

        emailEdit = (EditText) view.findViewById(R.id.emailEditText);
        usernameEdit = (EditText) view.findViewById(R.id.usernameEditText);
        passwordEdit = (EditText) view.findViewById(R.id.passwordEditText);
        controlPasswordEdit = (EditText) view.findViewById(R.id.controlPasswordEditText);
        registerError = (TextView) view.findViewById(R.id.registerError);

        // get special sumbit button
        registerNewUserButton = (ActionProcessButton) view.findViewById(R.id.registerNewUserButton);
        registerNewUserButton.setMode(ActionProcessButton.Mode.ENDLESS);
        registerNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUserButton.setProgress(0);
                registerError.setVisibility(View.GONE);
                createAccount();
            }
        });

        // get current state
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Toast.makeText(context, "Already logged in", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public void createAccount() {
        // check if correct form
        if (!validateForm()) {
            return;
        }

        registerNewUserButton.setProgress(1);

        final String email = emailEdit.getText().toString();
        final String userName = usernameEdit.getText().toString();
        final String password = passwordEdit.getText().toString();

        // Create user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                                    .Builder()
                                    .setDisplayName(userName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Welcome " +
                                                    user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                            registerNewUserButton.setProgress(100);
                                            getActivity().finish();
                                        }
                                    });
                        } else {
                            registerError.setText(task.getException().getMessage());
                            registerError.setVisibility(View.VISIBLE);
                            registerNewUserButton.setProgress(-1);
                        }
                    }
                });
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