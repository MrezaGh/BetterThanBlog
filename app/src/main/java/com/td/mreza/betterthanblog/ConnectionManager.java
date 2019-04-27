package com.td.mreza.betterthanblog;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class ConnectionManager {


    public JSONArray load(final int post_id){
        final JSONArray responseArray[] = new JSONArray[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                StringBuilder response = new StringBuilder();
                String url_address = getURLString(post_id);
                try {
                    responseArray[0] = getResponseFromServer(response, url_address);

                    latch.countDown();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("CurrentThread: ", Thread.currentThread().getName());
            }
        };

        Thread cloud = new Thread(runnable, "cloud thread");
        cloud.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("connection thread ->", String.valueOf(Thread.currentThread().getId()));

        return  responseArray[0];

    }

    private JSONArray getResponseFromServer(StringBuilder response, String url_address) throws IOException, JSONException {
        URL posts_url = new URL(url_address);
        HttpURLConnection connection = (HttpURLConnection)posts_url.openConnection();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = input.readLine()) != null){
                response.append(line);
            }
            input.close();
        }
        String responseText = response.toString();

        return new JSONArray(responseText);
    }

    private String getURLString(int post_id) {
        String posts_url_address = "https://jsonplaceholder.typicode.com/posts";
        String post_comment_address = "https://jsonplaceholder.typicode.com/comments?postId=" + post_id;
        String urlString;
        if (post_id == 0)
            urlString = posts_url_address;
        else
            urlString = post_comment_address;
        return urlString;
    }
}
