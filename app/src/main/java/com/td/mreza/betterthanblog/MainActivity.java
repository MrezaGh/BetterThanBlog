package com.td.mreza.betterthanblog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//todo : classes should be singleton

public class MainActivity extends AppCompatActivity implements Observer  {

    LinearLayout linearLayout;
    MessageController messageController;
    NotificationCenter notificationCenter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.mehrdad);
        notificationCenter = new NotificationCenter();
        messageController = new MessageController(getBaseContext(), notificationCenter);
        linearLayout = findViewById(R.id.linear_layout);
        notificationCenter.register(this);

//        messageController.fetch(false, 0);



        Button clearBtn = findViewById(R.id.clear_btn);//done!
        Button refreshBtn = findViewById(R.id.refresh_btn);//todo:
        Button getBtn = findViewById(R.id.get_btn);//todo

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastNUmber;
                if (messageController.cache.size() == 0)
                    lastNUmber = 0;
//                else
//                    lastNUmber = messageController.cache.get(messageController.cache.size() - 1);
                messageController.fetch(0);
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastNUmber;
                if (messageController.cache.size() == 0)
                    lastNUmber = 0;
//                else
//                    lastNUmber = messageController.cache.get(messageController.cache.size() - 1);
                messageController.fetch(2);
            }
        });


        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                messageController.cache.clear();
            }
        });


    }
    private String name;
    private Subject topic;


    @Override
    public void update() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.updateList();
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
}
