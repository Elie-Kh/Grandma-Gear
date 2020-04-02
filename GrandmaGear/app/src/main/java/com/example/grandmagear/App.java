package com.example.grandmagear;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class App extends Application {
    public static final String BPM_CHANNEL = "bpm";
    public static final String FALL_CHANNEL = "fall";
    public static final String BATTERY_CHANNEL = "battery";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel bpm = new NotificationChannel(BPM_CHANNEL,
                    "BPM",
                    NotificationManager.IMPORTANCE_HIGH);
            bpm.setDescription("channel bpm");

            NotificationChannel fall = new NotificationChannel(FALL_CHANNEL,
                    "FALL",
                    NotificationManager.IMPORTANCE_LOW);
            fall.setDescription("channel fall");

            NotificationChannel battery = new NotificationChannel(BATTERY_CHANNEL,
                    "BATTERY",
                    NotificationManager.IMPORTANCE_LOW);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(bpm);
            manager.createNotificationChannel(fall);
            manager.createNotificationChannel(battery);
        }
    }
}
