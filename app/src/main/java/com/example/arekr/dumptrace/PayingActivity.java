package com.example.arekr.dumptrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PayingActivity extends AppCompatActivity implements Serializable,View.OnClickListener{

    private TextView var,t1,t2,t3,t4,t5,price,discount,finalprice;
    private Button ok;
    int x;
    double a,b,c;
    String s,w,r;
    String id;
    Intent intent;
    ArrayList<Price>p=new ArrayList<>();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying);
        intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Intent i = new Intent(PayingActivity.this, ItemslistActivity.class);

                startActivity(i);
            }
        });
        toolbar.setTitle("Book a Pickup");
        var=(TextView)findViewById(R.id.var);
        t1=(TextView)findViewById(R.id.t1);
        t2=(TextView)findViewById(R.id.t2);
        t3=(TextView)findViewById(R.id.t3);
       // t4=(TextView)findViewById(R.id.t4);
        t5=(TextView)findViewById(R.id.t5);
        price=(TextView)findViewById(R.id.price);
        //discount=(TextView)findViewById(R.id.discount);
        finalprice=(TextView)findViewById(R.id.finalprice);
        ok=(Button)findViewById(R.id.ok);
        ok.setOnClickListener(this);



    }
    @Override
    protected void onResume() {
        super.onResume();
        var.setText("");
        id="";
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PayingActivity.this);
        Gson gson = new Gson();
        String js="c";
        String jso = sharedPrefs.getString(js, null);
        Type type = new TypeToken<String>() {}.getType();
        String arrayList = gson.fromJson(jso, type);
        id = arrayList;

        var.setText(id);

        x = Integer.parseInt(id);
        if (x <= 3) {
            double a = (x) * 9.07;
            double b = 9.07;
            String s = Double.toString(a);
            String r=s;
            String w = Double.toString(b);
            price.setText(w);
           // discount.setText(r);
            finalprice.setText(s);

        } else {
            double a = x * 9.07;
            double b = 9.07;
            double c = a - b;
            String s = Double.toString(a);
            String r = Double.toString(b);
            String w = Double.toString(c);
            price.setText(r);
            //discount.setText(r);
            finalprice.setText(s);

        }



    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ok) {
            p.add(new Price(id,price.getText().toString(),finalprice.getText().toString()));
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PayingActivity.this);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            Gson gson = new Gson();
            String pricelist = gson.toJson(p);
            String price="pricekey";
            editor.putString(price, pricelist);
            editor.commit();
            Intent i = new Intent(PayingActivity.this, DetailsActivity.class);
//            Bundle args = new Bundle();
//            args.putSerializable("data", (Serializable) data);
//            i.putExtra("bundle", args);
            startActivity(i);
        }
    }
}
