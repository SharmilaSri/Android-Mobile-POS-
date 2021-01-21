package com.retailx.dreamdx.retailx.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.retailx.dreamdx.retailx.UtilityFunctions;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.Validator;

public class DatauploadService extends Service {

    Context context;
    String clientId="-1";
    final String CHANNEL_ID="1000";
    final int SYNC_NOTIFICATION_ID=1000;

   public DatauploadService(){

   }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId) {

        String userId = SharedPreference.getInstance().getValue(getApplicationContext()
                , Constants.USER_ID);

        if(UtilityFunctions.isNetworkConnected(getApplicationContext()) || UtilityFunctions.isInternetAvailable()) {
            //SyncFunctions.syncWithServer(getApplicationContext(), userId);
        }else{
            Validator.showToast(this,"NO INTERNET");
        }

        stopSelf();
        return START_NOT_STICKY;

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager= getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
