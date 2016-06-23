package nl.mprog.stephan.squashapp.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nl.mprog.stephan.squashapp.adapters.FragmentInlogAdapter;

public class LoginActivity extends AppCompatActivity {

    private Integer resultCode;
    private ViewPager viewPager;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if user is already logged in
        if(user != null){
            Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set fragments
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentInlogAdapter adapter = new FragmentInlogAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

        // add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Check if google play services are up-to-date
        resultCode =
                GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.getApplicationContext());
    }

    /**
     * Go to SignInFragment.
     */
    public void goToSignInFragment(View v){
        viewPager.setCurrentItem(1);
    }

    /**
     * Go to RegisterFragment.
     */
    public void goToRegisterFragment(View v){
        viewPager.setCurrentItem(0);
    }

    /**
     * Go to ForgetPasswordFragment.
     */
    public void goToForgetPasswordFragment(View v){
        viewPager.setCurrentItem(2);
    }


    /**
     *  Check if google play is completed successfully.
     */
    @Override
    public void onResume(){
        super.onResume();

        // Check if user isn't logged in
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check if google play services are up-to-date
        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(LoginActivity.this, "Please update google play service",
                    Toast.LENGTH_SHORT).show();
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,resultCode, 0);
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
}
