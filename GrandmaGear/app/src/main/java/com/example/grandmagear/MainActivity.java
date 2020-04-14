package com.example.grandmagear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
//        FirebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
//                .document()


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected void initializePage(){
        mPatientButton = findViewById(R.id.main_login_button);
        mLogo = findViewById(R.id.app_logo);
        mLogo.setImageResource(R.drawable.gg_default_pic);

        mPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

    }
}
