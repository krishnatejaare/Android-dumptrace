package com.example.arekr.dumptrace;

import org.json.JSONException;

/**
 * Created by arekr on 07/11/2016.
 */
public interface AsyncResponse {
    void processFinish(String output) throws JSONException;
}
