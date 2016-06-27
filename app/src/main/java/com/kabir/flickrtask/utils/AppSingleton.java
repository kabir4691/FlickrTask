package com.kabir.flickrtask.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.bumptech.glide.Glide;

/**
 * Created by Kabir on 27-06-2016.
 */
public class AppSingleton extends Application {

    private static final String TAG = "AppSingleton";
    static AppSingleton singleton = null;

    private Handler memoryHandler;
    private static final long MEMORY_CHECK_INTERVAL = 5000;
    private static final float LOW_MEMORY_THRESHOLD = 20f;

    public static synchronized AppSingleton getInstance() {
        return singleton;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        memoryHandler = new Handler();
        startMemoryAnalyzer();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        singleton = null;
    }

    private void startMemoryAnalyzer() {
        memoryHandler.postDelayed(memoryRunnable, MEMORY_CHECK_INTERVAL);
    }

    private void clearMemory() {
        Log.d(TAG, "clear memory called");
        Glide.get(this).clearMemory();
    }

    private Runnable memoryRunnable = new Runnable() {

        @Override
        public void run() {

            float availableMemory = MemoryUtils.getPercentAvailableMemory();
//            Log.i(TAG, "available memory: " + availableMemory);
            if (availableMemory <= LOW_MEMORY_THRESHOLD) {
                clearMemory();
            }
            startMemoryAnalyzer();
        }
    };
}