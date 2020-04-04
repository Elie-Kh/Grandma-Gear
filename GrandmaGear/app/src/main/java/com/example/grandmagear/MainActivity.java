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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "_Main Activity";
    protected Button mPatientButton;
    protected ImageView mLogo;
    private SharedPreferencesHelper mSharedPreferences;
    protected SharedPreferencesHelper sharedPreferencesHelper_Login;
    protected FirebaseHelper firebaseHelper;
    protected NotificationHelper notificationHelper;
    protected FirebaseObjects.UserDBO userDBO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseHelper = new FirebaseHelper();
        sharedPreferencesHelper_Login = new SharedPreferencesHelper(MainActivity.this, "Login");
        mSharedPreferences = new SharedPreferencesHelper(MainActivity.this,
                "DisclaimerPreferences");
        //Log.d(TAG, firebaseHelper.getCurrentUserID());

        //check if user had previously accepted disclaimer. If not, open fragment
        if(mSharedPreferences.getDisclaimerStatus() == null
        || mSharedPreferences.getDisclaimerStatus().equals("False")){
            DisclaimerFragment disclaimerFragment = new DisclaimerFragment();
            disclaimerFragment.setCancelable(false);
            disclaimerFragment.show(getSupportFragmentManager(), "DisclaimerFragment");
        }
        initializePage();
        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
            @Override
            public void onCallback(FirebaseObjects.UserDBO user) {
                userDBO = user;
                /*notificationHelper = new NotificationHelper(MainActivity.this, userDBO);
                notificationHelper.sendOnBpm("BPM Alert", "Low BPM");
                notificationHelper.sendOnFall("FALL Alert", "Grandma Fell");
                notificationHelper.sendOnBattery("BATTERY Alert", "Low Battery");*/
            }
        },sharedPreferencesHelper_Login.getEmail(),
                Boolean.parseBoolean(sharedPreferencesHelper_Login.getType()));


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
