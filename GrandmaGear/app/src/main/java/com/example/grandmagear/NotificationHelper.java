package com.example.grandmagear;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.grandmagear.Patient_Main_Lobby.NotificationReceiver;
import com.example.grandmagear.Patient_Main_Lobby.NotificationReceiverDecline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationHelper {
    public static final String TAG = "_NotificationHelper";
    private NotificationManagerCompat notificationManagerCompat;
    private ArrayList<String> nTitle = new ArrayList<String>();
    private ArrayList<String> nText = new ArrayList<String>();
    private ArrayList<String> nTime = new ArrayList<String>();
    private ArrayList<String> nDeviceId = new ArrayList<String>();
    private Context context;
    private FirebaseObjects.UserDBO userDBO;
    private FirebaseHelper firebaseHelper;

    public NotificationHelper(Context context, FirebaseObjects.UserDBO userDBO){
        this.notificationManagerCompat = NotificationManagerCompat.from(context);
        this.context = context;
        this.userDBO = userDBO;
        firebaseHelper = new FirebaseHelper();
    }

    public void sendOnBpm(String title, String text, String deviceID){
        Intent intent = new Intent(context, LogInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.gg_default_pic);
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

        FirebaseObjects.Notifications bpm = new FirebaseObjects.Notifications(title, text, currentDateAndTime, deviceID);
        firebaseHelper.addNotification(bpm);

        /*nTitle.add(title);
        nText.add(text);
        nTime.add(currentDateAndTime);

        sharedPreferencesHelperTitle.saveNotificationTitle(nTitle);
        sharedPreferencesHelperText.saveNotificationText(nText);
        sharedPreferencesHelperTime.saveNotificationTime(nTime);*/

    }

    public void sendOnFall(String title, String text, String deviceID){
        Intent intent = new Intent(context, LogInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.gg_default_pic);
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

        FirebaseObjects.Notifications fall = new FirebaseObjects.Notifications(title, text, currentDateAndTime, deviceID);
        firebaseHelper.addNotification(fall);

        /*nTitle.add(title);
        nText.add(text);
        nTime.add(currentDateAndTime);

        sharedPreferencesHelperTitle.saveNotificationTitle(nTitle);
        sharedPreferencesHelperText.saveNotificationText(nText);
        sharedPreferencesHelperTime.saveNotificationTime(nTime);*/
    }

    public void sendOnBattery(String title, String text, String deviceID){
        Intent intent = new Intent(context, LogInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.gg_default_pic);
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


        FirebaseObjects.Notifications battery = new FirebaseObjects.Notifications(title, text, currentDateAndTime, deviceID);
        firebaseHelper.addNotification(battery);

        /*nTitle.add(title);
        nText.add(text);
        nTime.add(currentDateAndTime);

        sharedPreferencesHelperTitle.saveNotificationTitle(nTitle);
        sharedPreferencesHelperText.saveNotificationText(nText);
        sharedPreferencesHelperTime.saveNotificationTime(nTime);*/
    }

    public void sendOnPanic(String title, String text, String deviceID){
        Intent intent = new Intent(context, LogInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.gg_default_pic);
        Notification notification = new NotificationCompat.Builder(context, App.PANIC_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(4, notification);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());

        FirebaseObjects.Notifications help = new FirebaseObjects.Notifications(title, text, currentDateAndTime, deviceID);
        firebaseHelper.addNotification( help);

        /*nTitle.add(title);
        nText.add(text);
        nTime.add(currentDateAndTime);

        sharedPreferencesHelperTitle.saveNotificationTitle(nTitle);
        sharedPreferencesHelperText.saveNotificationText(nText);
        sharedPreferencesHelperTime.saveNotificationTime(nTime);*/
    }


    public void sendOnRequestLocation(String title, String text, String deviceID){
        Intent intent = new Intent(context, LogInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //Accept
        Intent enableLocation = new Intent(context, NotificationReceiver.class);
        PendingIntent enableLocationPendingIntent = PendingIntent.getBroadcast(context, 0, enableLocation, PendingIntent.FLAG_CANCEL_CURRENT);

        //Decline
        Intent disableLocation = new Intent(context, NotificationReceiverDecline.class);
        PendingIntent disableLocationPendingIntent = PendingIntent.getBroadcast(context, 0, disableLocation, PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.gg_default_pic);
        Notification notification = new NotificationCompat.Builder(context, App.REQUEST_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.enablelocation, "Accept", enableLocationPendingIntent)
                .addAction(R.drawable.enablelocation, "Decline", disableLocationPendingIntent)
                .build();
        notificationManagerCompat.notify(5, notification);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());

        FirebaseObjects.Notifications help = new FirebaseObjects.Notifications(title, text, currentDateAndTime, deviceID);
        firebaseHelper.addNotification( help);

        /*nTitle.add(title);
        nText.add(text);
        nTime.add(currentDateAndTime);

        sharedPreferencesHelperTitle.saveNotificationTitle(nTitle);
        sharedPreferencesHelperText.saveNotificationText(nText);
        sharedPreferencesHelperTime.saveNotificationTime(nTime);*/
    }

}
