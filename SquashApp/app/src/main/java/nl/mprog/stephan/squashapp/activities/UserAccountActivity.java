package nl.mprog.stephan.squashapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nl.mprog.stephan.squashapp.helpers.FirebaseConnector;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView currentUser;
    private TextView currentEmail;
    private TextView accountErrorShow;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseConnector firebaseConnector = new FirebaseConnector();

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

        // Get views
        Button emailButton = (Button) findViewById(R.id.changeEmailButton);
        Button usernameButton = (Button) findViewById(R.id.changeUsernameButton);
        Button passwordButton = (Button) findViewById(R.id.changePasswordButton);
        Button deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);
        Button signOutButton = (Button) findViewById(R.id.signOutButton);
        currentUser = (TextView) findViewById(R.id.currentUserName);
        currentEmail = (TextView) findViewById(R.id.currentEmail);
        accountErrorShow = (TextView) findViewById(R.id.accountErrorShow);

        // set onClickListener
        try{
            emailButton.setOnClickListener(this);
            usernameButton.setOnClickListener(this);
            passwordButton.setOnClickListener(this);
            deleteAccountButton.setOnClickListener(this);
            signOutButton.setOnClickListener(this);
        }
        catch (NullPointerException e){
            Toast.makeText(UserAccountActivity.this, "Please restart app", Toast.LENGTH_SHORT).show();
        }


        // set details
        currentUser.setText(user.getDisplayName());
        currentEmail.setText(user.getEmail());

        // add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
     * On click listener.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.changeEmailButton:
                changeMail();
                break;
            case R.id.changeUsernameButton:
                changeUserName();
                break;
            case R.id.changePasswordButton:
                changePassword();
                break;
            case R.id.deleteAccountButton:
                deleteAccount();
                break;
            case R.id.signOutButton:
                signOut();
                break;
        }
    }

    /**
     * Sign out.
     */
    private void signOut(){
        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Sign out
                        auth.signOut();
                        Toast.makeText(UserAccountActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    /**
     * Change email address.
     */
    private void changeMail(){
        // Create dialog
        LayoutInflater li = LayoutInflater.from(this);
        final View layout = li.inflate(R.layout.alertdialog_auth_change_setting, null);
        final EditText password = (EditText) layout.findViewById(R.id.reAutenticate);
        final EditText settingToChange = (EditText) layout.findViewById(R.id.settingToChange);
        settingToChange.setHint(user.getEmail());
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Change email adress")
                .setView(layout)
                .setPositiveButton("Change mail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // After dialog is shown
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Set an onclick listener that will only remove dialog on success
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), password.getText().toString());

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    final String email = settingToChange.getText().toString();

                                    // Check input
                                    if (TextUtils.isEmpty(email)) {
                                        settingToChange.setError("Required.");
                                        return;
                                    } else if (!email.contains("@")) {
                                        settingToChange.setError("It must be a valid email adress.");
                                        return;
                                    } else {
                                        settingToChange.setError(null);
                                    }

                                    // Change mail
                                    user.updateEmail(email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(UserAccountActivity.this,
                                                                "Succesfully changed email",
                                                                Toast.LENGTH_SHORT).show();
                                                        currentEmail.setText(email);
                                                        dialog.cancel();
                                                    } else {
                                                        settingToChange.setError(
                                                                task.getException().getMessage());
                                                    }
                                                }
                                            });

                                } else {
                                    password.setError(task.getException().getMessage());
                                }
                            }
                        });
            }
        });
    }

    /**
     * Change username.
     */
    private void changeUserName(){
        // Create dialog
        final EditText userNameText = new EditText(this);
        userNameText.setHint(user.getDisplayName());
        userNameText.setGravity(Gravity.CENTER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Change username")
                .setView(userNameText)
                .setPositiveButton("Change username", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // empty will come after show.
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        return;
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Set an onclick listener that will only remove dialog on success
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate
                final String userName = userNameText.getText().toString();
                if(TextUtils.isEmpty(userName)){
                    userNameText.setError("Required.");
                    return;
                }
                else if(userName.length() < 3){
                    userNameText.setError("Must be al least 3 characters.");
                    return;
                }

                // Change username
                firebaseConnector.renameUser(user, userName, currentUser, UserAccountActivity.this);
                dialog.cancel();
            }
        });
    }

    /**
     * Change password.
     */
    private void changePassword(){
        // Create dialog
        LayoutInflater li = LayoutInflater.from(this);
        final View layout = li.inflate(R.layout.alertdialog_auth_change_setting, null);
        final EditText Oldpassword = (EditText) layout.findViewById(R.id.reAutenticate);
        final EditText settingToChange = (EditText) layout.findViewById(R.id.settingToChange);
        final EditText secondSetting = (EditText) layout.findViewById(R.id.secondSettingToChange);
        settingToChange.setHint("New password");
        settingToChange.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        secondSetting.setHint("Confirm password");
        secondSetting.setVisibility(View.VISIBLE);
        secondSetting.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Change password")
                .setView(layout)
                .setPositiveButton("Change password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Will be created after show
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        return;
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();

        // Set an onclick listener that will only remove dialog on success
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), Oldpassword.getText().toString());

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    final String password = settingToChange.getText().toString();
                                    final String confirm = secondSetting.getText().toString();

                                    // Validate
                                    if (TextUtils.isEmpty(password)) {
                                        settingToChange.setError("Required.");
                                        return;
                                    }
                                    else if(password.length() < 6){
                                        settingToChange.setError("Minimal 6 characters.");
                                        return;
                                    }
                                    else {
                                        settingToChange.setError(null);
                                    }

                                    if(confirm.compareTo(password) != 0){
                                        secondSetting.setError("Passwords dont match");
                                        return;
                                    }
                                    else{
                                        secondSetting.setError(null);
                                    }

                                    // Change password
                                    user.updatePassword(password)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(UserAccountActivity.this,
                                                                "Succesfully changed password",
                                                                Toast.LENGTH_SHORT).show();
                                                        dialog.cancel();
                                                    } else {
                                                        settingToChange.setError(
                                                                task.getException().getMessage());
                                                    }
                                                }
                                            });

                                } else {
                                    Oldpassword.setError(task.getException().getMessage());
                                }
                            }
                        });
            }
        });
    }

    /**
     * Delete account.
     */
    private void deleteAccount(){
        // Create dialog
        final EditText oldPassword = new EditText(this);
        oldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        oldPassword.setHint("Password");
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Delete account")
                .setView(oldPassword)
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Will be created after show
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Set an onclick listener that will only remove dialog on success
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input
                String password = oldPassword.getText().toString();
                if(password.isEmpty()){
                    oldPassword.setError("Required");
                    return;
                }
                else if(password.length() < 6){
                    oldPassword.setError("Must be larger then 6 characters");
                    return;
                }

                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), password);

                // Reauthenicate the user
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // On success remove registrations and delete account
                                    firebaseConnector.deleteUser(
                                            FirebaseAuth.getInstance().getCurrentUser(),
                                            accountErrorShow);
                                    dialog.cancel();

                                } else {
                                    oldPassword.setError(task.getException().getMessage());
                                }
                            }
                        });
            }
        });
    }
}
