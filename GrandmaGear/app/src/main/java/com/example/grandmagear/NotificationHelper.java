package com.example.grandmagear;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationHelper {
    public static final String TAG = "_NotificationHelper";
    private SharedPreferencesHelper sharedPreferencesHelperTitle;
    private SharedPreferencesHelper sharedPreferencesHelperText;
    private SharedPreferencesHelper sharedPreferencesHelperTime;
    private NotificationManagerCompat notificationManagerCompat;
    private ArrayList<String> nTitle = new ArrayList<String>();
    private ArrayList<String> nText = new ArrayList<String>();
    private ArrayList<String> nTime = new ArrayList<String>();
    private Context context;

    public NotificationHelper(Context context){
        this.notificationManagerCompat = NotificationManagerCompat.from(context);
        this.sharedPreferencesHelperTitle = new SharedPreferencesHelper(context, "Notification Title");
        this.sharedPreferencesHelperText = new SharedPreferencesHelper(context, "Notification Text");
        this.sharedPreferencesHelperTime = new SharedPreferencesHelper(context, "Notification Time");
        this.context = context;
    }

    public void sendOnBpm(String title, String text){
        Intent intent = new Intent(context, LogInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.sooken);
        Notification notification = new NotificationCompat.Builder(context, App.BPM_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(1, notification);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());


        nTitle.add(title);
        nText.add(text);
        nTime.add(currentDateAndTime);

        sharedPreferencesHelperTitle.saveNotificationTitle(nTitle);
        sharedPreferencesHelperText.saveNotificationText(nText);
        sharedPreferencesHelperTime.saveNotificationTime(nTime);

    }

    public void sendOnFall(String title, String text){
        Intent intent = new Intent(context, LogInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.sooken);
        Notification notification = new NotificationCompat.Builder(context, App.FALL_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(2, notification);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());


        nTitle.add(title);
        nText.add(text);
        nTime.add(currentDateAndTime);

        sharedPreferencesHelperTitle.saveNotificationTitle(nTitle);
        sharedPreferencesHelperText.saveNotificationText(nText);
        sharedPreferencesHelperTime.saveNotificationTime(nTime);
    }

    public void sendOnBattery(String title, String text){
        Intent intent = new Intent(context, LogInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.sooken);
        Notification notification = new NotificationCompat.Builder(context, App.BATTERY_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(3, notification);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());


        nTitle.add(title);
        nText.add(text);
        nTime.add(currentDateAndTime);

        sharedPreferencesHelperTitle.saveNotificationTitle(nTitle);
        sharedPreferencesHelperText.saveNotificationText(nText);
        sharedPreferencesHelperTime.saveNotificationTime(nTime);
    }
}
