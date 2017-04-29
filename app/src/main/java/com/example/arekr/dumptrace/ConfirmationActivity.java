package com.example.arekr.dumptrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

import com.firebase.client.utilities.Base64;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConfirmationActivity extends AppCompatActivity implements AsyncResponse,View.OnClickListener {
String email,json;
    JSONObject jsonDetails;
    Object[] toPass;
    List<String>finaldetails=new ArrayList<>();
    private Button home;
   // HttpURLConnection httpURLConnection = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        home=(Button)findViewById(R.id.home);
        home.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Schedule pickups");
        //Getting Intent
        Intent intent = getIntent();
        try {
        jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

        //Displaying payment details
        showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
    } catch (JSONException e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException, IOException {
        //Views
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus= (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        finaldetails.add(jsonDetails.getString("id"));
        finaldetails.add(jsonDetails.getString("state"));

        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount+" USD");
        String details="detailskey";
        String items="pricekey";
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfirmationActivity.this);
        Gson gson = new Gson();
        String detail = sharedPrefs.getString(details, null);
        Type type1 = new TypeToken<ArrayList<Details>>() {}.getType();
        ArrayList<Details> detailsList = gson.fromJson(detail, type1);

        for(int i=0;i<detailsList.size();i++){
            Details o=detailsList.get(i);
            finaldetails.add(o.getName());
            finaldetails.add(o.getAddress());
            finaldetails.add(o.getEmail());
            finaldetails.add(o.getPhonenumber());
            finaldetails.add(o.getDate());
            finaldetails.add(o.getTime());
            email=o.getEmail();

        }

        String item = sharedPrefs.getString(items, null);
        Type type2 = new TypeToken<ArrayList<Price>>() {}.getType();
        ArrayList<Price> itemsList = gson.fromJson(item, type2);

        for(int i=0;i<detailsList.size();i++){
            Price o=itemsList.get(i);
            finaldetails.add(o.getCount());
            finaldetails.add(o.getFinalprice());



        }
        StringBuilder googlePlacesUrl = new StringBuilder("https:dumptrace.herokuapp.com/email");
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfirmationActivity.this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
         gson = new Gson();
        json = gson.toJson(finaldetails);
        String j="paymentkey";
        editor.putString(j, json);
        editor.commit();

        toPass = new Object[2];
        Log.d("TAG", googlePlacesUrl.toString());
        toPass[0] = googlePlacesUrl.toString();
        toPass[1]=json.toString();
        httpcall http = new httpcall();
        http.delegate = this;
        http.execute(toPass);

    }

    @Override
    public void processFinish(String output) throws JSONException {
        System.out.println("completed.............................................");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.home) {

            Intent i = new Intent(ConfirmationActivity.this, StartActivity.class);

            startActivity(i);
        }
    }
}
