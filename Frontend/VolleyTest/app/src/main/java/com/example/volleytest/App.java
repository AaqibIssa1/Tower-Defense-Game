package com.example.volleytest;

import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.volleytest.exceptions.BlockedMainThreadException;
import com.google.gson.Gson;

import java.util.concurrent.Future;


public class App extends Application {
    public static final String otherSERVER = "https://api.androidhive.info/volley/string_response.html";
    public static final Thread mainThread = Thread.currentThread();
    public static final Gson gson = new Gson();

    public static final String TAG = App.class
            .getSimpleName();
    private RequestQueue mRequestQueue;
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static synchronized App getInstance() {
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, TAG);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void assertWorkerThread() {
        if(Thread.currentThread() == mainThread)
            throw new BlockedMainThreadException();
    }

    public static <T> T getFuture(Future<T> future) {
        assertWorkerThread();
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}