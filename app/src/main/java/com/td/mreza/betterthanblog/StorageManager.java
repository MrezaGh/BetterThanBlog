package com.td.mreza.betterthanblog;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class StorageManager {
    Context context;
    File database;

    public StorageManager(Context context){
        this.context = context;
        this.database = new File(context.getFilesDir(), "database");

    }
    public boolean save(final int postID, final JSONArray details){
        final CountDownLatch latch = new CountDownLatch(1);
        final boolean[] saved = {false};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("save ", "CurrentThread:" + Thread.currentThread().getName());
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString(String.valueOf(postID),details.toString()).apply();
                saved[0] = true;
                latch.countDown();
//                Log.i("congrats: ", "saved");

            }
        };

        Thread storage = new Thread(runnable, "storage thread save");
        storage.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return saved[0];

    }

    public JSONArray load(final int postID){
        final JSONArray responseArray[] = new JSONArray[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("save ", "CurrentThread:" + Thread.currentThread().getName());

                String responseText = PreferenceManager.getDefaultSharedPreferences(context).getString(String.valueOf(postID), "");
                try {
                    responseArray[0] = new JSONArray(responseText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                latch.countDown();

            }
        };

        Thread storage = new Thread(runnable, "storage thread load");
        storage.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseArray[0];
    }

}