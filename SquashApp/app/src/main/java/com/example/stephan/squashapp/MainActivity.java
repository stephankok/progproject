package com.example.stephan.squashapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.reload:
                Toast.makeText(MainActivity.this, "Reload", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admin:
                Toast.makeText(MainActivity.this, "Admin", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menulogo:
                Toast.makeText(MainActivity.this, "Logo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
