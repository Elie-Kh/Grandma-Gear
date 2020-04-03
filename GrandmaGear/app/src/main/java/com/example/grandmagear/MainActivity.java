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
    //protected NotificationHelper notificationHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        /*notificationHelper = new NotificationHelper(this);
        notificationHelper.sendOnBpm("BPM Alert", "Low BPM");
        notificationHelper.sendOnFall("FALL Alert", "Grandma Fell");
        notificationHelper.sendOnBattery("BATTERY Alert", "Low Battery");*/


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
}
