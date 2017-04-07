package com.example.arekr.dumptrace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.AlphabeticIndex;
import android.icu.text.DisplayContext;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DebugActivity extends AppCompatActivity implements Serializable {
    private List<item> tutorialList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemsAdapter tutorialsAdapter;
    private FloatingActionButton fab;
    private TextView heading,head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("teja ITEMS LIST................................................................................");
        setContentView(R.layout.activity_debug);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Schedule pickup");
        //setSupportActionBar(toolbar);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(DebugActivity.this);
        Gson gson = new Gson();
        String js=null;
        String jso = sharedPrefs.getString(js, null);
        Type type = new TypeToken<ArrayList<item>>() {}.getType();
        ArrayList<item> arrayList = gson.fromJson(jso, type);

        for(int i=0;i<arrayList.size();i++){
            item object=arrayList.get(i);
            System.out.println(object.getName());
            System.out.println(object.getCount());
        }


        heading=(TextView)findViewById(R.id.heading);
        //head=(TextView)findViewById(R.id.head);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        ArrayList<item>tutorialList=(ArrayList<item>) args.getSerializable("data");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        System.out.println("teja");
        //System.out.println(tutorialList.get(0).getTitle());

        tutorialsAdapter = new ItemsAdapter(tutorialList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tutorialsAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
