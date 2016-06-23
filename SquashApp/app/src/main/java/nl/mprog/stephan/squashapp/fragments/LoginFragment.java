package nl.mprog.stephan.squashapp.fragments;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import nl.mprog.stephan.squashapp.activities.R;

/**
 * Created by Stephan on 13-6-2016.
 *
 */
public class LoginFragment extends Fragment {

    private final int SENDING = 1;      // Button sending code
    private final int FINISHED = 100;   // Button finished code
    private final int ERROR = -1;       // Button error code

    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView loginError;
    private ActionProcessButton signInButton;
    private FirebaseAuth mAuth;

    /**
     *  newInstance constructor for creating fragment with arguments.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get root to the connection with Firebase
        mAuth = FirebaseAuth.getInstance();

        // Check if already logged on
        if(mAuth.getCurrentUser() != null){
            getActivity().finish();
        }
    }

    /**
     * Create view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Get items on view.
        mEmailField = (EditText) view.findViewById(R.id.field_email);
        mPasswordField = (EditText) view.findViewById(R.id.field_password);
        loginError = (TextView) view.findViewById(R.id.loginError);
        signInButton = (ActionProcessButton) view.findViewById(R.id.email_sign_in_button);

        // Set button reaction on click.
        signInButton.setMode(ActionProcessButton.Mode.ENDLESS);

        // Set onClick listener.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove error if any
                loginError.setVisibility(View.GONE);

                // Sign in
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });
        return view;
    }

    /**
     * Sign in.
     * @param email: Email of user
     * @param password: Password of user
     */
    private void signIn(String email, String password) {
        // Check if for basic correct format
        if (!validateForm()) {
            return;
        }

        // Update button
        signInButton.setProgress(SENDING);

        // Sign in
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Check if finished successfully or not
                        if (!task.isSuccessful()) {
                            // Failed, set message
                            loginError.setText(task.getException().getMessage());
                            loginError.setVisibility(View.VISIBLE);
                            signInButton.setProgress(ERROR);
                        }
                        else{
                            // Success, end activity
                            signInButton.setProgress(FINISHED);
                            getActivity().finish();
                        }
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