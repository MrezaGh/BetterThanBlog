package com.td.mreza.betterthanblog;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, Observer{

    LinearLayout linearLayout;
    MessageController messageController;
    NotificationCenter notificationCenter;

    private int mColumnCount = 3;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        notificationCenter = new NotificationCenter();
        messageController = new MessageController(getBaseContext(), notificationCenter);
        notificationCenter.register(this);



        findViewById(R.id.btnAddColumn).setOnClickListener(this);
        findViewById(R.id.btnDecreaseColumn).setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        setLayoutManager();
        messageController.fetch(0);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddColumn:
                mColumnCount++;
                setLayoutManager();
                break;
            case R.id.btnDecreaseColumn:
                mColumnCount--;
                setLayoutManager();
                break;
        }
    }

    private void setLayoutManager() {
        if (mColumnCount <= 1) {
            mColumnCount = 1;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
    }

    @Override
    public void update() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new CardsAdapter((JSONArray) messageController.cache.get(0)));
            }
        });
    }

    private void updateList() {
        HashMap list =messageController.cache;
        ArrayList<TextView> tvs = new ArrayList<>();
//        for (Integer s:list) {
//            TextView tv=new TextView(getApplicationContext());
//            tv.setText(s.toString());
//            tvs.add(tv);
//
//        }
        Log.i("Threadd", Thread.currentThread().getName());
        ArrayList<TextView> tvsNew = new ArrayList<>();
        for (int i = 0; i <tvs.size() ; i++) {
            if(i>tvs.size()-11) {
                tvsNew.add(tvs.get(i));
            }
        }
        for (TextView t: tvsNew) {
            linearLayout.addView(t);
        }
    }

    @Override
    public void setSubject(Subject sub) {
        this.topic=sub;
    }

    private String name;
    private Subject topic;


}
