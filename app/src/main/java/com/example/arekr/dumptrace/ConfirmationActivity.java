package com.example.arekr.dumptrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ConfirmationActivity extends AppCompatActivity implements AsyncResponse {
String email;
    Object[] toPass;
    HttpURLConnection httpURLConnection = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        //Getting Intent
        Intent intent = getIntent();
        try {
        JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

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
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount+" USD");
        String details="detailskey";
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfirmationActivity.this);
        Gson gson = new Gson();
        String detail = sharedPrefs.getString(details, null);
        Type type1 = new TypeToken<ArrayList<Details>>() {}.getType();
        ArrayList<Details> detailsList = gson.fromJson(detail, type1);

        for(int i=0;i<detailsList.size();i++){
            Details o=detailsList.get(i);

            email=o.getEmail();

        }
        StringBuilder googlePlacesUrl = new StringBuilder("http://192.168.43.194:8080/email");

        toPass = new Object[1];
        Log.d("TAG", googlePlacesUrl.toString());
        toPass[0] = googlePlacesUrl.toString();
        httpcall http = new httpcall();
        http.delegate = this;
        http.execute(toPass);

    }

    @Override
    public void processFinish(String output) throws JSONException {
        System.out.println("completed.............................................");
    }
}
