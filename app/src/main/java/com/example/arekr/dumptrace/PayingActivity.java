package com.example.arekr.dumptrace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

public class PayingActivity extends AppCompatActivity{

    private TextView var,t1,t2,t3,t4,t5,price,discount,finalprice;
    private Button ok;
    int x;
    double a,b,c;
    String s,w,r;
    String id;
    Intent intent;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying);
        intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Schedule pickups");
        var=(TextView)findViewById(R.id.var);
        t1=(TextView)findViewById(R.id.t1);
        t2=(TextView)findViewById(R.id.t2);
        t3=(TextView)findViewById(R.id.t3);
        t4=(TextView)findViewById(R.id.t4);
        t5=(TextView)findViewById(R.id.t5);
        price=(TextView)findViewById(R.id.price);
        discount=(TextView)findViewById(R.id.discount);
        finalprice=(TextView)findViewById(R.id.finalprice);
        ok=(Button)findViewById(R.id.ok);




    }
    @Override
    protected void onResume() {
        super.onResume();
        var.setText("");
        id="";
        id = intent.getStringExtra("c");

        var.setText(id);

        x = Integer.parseInt(id);
        if (x <= 3) {
            double a = (x) * 9.07;
            double b = 0;
            String s = Double.toString(a);
            String w = Double.toString(b);
            price.setText(s);
            discount.setText(s);
            finalprice.setText(w);

        } else {
            double a = x * 9.07;
            double b = 3 * 9.07;
            double c = a - b;
            String s = Double.toString(a);
            String w = Double.toString(b);
            String r = Double.toString(c);
            price.setText(s);
            discount.setText(w);
            finalprice.setText(r);

        }
    }

}
