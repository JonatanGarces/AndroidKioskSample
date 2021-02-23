package com.curzar.androidkiosksample;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
//1000=1sec
    //2 min= 120 seg
    //120,000
    public static final int period = 120000;  //interval between two services(Here Service run every 5 seconds)
    public static final int delay = 120000 ;
    int count = 0;  //number of times service is display
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), delay, period);   //Schedule task
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
       // Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }
    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast
                   // Toast.makeText(MyService.this, "Service is running", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}