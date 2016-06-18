package com.example.stephan.squashapp.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.stephan.squashapp.adapters.pageAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class LoginActivity extends AppCompatActivity {

    private Integer resultCode;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pageAdapter adapter = new pageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

        // add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Check if google play services are up-to-date
        resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getApplicationContext());
    }

    public void goToSignInFragment(View v){
        viewPager.setCurrentItem(1);
    }

    public void goToRegisterFragment(View v){
        viewPager.setCurrentItem(0);
    }

    public void goToForgetPasswordFragment(View v){
        viewPager.setCurrentItem(2);
    }


    @Override
    public void onResume(){
        super.onResume();

        // Check if google play services are up-to-date
        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(LoginActivity.this, "Please update google play service",
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
}
