package com.example.arekr.dumptrace;

/**
 * Created by arekr on 07/11/2016.
 */
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http extends AsyncTask<Object, Integer, String> {
    public AsyncResponse delegate = null;

    protected String doInBackground(Object... inputObj) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        String googlePlacesUrl = (String) inputObj[0];
        String httpData = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(googlePlacesUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            System.out.println(httpURLConnection.getResponseCode());
            httpURLConnection.connect();
            System.out.println(httpURLConnection.getResponseCode());

            inputStream = httpURLConnection.getInputStream();
            System.out.println(httpURLConnection.getResponseCode());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            httpData = stringBuffer.toString();

            System.out.println(httpData);
            bufferedReader.close();
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        } finally {
            try {
                if(inputStream!=null)
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpURLConnection.disconnect();
        }

        return httpData;
    }

    protected void onPostExecute(String result) {
        try {

            delegate.processFinish(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
