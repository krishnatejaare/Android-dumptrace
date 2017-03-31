package com.example.arekr.dumptrace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.AlphabeticIndex;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemslistActivity extends AppCompatActivity implements Serializable {
    private List<item> tutorialList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemsAdapter tutorialsAdapter;
    private FloatingActionButton fab;
    private TextView heading,head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemslist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Schedule pickup");
        setSupportActionBar(toolbar);
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
