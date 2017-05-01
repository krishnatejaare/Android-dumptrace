package com.example.arekr.dumptrace;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private Button report,book,rfid;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        report=(Button)findViewById(R.id.report);
        book=(Button)findViewById(R.id.book);
        rfid=(Button)findViewById(R.id.rfid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dump Trace");
        rfid.setOnClickListener(this);
        report.setOnClickListener(this);
        book.setOnClickListener(this);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtSpeechInput.setText(result.get(0));
                    if(result.get(0).equals("report illegal dumping")){
                        Intent i = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                    if(result.get(0).equals("order pick-up")){
                        Intent i = new Intent(StartActivity.this, ItemsActivity.class);

                        startActivity(i);
                    }
                }
                break;
            }

        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.report) {

            Intent i = new Intent(StartActivity.this, MainActivity.class);

            startActivity(i);
        }
        if (v.getId() == R.id.book) {

            Intent i = new Intent(StartActivity.this, ItemsActivity.class);

            startActivity(i);
   }
        if (v.getId() == R.id.rfid) {
            List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
            for(PackageInfo pack : packages)
            {
                ActivityInfo[] activityInfo = new ActivityInfo[0];
                try {
                    activityInfo = getPackageManager().getPackageInfo(pack.packageName, PackageManager.GET_ACTIVITIES).activities;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if(activityInfo!=null)
                {
                    for(int i=0; i<activityInfo.length; i++)
                    {
                        if(pack.packageName.equals("com.example.jasmeet.track_n_trace")){
                            System.out.println(pack.packageName);
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pack.packageName);
                            if (launchIntent != null) {
                                startActivity(launchIntent);//null pointer check in case package name was not found
                            }
                        }

                    }
                }
            }
//

        }

    }
}
