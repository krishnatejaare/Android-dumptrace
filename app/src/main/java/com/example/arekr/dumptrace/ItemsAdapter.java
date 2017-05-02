package com.example.arekr.dumptrace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.json.Json;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krishna Teja Are on 3/27/2017.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> implements Serializable {

    private List<item> tutorialList;

    Activity activity;
    int count=0;

    public ItemsAdapter(List<item> tutorialList){
        this.tutorialList = tutorialList;


    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, quality, viewCount;
        public Button submit;

        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.name);
            //quality = (TextView) view.findViewById(R.id.qualityText);
            viewCount = (TextView) view.findViewById(R.id.count);
            submit=(Button)view.findViewById(R.id.submit);


        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.itemslayout) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.itemslayout, parent, false);

            return new MyViewHolder(itemView);
        }
        else{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.submitlayout, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(position == tutorialList.size()) {
            holder.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for(int i=0;i<tutorialList.size();i++){
                        item object=tutorialList.get(i);
                        count+=Integer.parseInt(object.getCount());
                    }
                    String c=Integer.toString(count);
                    Context context=view.getContext();
                   Intent intent = new Intent(context, PayingActivity.class);
                   intent.putExtra("c", c);
                   context.startActivity(intent);
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    String json = gson.toJson(c);

                    String j="c";
                    editor.putString(j, json);
                    editor.commit();
//                    Context context=view.getContext();
//                    Intent i = new Intent(context, ItemslistActivity.class);
//                    Bundle args = new Bundle();
//                    args.putSerializable("count", (Serializable) count);
//                    i.putExtra("bundle", args);
//                    context.startActivity(i);

                }
            });
        }
        else {
            item tutorial = tutorialList.get(position);
            holder.title.setText(String.valueOf(tutorial.getName()));
            holder.viewCount.setText(tutorial.getCount() + "");
        }
    }

    @Override
    public int getItemCount() {
        return (tutorialList.size()+1);
    }
    public int getItemViewType(int position) {
        return (position == tutorialList.size()) ? R.layout.submitlayout :R.layout.itemslayout ;
    }

}

