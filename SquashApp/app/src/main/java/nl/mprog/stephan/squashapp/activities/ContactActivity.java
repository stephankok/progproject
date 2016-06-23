package nl.mprog.stephan.squashapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Show contact information of S.C.H.I.E.T. Squash
 */
public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView callUSC = (TextView) findViewById(R.id.callUSC);
        TextView emailSchiet = (TextView) findViewById(R.id.emailSchiet);
        TextView webPageSchiet = (TextView) findViewById(R.id.webPageSchiet);
        TextView webPageUSC = (TextView) findViewById(R.id.webPageUSC);
        TextView webPageFacebook = (TextView) findViewById(R.id.webPageFacebook);

        callUSC.setOnClickListener(this);
        emailSchiet.setOnClickListener(this);
        webPageSchiet.setOnClickListener(this);
        webPageUSC.setOnClickListener(this);
        webPageFacebook.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.callUSC:
                Toast.makeText(ContactActivity.this, "Calling USC", Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:020 5258955"));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
                break;
            case R.id.emailSchiet:
                Toast.makeText(ContactActivity.this, "Mailing SCHIET", Toast.LENGTH_SHORT).show();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: info@schietsquash.nl"));
                startActivity(Intent.createChooser(emailIntent, "Email S.C.H.I.E.T. Squash"));
                break;
            case R.id.webPageSchiet:
                Intent webPageSchietIntent = new Intent(Intent.ACTION_VIEW);
                webPageSchietIntent.setData(Uri.parse("http://www.schietsquash.nl"));
                startActivity(webPageSchietIntent);
                break;
            case R.id.webPageUSC:
                Intent webPageUSCIntent = new Intent(Intent.ACTION_VIEW);
                webPageUSCIntent.setData(Uri.parse("http://www.usc.nl"));
                startActivity(webPageUSCIntent);
                break;
            case R.id.webPageFacebook:
                Intent webPageFBIntent = new Intent(Intent.ACTION_VIEW);
                webPageFBIntent.setData(Uri.parse("http://www.facebook.com/schietsquashamsterdam"));
                startActivity(webPageFBIntent);
                break;

            default:
                break;
        }
    }
}
