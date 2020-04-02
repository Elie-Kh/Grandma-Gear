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

import java.util.ArrayList;

public class NotificationHelper {
    public static final String TAG = "_NotificationHelper";
    private SharedPreferencesHelper sharedPreferencesHelper;
    private NotificationManagerCompat notificationManagerCompat;
    private ArrayList<String> nTitle = new ArrayList<String>();
    private ArrayList<String> nText = new ArrayList<String>();
    private Context context;

    public NotificationHelper(Context context){
        this.notificationManagerCompat = NotificationManagerCompat.from(context);
        this.context = context;
    }

    public void sendOnBpm(String title, String text){
        Intent intent = new Intent(context, UserActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.sooken);
        Notification notification = new NotificationCompat.Builder(context, App.BPM_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(largeIcon)
                        .bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(1, notification);

        nTitle.add(title);
        nText.add(text);
        Log.d(TAG, nTitle.get(0));
        Log.d(TAG, nText.get(0));

        sharedPreferencesHelper = new SharedPreferencesHelper(context, "Notification Title");
        sharedPreferencesHelper.saveNotificationTitle(nTitle);
        Log.d(TAG, sharedPreferencesHelper.getNotificationTitle().get(0));
        sharedPreferencesHelper = new SharedPreferencesHelper(context, "Notification Text");
        sharedPreferencesHelper.saveNotificationText(nText);
        Log.d(TAG, sharedPreferencesHelper.getNotificationText().get(0));
    }

    public void sendOnFall(String title, String text){
        Intent intent = new Intent(context, UserActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.sooken);
        Notification notification = new NotificationCompat.Builder(context, App.FALL_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(largeIcon)
                        .bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(2, notification);

        nTitle.add(title);
        nText.add(text);
        Log.d(TAG, nTitle.get(1));
        Log.d(TAG, nText.get(1));

        sharedPreferencesHelper = new SharedPreferencesHelper(context, "Notification Title");
        sharedPreferencesHelper.saveNotificationTitle(nTitle);
        Log.d(TAG, sharedPreferencesHelper.getNotificationTitle().get(1));
        sharedPreferencesHelper = new SharedPreferencesHelper(context, "Notification Text");
        sharedPreferencesHelper.saveNotificationText(nText);
        Log.d(TAG, sharedPreferencesHelper.getNotificationText().get(1));
    }

    public void sendOnBattery(String title, String text){
        Intent intent = new Intent(context, UserActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.sooken);
        Notification notification = new NotificationCompat.Builder(context, App.BATTERY_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(largeIcon)
                        .bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(3, notification);

        nTitle.add(title);
        nText.add(text);
        Log.d(TAG, nTitle.get(2));
        Log.d(TAG, nText.get(2));

        sharedPreferencesHelper = new SharedPreferencesHelper(context, "Notification Title");
        sharedPreferencesHelper.saveNotificationTitle(nTitle);
        Log.d(TAG, sharedPreferencesHelper.getNotificationTitle().get(0));
        sharedPreferencesHelper = new SharedPreferencesHelper(context, "Notification Text");
        sharedPreferencesHelper.saveNotificationText(nText);
        Log.d(TAG, sharedPreferencesHelper.getNotificationText().get(0));
    }
}
