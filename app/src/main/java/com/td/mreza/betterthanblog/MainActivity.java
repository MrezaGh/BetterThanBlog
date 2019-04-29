package com.td.mreza.betterthanblog;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//todo : classes should be singleton

public class MainActivity extends AppCompatActivity implements Observer  {

    LinearLayout linearLayout;
    MessageController messageController;
    NotificationCenter notificationCenter;
    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private List<Product> productList;
    private int currentViewMode=0;
    static final int VIEW_MODE_LISTVIEW=0;
    static final int VIEW_MODE_GRIDVIEW=1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.mehrdad);

        notificationCenter = new NotificationCenter();
        messageController = new MessageController(getBaseContext(), notificationCenter);
        linearLayout = findViewById(R.id.linear_layout);
        notificationCenter.register(this);

//        messageController.fetch(false, 0);

        //////////
        stubGrid=(ViewStub)findViewById(R.id.stub_grid);
        stubList=(ViewStub)findViewById(R.id.stub_list);

        stubGrid.inflate();
        stubList.inflate();

        listView= (ListView) findViewById(R.id.listView);
        gridView=(GridView)findViewById(R.id.gridView);

        getProductList();

        SharedPreferences sharedPreferences= getSharedPreferences("viewMode",MODE_PRIVATE);
        currentViewMode= sharedPreferences.getInt("currentViewMode",VIEW_MODE_LISTVIEW);

        listView.setOnItemClickListener(onItemClick);
        gridView.setOnItemClickListener(onItemClick);

        swichView();
        //////////
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
//    private String name;
//    private Subject topic;


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
    private void swichView() {
        if (VIEW_MODE_LISTVIEW==currentViewMode){
            stubList.setVisibility(View.VISIBLE);
            stubGrid.setVisibility(View.GONE);

        }else {
            stubGrid.setVisibility(View.VISIBLE);
            stubList.setVisibility(View.GONE);

        }

    }
    public List<Product> getProductList(){
        productList = new ArrayList<>();
        productList.add(new Product("title1","this is body1","hey1"));
        productList.add(new Product("title2","this is body2","hey2"));
        productList.add(new Product("title3","this is body3","hey3"));
        productList.add(new Product("title4","this is body4","hey4"));
        productList.add(new Product("title5","this is body5","hey5"));
        productList.add(new Product("title6","this is body6","hey6"));
        return productList;
    }
    AdapterView.OnItemClickListener onItemClick= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(),productList.get(position).getTitle()+"__"+ productList.get(position).getDescription(), Toast.LENGTH_SHORT);

        }
    };
    public boolean onCreatOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item_menu_1:
                if (VIEW_MODE_LISTVIEW==currentViewMode){
                    currentViewMode=VIEW_MODE_GRIDVIEW;

                }else{
                    currentViewMode=VIEW_MODE_LISTVIEW;

                }
                swichView();
                SharedPreferences sharedPreferences=getSharedPreferences("ViewMode",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.commit();
                break;

        }
        return true;
    }


    private String name;
    private Subject topic;


}
