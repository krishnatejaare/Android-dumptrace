package com.example.arekr.dumptrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends NavigationalDrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private EditText item;
    private Button add,submit;
    private TextView title,helptext;
    private Spinner spinner;
    String value,json,j;
    List<item>data=new ArrayList<>();


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
        submit.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Intent i = new Intent(ItemsActivity.this, StartActivity.class);

                startActivity(i);
            }
        });
        toolbar.setTitle("Schedule pickups");
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
        value = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        String a=j;
        String b=json;
        savedInstanceState.putString(j, json);
        // etc.
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        Gson gson = new Gson();
        String myString = savedInstanceState.getString(j);
        Type type = new TypeToken<ArrayList<item>>() {}.getType();
        ArrayList<item> data = gson.fromJson(myString, type);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add) {
            ArrayList<String>name=new ArrayList<>();

            data.add(new item(item.getText().toString(),value.toString()));
            int a=Integer.parseInt(value);
            spinner.setSelection(0);
            item.setText("");

        }

        else if (view.getId() == R.id.submit) {
            ArrayList<String>name=new ArrayList<>();

            data.add(new item(item.getText().toString(),value.toString()));
//            data.add(new Tutorial("table","2"));
//            data.add(new Tutorial("chair","3"));

            System.out.println("Teja");
            System.out.println(data);
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ItemsActivity.this);
            Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                json = gson.toJson(data);

            j="itemskey";
            editor.putString(j, json);
            editor.commit();
            System.out.println("teja ITEMS LIST................................................................................");
            Intent i = new Intent(ItemsActivity.this, ItemslistActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("data", (Serializable) data);
            i.putExtra("bundle", args);
            System.out.println("teja ITEMS LIST................................................................................");
            startActivity(i);
            System.out.println("teja ITEMS LIST................................................................................");

        }
    }
}
