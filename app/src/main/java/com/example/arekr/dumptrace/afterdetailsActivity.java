package com.example.arekr.dumptrace;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class afterdetailsActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterdetails);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(afterdetailsActivity.this);
        Gson gson = new Gson();
       String j="itemskey";
        String jso = sharedPrefs.getString(j, null);
        Type type = new TypeToken<ArrayList<item>>() {}.getType();
        ArrayList<item> arrayList = gson.fromJson(jso, type);
        for(int i=0;i<arrayList.size();i++){
            item object=arrayList.get(i);
            System.out.println(object.getName());
            System.out.println(object.getCount());
        }
        String details="detailskey";
        String detail = sharedPrefs.getString(details, null);
        Type type1 = new TypeToken<ArrayList<Details>>() {}.getType();
        ArrayList<Details> detailsList = gson.fromJson(detail, type1);

        for(int i=0;i<detailsList.size();i++){
            Details o=detailsList.get(i);
            System.out.println(o.getName());
            System.out.println(o.getAddress());
            System.out.println(o.getEmail());
            System.out.println(o.getPhonenumber());
            System.out.println(o.getDate());
            System.out.println(o.getTime());
        }
        String price="pricekey";
        String rice=sharedPrefs.getString(price,null);
        Type type2 = new TypeToken<ArrayList<Price>>() {}.getType();
        ArrayList<Price> priceList = gson.fromJson(rice, type2);
        for(int i=0;i<priceList.size();i++){
            Price p=priceList.get(i);
            System.out.println(p.getCount());
            System.out.println(p.getPrice());
            System.out.println(p.getDiscount());
            System.out.println(p.getFinalprice());
        }
    }
}
