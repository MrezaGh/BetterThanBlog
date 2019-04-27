package com.td.mreza.betterthanblog;

import android.content.Context;
import android.util.Log;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class MessageController {
    public ArrayList<Integer> cache;
    NotificationCenter notificationCenter;
    String fileDir;
    Context context;
    ConnectionManager connectionManager;
    StorageManager storageManager;

    public MessageController(Context context, NotificationCenter notificationCenter){
        this.context = context;
        this.notificationCenter = notificationCenter;
        this.connectionManager = new ConnectionManager();
        this.storageManager = new StorageManager(context);
        this.cache = new ArrayList<>();

    }
    public void fetch(final Boolean fromCache, final int lastNumber){
        final CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (fromCache){
                    //todo : review and refactor
                    int[] numbers = storageManager.load(lastNumber);
                    if (numbers != null) {
                        for (int number : numbers) {
                            cache.add(number);
                        }
                        notificationCenter.data_loaded();
                    }

                }
                else {
//                    int[] numbers = connectionManager.load(lastNumber);

                    // if you want all the posts post_id = 0 else if you want a specific post give it's ID
//                    JSONArray response_json = connectionManager.load(postId);
                    JSONArray response_json = connectionManager.load(0);

                    Log.i("response from server: ", response_json.toString());//todo: remove this

                    //todo: cache response in database
//                    boolean saved = storageManager.save(lastNumber + 10);
//                    if (numbers != null){
//                        for (int number : numbers) {
//                            cache.add(number);
//                        }
//                    }
                    notificationCenter.data_loaded();
                }
                Log.i("cache ", String.valueOf(cache));
            }
        };
        Thread fetchThread = new Thread(runnable, "MessageController Fetch thread");
        fetchThread.start();


    }
}
