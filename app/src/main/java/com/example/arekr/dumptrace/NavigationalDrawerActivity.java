package com.example.arekr.dumptrace;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;

public class NavigationalDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
public TextView t1,t2,t3,t4,t5,t6,t7,t8,count,finalprice,name,email,phone,date,time,address ;
    public Button pay;
    private String paymentAmount;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigational_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Schedule Pickups");
        setSupportActionBar(toolbar);
        t1=(TextView)findViewById(R.id.t1);
        t2=(TextView)findViewById(R.id.t2);
        t3=(TextView)findViewById(R.id.t3);
        t4=(TextView)findViewById(R.id.t4);
        t5=(TextView)findViewById(R.id.t5);
        t6=(TextView)findViewById(R.id.t6);
        t7=(TextView)findViewById(R.id.t7);
        count=(TextView)findViewById(R.id.count);
        t8=(TextView)findViewById(R.id.t8);
        address=(TextView)findViewById(R.id.address);
        finalprice=(TextView)findViewById(R.id.price);
        pay=(Button)findViewById(R.id.pay);
        pay.setOnClickListener(this);
        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        phone=(TextView)findViewById(R.id.phone);
        date=(TextView)findViewById(R.id.date);
        time=(TextView)findViewById(R.id.time);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(NavigationalDrawerActivity.this);
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
            t3.setText(o.getName());
            System.out.println(o.getAddress());
            t5.setText(o.getEmail());
            t6.setText(o.getPhonenumber());
            t7.setText(o.getDate());
            t8.setText(o.getTime());
            t4.setText(o.getAddress());
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
            t1.setText(p.getCount());
            System.out.println(p.getPrice());

            System.out.println(p.getDiscount());
            //paymentAmount=p.getFinalprice();
            paymentAmount="1";
            System.out.println(p.getFinalprice());
            t2.setText(p.getFinalprice());
        }
       // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onClick(View v) {

        getPayment();
    }
    private void getPayment() {
        //Getting the amount from editText


        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Simplified Coding Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigational_drawer, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edititems) {
            Intent i = new Intent(NavigationalDrawerActivity.this, ItemsActivity.class);

            startActivity(i);
        } else if (id == R.id.nav_editdetails) {
            Intent i = new Intent(NavigationalDrawerActivity.this, DetailsActivity.class);

            startActivity(i);

        } else if (id == R.id.nav_home) {
            Intent i = new Intent(NavigationalDrawerActivity.this, StartActivity.class);

            startActivity(i);

        } else if (id == R.id.nav_report) {
            Intent i = new Intent(NavigationalDrawerActivity.this, MainActivity.class);

            startActivity(i);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
