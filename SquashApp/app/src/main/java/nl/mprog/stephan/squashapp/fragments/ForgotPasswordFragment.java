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
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nl.mprog.stephan.squashapp.activities.R;

/**
 * Fragment for when users have forgotten their password.
 */
public class ForgotPasswordFragment extends Fragment {

    private final int SENDING = 1;      // Button sending code
    private final int FINISHED = 100;   // Button finished code
    private final int ERROR = -1;       // Button error code

    private EditText emailEditText;
    private TextView errorField;
    private ActionProcessButton forgotPasswordButton;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * newInstance constructor for creating fragment with arguments.
      */
    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    /**
     *  Store instance variables based on arguments passed
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate the view for the fragment based on layout XML
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        // Get view
        emailEditText = (EditText) view.findViewById(R.id.forgotPasswordMail);
        forgotPasswordButton = (ActionProcessButton) view.findViewById(R.id.forgotPasswordButton);
        errorField = (TextView) view.findViewById(R.id.errorField);

        // Set special mode
        forgotPasswordButton.setMode(ActionProcessButton.Mode.ENDLESS);

        // Set onClick listener
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        // Check if really logged in
        if (user != null) {
            getActivity().finish();
        }

        return view;
    }

    /**
     * Send password mail.
     */
    private void forgotPassword() {

        if (!validateForm()) {
            return;
        }

        forgotPasswordButton.setProgress(SENDING);
        String emailAddress = emailEditText.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Email send" +
                                            "\nIt may take a moment until it arrives",
                                    Toast.LENGTH_SHORT).show();
                            forgotPasswordButton.setProgress(FINISHED);
                        } else {
                            forgotPasswordButton.setProgress(ERROR);
                            errorField.setText(task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * Validate given input.
     */
    private boolean validateForm() {
        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required.");
            return false;
        } else if (!email.contains("@")) {
            emailEditText.setError("It must be a valid email adress.");
            return false;
        } else {
            emailEditText.setError(null);
        }
        return true;
    }
}