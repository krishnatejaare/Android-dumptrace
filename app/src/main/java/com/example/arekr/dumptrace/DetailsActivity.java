package com.example.arekr.dumptrace;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private TextView timeView,name,address,phone,email;
    private Button time,submit;
    private int pHour;
    private int pMinute;
    static final int TIME_DIALOG_ID = 0;
    ArrayList<Details>d=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        dateView = (TextView) findViewById(R.id.dateView);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        timeView = (TextView) findViewById(R.id.timeView);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        time = (Button) findViewById(R.id.time);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Intent i = new Intent(DetailsActivity.this, PayingActivity.class);

                startActivity(i);
            }
        });
        toolbar.setTitle("Book a Pickup");
        /** Listener for click event of the button */
        time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        /** Get the current time */
        final Calendar cal = Calendar.getInstance();
        pHour = cal.get(Calendar.HOUR_OF_DAY);
        pMinute = cal.get(Calendar.MINUTE);

        /** Display the current time in the TextView */
        updateDisplay();

    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, pHour, pMinute, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    /** Callback received when the user "picks" a time in the dialog */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHour = hourOfDay;
                    pMinute = minute;
                    updateDisplay();
                    displayToast();
                }
            };

    /** Updates the time in the TextView */
    private void updateDisplay() {
        timeView.setText(
                new StringBuilder()
                        .append(pad(pHour)).append(":")
                        .append(pad(pMinute)));
    }

    /** Displays a notification when the time is updated */
    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Time choosen is ").append(timeView.getText()),   Toast.LENGTH_SHORT).show();

    }

    /** Add padding to numbers less than ten */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {
            if (name.getText().toString().equals("")||address.getText().toString().equals("")||phone.getText().toString().equals("")||email.getText().toString().equals("")) {
                Toast.makeText(DetailsActivity.this, "Incomplete Form...Please Fill the Details ", Toast.LENGTH_LONG).show();
            }
            else {


                d.add(new Details(name.getText().toString(), address.getText().toString(), phone.getText().toString(), email.getText().toString(), dateView.getText().toString(), timeView.getText().toString()));
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
                SharedPreferences.Editor edito = sharedPref.edit();
                Gson gson = new Gson();
                String jso = gson.toJson(d);
                String details = "detailskey";
                edito.putString(details, jso);
                edito.commit();

                Intent i = new Intent(DetailsActivity.this,afterdetailsActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("details", (Serializable) d);
                i.putExtra("bundle", args);
                startActivity(i);
            }

        }
    }
}
