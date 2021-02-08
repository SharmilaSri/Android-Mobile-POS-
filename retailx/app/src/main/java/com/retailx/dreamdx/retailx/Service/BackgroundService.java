package com.retailx.dreamdx.retailx.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.Syncronisation.SyncFunctions;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

    private Timer mTimer=new Timer();


   public BackgroundService(){

   }

    @Override
    public void onCreate() {
        super.onCreate();

        mTimer.scheduleAtFixedRate(new DisplayToastTimerTask(),0,3*1000);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(BackgroundService.this,"stopped",Toast.LENGTH_SHORT).show();
        mTimer.cancel();


    }
    private Handler mHandler = new Handler();
    private class DisplayToastTimerTask extends TimerTask {

        @Override
        public void run() {

           SyncFunctions.syncWithServer(BackgroundService.this,SharedPreference.getInstance().getValue(BackgroundService.this,Constants.USER_ID));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "running", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
