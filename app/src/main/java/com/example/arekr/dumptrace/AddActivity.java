package com.example.arekr.dumptrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AddActivity extends ActionBarActivity {

    Button addRegs,button;
    Button addAssets;

    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        sp = PreferenceManager.getDefaultSharedPreferences(AddActivity.this);

        addRegs = (Button)findViewById(R.id.addRegsButton);
        button = (Button)findViewById(R.id.button);
        addAssets = (Button)findViewById(R.id.addAssetsButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Intent i = new Intent(AddActivity.this, AssetMainActivity.class);

                startActivity(i);
            }
        });
        toolbar.setTitle("Asset Manager");
        addRegs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent launchactivity = new Intent(AddActivity.this, AddRegActivity.class);
                startActivity(launchactivity);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent launchactivity = new Intent(AddActivity.this, MainActivity.class);
                startActivity(launchactivity);

            }
        });

        addAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent launchactivity = new Intent(AddActivity.this, AddAssetsActivity.class);
                startActivity(launchactivity);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            sp.edit().clear().commit();

            Intent launchactivity = new Intent(AddActivity.this, AssetMainActivity.class);
            startActivity(launchactivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
