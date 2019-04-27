package com.td.mreza.betterthanblog;

import android.content.Context;
import android.util.Log;

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
    public boolean save(final int lastNumber){
        final CountDownLatch latch = new CountDownLatch(1);
        final boolean[] saved = {false};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("save ", "CurrentThread:" + Thread.currentThread().getName());
                try {
                    PrintWriter writer = new PrintWriter(database);
                    writer.print(lastNumber);
                    writer.close();
                    saved[0] = true;
                    latch.countDown();
//                    Log.i("congrats: ", "saved");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d("oops: ", "printer pokid");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.d("oops: ", "printer pokid nullpointer");
                }
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

    public int[] load(final int lastNumber){
        final int[] numbers = new int[10];
        final CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("save ", "CurrentThread:" + Thread.currentThread().getName());
                try {
                    FileInputStream fileInputStream = context.openFileInput("database");
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String lineData = bufferedReader.readLine();
//                    Log.i("files -> ", "data: " + lineData);
                    bufferedReader.close();
                    if (lineData != null){
                        int lastSavedNumber = Integer.parseInt(lineData);
                        if (lastNumber < lastSavedNumber)
                        for (int i = 0; i < 10; i++) {
                            numbers[i] = i + lastNumber + 1;
                        }
                    }
                    else if (lastNumber == 0){
                        for (int i = 0; i < 10; i++) {
                            numbers[i] = i + 1;
                        }
                    }
                    latch.countDown();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.i("oops", "file not found");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("oops", "couldn't read file");
                }

            }
        };

        Thread storage = new Thread(runnable, "storage thread load");
        storage.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (numbers[0] == 0)//if nothing needs to return numbers is {0,0,...}
            return null;
        return numbers;
    }

}