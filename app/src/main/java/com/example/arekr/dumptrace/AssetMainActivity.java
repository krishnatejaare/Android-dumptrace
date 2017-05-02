package com.example.arekr.dumptrace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AssetMainActivity extends ActionBarActivity {

    Button login,button;
    static EditText appName;
    EditText username;
    EditText password;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_main);

        appName = (EditText)findViewById(R.id.appNameText);
        login = (Button)findViewById(R.id.loginButton);

        username = (EditText)findViewById(R.id.usernameText);
        password = (EditText)findViewById(R.id.passwordText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Intent i = new Intent(AssetMainActivity.this, StartActivity.class);

                startActivity(i);
            }
        });
        toolbar.setTitle("Asset Manager");
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AssetMainActivity.this);


        Boolean isLoggedIn = sp.getBoolean("loggedIn", false);
        System.out.println("onMainCreate: "+isLoggedIn);

        if (isLoggedIn) {
            Intent launchactivity = new Intent(AssetMainActivity.this, AddActivity.class);
            startActivity(launchactivity);
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!appName.getText().toString().equals("")) {

                    url = "http://" + appName.getText().toString() + ".mybluemix.net";

                    //try to login
                    new PostNGet() {
                        @Override
                        protected void onPreExecute() {

                            try {
                                jsonToSend.put("username", username.getText().toString());
                                jsonToSend.put("password", password.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            urlToPost = url + "/logincheck";

                            mDialog = new ProgressDialog(AssetMainActivity.this);
                            mDialog.setMessage("Logging In...");
                            mDialog.show();
                        }

                        @Override
                        protected void onPostExecute(String str) {
                            System.out.println("ResponseStr: " + responseStr);

                            SharedPreferences.Editor speditor = sp.edit();
                            speditor.putString("url", url);

                            if(responseStr.contains("404 Not Found")){
                                Toast.makeText(AssetMainActivity.this, "App name incorrect!", Toast.LENGTH_LONG).show();
                            }

                            else{

                                try {
                                    JSONObject respJSON = new JSONObject(responseStr);

                                    if ((Boolean)respJSON.get("status")) {

                                        speditor.putBoolean("loggedIn", true).commit();
                                        Intent launchactivity = new Intent(AssetMainActivity.this, AddActivity.class);
                                        startActivity(launchactivity);
                                    }
                                    else{
                                        Toast.makeText(AssetMainActivity.this, (String)respJSON.get("error"), Toast.LENGTH_LONG).show();
                                    }
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            mDialog.dismiss();

                        }
                    }.execute();


                } else {
                    Toast.makeText(AssetMainActivity.this, "Enter App Name first", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
