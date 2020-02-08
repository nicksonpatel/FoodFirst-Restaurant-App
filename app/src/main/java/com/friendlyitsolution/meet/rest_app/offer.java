package com.friendlyitsolution.meet.rest_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class offer extends AppCompatActivity {
    RecyclerView recy;
    static PlanAdapter adapter;
    static List<plan_model> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recy=(RecyclerView)findViewById(R.id.recy);
        list=new ArrayList<>();
        adapter=new PlanAdapter(list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Myapp.con);
        recy.setLayoutManager(mLayoutManager);
        recy.setItemAnimator(new DefaultItemAnimator());
        recy.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),add_offer.class);
                startActivity(i);      }
        });
        getData();
    }

    @Override
    protected void onRestart() {
            super.onRestart();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    static void getData()
    {

        list.clear();
        adapter.notifyDataSetChanged();
        if(Myapp.userdata.containsKey("plans")) {
            Map<String, Object> datas = (Map<String, Object>) Myapp.userdata.get("plans");
            List<String> k=new ArrayList<>(datas.keySet());
            for(int i=0;i<k.size();i++)
            {
                Map<String,String> data=(Map<String, String>) datas.get(k.get(i));
                plan_model m=new plan_model(data.get("name"),data.get("price"),data.get("dec"),data.get("capacity"),data.get("eprice"),k.get(i));
                list.add(m);
            }
        }
        else
        {
            Myapp.showMsg("No data found");
        }

    }

}
