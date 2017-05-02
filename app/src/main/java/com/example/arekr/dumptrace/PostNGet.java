package com.example.arekr.dumptrace;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.apache.http.client.*;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.*;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.net.HttpURLConnection;

/**
 * Created by Krishna Teja Are on 5/2/2017.
 */

public class PostNGet extends AsyncTask<Void, Void, String> {
    public ProgressDialog mDialog;
    public JSONObject jsonToSend = new JSONObject();
    public String urlToPost;
    public String responseStr = "";

    @Override
    protected String doInBackground(Void... params) {
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlToPost);
            System.out.println("URL: " + urlToPost);
            httppost.setEntity(new StringEntity(jsonToSend.toString(), "UTF8"));
            httppost.setHeader("Content-type", "application/json");
            org.apache.http.HttpResponse resp = httpclient.execute(httppost);
            responseStr = EntityUtils.toString(resp.getEntity());
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}