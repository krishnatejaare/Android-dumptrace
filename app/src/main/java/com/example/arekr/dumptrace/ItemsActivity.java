package com.example.arekr.dumptrace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private EditText item;
    private Button add,submit;
    private TextView title,helptext;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        add=(Button)findViewById(R.id.add);
        submit=(Button)findViewById(R.id.submit);
        item=(EditText)findViewById(R.id.item);
        title=(TextView)findViewById(R.id.title);
        helptext=(TextView)findViewById(R.id.helptext);
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        add.setOnClickListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("Enter Text if > 5 ");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add) {


        }
    }
}
