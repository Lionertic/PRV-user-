package com.example.lionertic.main.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

import com.example.lionertic.main.AsyncTask.Routes;
import com.example.lionertic.main.Fragments.Maps;
import com.example.lionertic.main.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;

public class DriverRouting extends Service {

    Handler mHandler = new Handler();
    Runnable mRunnable;
    SharedPreferences sd;
    public DriverRouting() {
        sd=MainActivity.context.getSharedPreferences("route",Context.MODE_PRIVATE);
        sd.edit().putInt("0",0).apply();
        sd.edit().putInt("1",0).apply();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "My Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getRoute();
        return START_NOT_STICKY;
    }

    void getRoute(){

        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                new Routes(MainActivity.context).execute(Maps.key);
                mHandler.postDelayed(mRunnable, 5000);
            }
        }, 5000);
    }
}
