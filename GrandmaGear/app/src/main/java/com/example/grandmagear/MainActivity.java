package com.example.grandmagear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected Button mPatientButton;
    protected ImageView mLogo;
    private SharedPreferencesHelper mSharedPreferences;
    private NotificationManagerCompat notificationManagerCompat;
    protected ArrayList<String> title = new ArrayList<String>();
    protected ArrayList<String> text = new ArrayList<String>();
    protected MediaSessionCompat mediaSessionCompat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManagerCompat = NotificationManagerCompat.from(this);
        mSharedPreferences = new SharedPreferencesHelper(MainActivity.this,
                "DisclaimerPreferences");

        //check if user had previously accepted disclaimer. If not, open fragment
        if(mSharedPreferences.getDisclaimerStatus() == null
        || mSharedPreferences.getDisclaimerStatus().equals("False")){
            DisclaimerFragment disclaimerFragment = new DisclaimerFragment();
            disclaimerFragment.setCancelable(false);
            disclaimerFragment.show(getSupportFragmentManager(), "DisclaimerFragment");
        }
        initializePage();
        mediaSessionCompat = new MediaSessionCompat(this, "tag");
        sendOnBpm();
        sendOnFall();
        sendOnBattery();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected void initializePage(){
        mPatientButton = findViewById(R.id.main_login_button);
        mLogo = findViewById(R.id.app_logo);
        mLogo.setImageResource(R.drawable.sooken);

        mPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

    }

    public void sendOnBpm(){
        Intent intent = new Intent(this, UserActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.sooken);
        Notification notification = new NotificationCompat.Builder(this, App.BPM_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle("BPM Alert")
                .setContentText("Low bpm")
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
        title.add("BPM Alert");
        text.add("Low bpm");

        mSharedPreferences = new SharedPreferencesHelper(MainActivity.this, "Notification Title");
        mSharedPreferences.saveNotificationTitle(title);
        mSharedPreferences = new SharedPreferencesHelper(MainActivity.this, "Notification Text");
        mSharedPreferences.saveNotificationText(text);
    }

    public void sendOnFall(){
        Intent intent = new Intent(this, UserActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.sooken);
        Notification notification = new NotificationCompat.Builder(this, App.FALL_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle("FALL Alert")
                .setContentText("Grandma fell")
                .setLargeIcon(largeIcon)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(2, notification);

        title.add("FALL Alert");
        text.add("Grandma fell");

        mSharedPreferences = new SharedPreferencesHelper(MainActivity.this, "Notification Title");
        mSharedPreferences.saveNotificationTitle(title);
        mSharedPreferences = new SharedPreferencesHelper(MainActivity.this, "Notification Text");
        mSharedPreferences.saveNotificationText(text);
    }

    public void sendOnBattery(){
        Intent intent = new Intent(this, UserActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.sooken);
        Notification notification = new NotificationCompat.Builder(this, App.BATTERY_CHANNEL)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle("BATTERY Alert")
                .setContentText("Low Battery")
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

        title.add("BATTERY Alert");
        text.add("Low Battery");

        mSharedPreferences = new SharedPreferencesHelper(MainActivity.this, "Notification Title");
        mSharedPreferences.saveNotificationTitle(title);
        mSharedPreferences = new SharedPreferencesHelper(MainActivity.this, "Notification Text");
        mSharedPreferences.saveNotificationText(text);
    }
}
