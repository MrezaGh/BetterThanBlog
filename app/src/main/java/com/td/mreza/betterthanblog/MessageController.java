package com.td.mreza.betterthanblog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


import org.json.JSONArray;

import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

public class MessageController {
    public HashMap cache;
    private HashMap lastTimeChecked;
    NotificationCenter notificationCenter;
    Context context;
    ConnectionManager connectionManager;
    StorageManager storageManager;

    public MessageController(Context context, NotificationCenter notificationCenter){
        this.context = context;
        this.notificationCenter = notificationCenter;
        this.connectionManager = new ConnectionManager();
        this.storageManager = new StorageManager(context);

        this.cache = new HashMap();
        this.lastTimeChecked = new HashMap();

    }
    public void fetch(final int postID){
        final Boolean fromCache = fromCacheOrServer(postID);
        final CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (fromCache){
                    //todo : review and refactor
                    JSONArray details = storageManager.load(postID);

                    Log.i("response from local: ", details.toString());//todo: remove this
                    cache.put(String.valueOf(postID), details);
                    notificationCenter.data_loaded();

                }
                else {
//                    int[] numbers = connectionManager.load(lastNumber);

                    // if you want all the posts post_id = 0 else if you want a specific post give it's ID
                    JSONArray response_json = connectionManager.load(postID);

                    Log.i("response from server: ", response_json.toString());//todo: remove this

                    //todo: cache response in database
                    cache.put(postID,response_json);
                    lastTimeChecked.put(postID,System.currentTimeMillis());

                    boolean saved = storageManager.save(postID, response_json);
                    notificationCenter.data_loaded();
                }
                Log.i("cache ", String.valueOf(cache.toString()));
            }
        };
        Thread fetchThread = new Thread(runnable, "MessageController Fetch thread");
        fetchThread.start();


    }

    private Boolean fromCacheOrServer(int postID) {
        final Boolean fromCache;
        int fiveMinute = 5*60*1000;
        Long lastTimeFetched = (Long) lastTimeChecked.get(postID);

        if (!isNetworkAvailable()){
            fromCache = true;
            Log.i("network status:", "not connected");// todo : remove
        }
        else
            fromCache = lastTimeFetched != null && (System.currentTimeMillis()-lastTimeFetched) < fiveMinute;

        Log.i("fromCache: ", String.valueOf(fromCache)); //todo : remove
        return fromCache;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
