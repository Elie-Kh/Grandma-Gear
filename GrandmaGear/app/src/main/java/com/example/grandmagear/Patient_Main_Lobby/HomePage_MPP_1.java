package com.example.grandmagear.Patient_Main_Lobby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;
import com.example.grandmagear.LogInActivity;
import com.example.grandmagear.R;
import com.example.grandmagear.SharedPreferencesHelper;
import com.example.grandmagear.UserActivity;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.grandmagear.Patient_Main_Lobby.PatientSettingsActivity.location;

public class HomePage_MPP_1 extends AppCompatActivity {
    private static final String TAG = "HomePage_MPP_1";
    private Handler mainHandler = new Handler();


    MenuItem Setting;
    Switch locationSwitch;
    TextView FullName;
    TextView Age;
    TextView Weight;
    TextView Height;

    ImageView ProfilePicture;
    ImageView Heart;
    ImageView Battery;
    ImageView Earth;

    FirebaseHelper firebaseHelper = new FirebaseHelper();
    SharedPreferencesHelper mSharedPreferencesHelper_Login;
    protected FirebaseObjects.UserDBO thisUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page__mpp_1);







    }
    @Override
    public void onResume() {
        super.onResume();
            setUpUI();
            resumeSharedLocation();
            resumeLocation(locationSwitch);
            locationStatus(locationSwitch);
    }


    void setUpUI(){

        FullName = findViewById(R.id.textViewFullName_Displayed_MPP_1);
        Age = findViewById(R.id.textView_Age_Displayed_MPP_1);
        Weight = findViewById(R.id.textView_Weight_Displayed_MPP_1);
        Height = findViewById(R.id.textView_Height_Displayed_MPP_1);

        ProfilePicture = findViewById(R.id.imageView_ProfilePicture_MPP_1);
        Heart = findViewById(R.id.imageView_Heart_MPP_1);
        Earth = findViewById(R.id.imageView_Earth_MPP_1);
        Battery = findViewById(R.id.imageView_BatteryLevel_MPP_1);
        locationSwitch = findViewById(R.id.locationSwitch);

        //TODO fetch the boolean location data from the database
        //location =

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.patient_action_bar,menu);
        Setting = findViewById(R.id.settings);

        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");
        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
                                   @Override
                                   public void onCallback(FirebaseObjects.UserDBO user) {
                                       thisUser = user;

                                       if (thisUser.getAcc_type()) {
                                           menu.getItem(0).setVisible(false);
                                       } else {
                                           menu.getItem(0).setVisible(true);
                                       }
                                   }
                               }, mSharedPreferencesHelper_Login.getEmail(),
                Boolean.parseBoolean(mSharedPreferencesHelper_Login.getType()));
        return super.onCreateOptionsMenu(menu);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {// User chose the "Settings" item, show the app settings UI...
            goToSettings();
        }// If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }
    public void goToSettings(){
        Intent intent = new Intent(this, PatientSettingsActivity.class);
        intent.putExtra(getString(R.string.PC_ID), "0");
        startActivity(intent);
    }
    public void goToLocation(View view){
        Intent intent = new Intent(this, MapsLocationActivity.class);
        intent.putExtra(getString(R.string.PC_ID), "0");
        startActivity(intent);
    }


    public void resumeSettings(){
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");
        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
                                   @Override
                                   public void onCallback(FirebaseObjects.UserDBO user) {
                                       thisUser = user;

                                       if (thisUser.getAcc_type()) {
                                           disableMenu();
                                       } else {
                                           displayMenu();
                                       }
                                   }
                               }, mSharedPreferencesHelper_Login.getEmail(),
                Boolean.parseBoolean(mSharedPreferencesHelper_Login.getType()));

    }
    public void resumeSharedLocation(){
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");
        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
                                   @Override
                                   public void onCallback(FirebaseObjects.UserDBO user) {
                                       thisUser = user;

                                       if (thisUser.getAcc_type()) {
                                           disableShareLocation();
                                       } else {
                                           displayShareLocation();
                                       }
                                   }
                               }, mSharedPreferencesHelper_Login.getEmail(),
                Boolean.parseBoolean(mSharedPreferencesHelper_Login.getType()));

    }

    public void resumeLocation(final Switch ls){
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");
        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
                                   @Override
                                   public void onCallback(FirebaseObjects.UserDBO user) {
                                       thisUser = user;
                                       ls.setChecked(thisUser.getGps_follow());
                                       if (thisUser.getGps_follow()) {
                                           firebaseHelper.editUser(thisUser, FirebaseObjects.GPS_Follow, true);
                                           displayLocation();
                                       } else {
                                           firebaseHelper.editUser(thisUser, FirebaseObjects.GPS_Follow, false);
                                           disableLocation();
                                       }
                                   }
                               }, mSharedPreferencesHelper_Login.getEmail(),
                Boolean.parseBoolean(mSharedPreferencesHelper_Login.getType()));

    }


    public void locationStatus (Switch b){


        b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firebaseHelper.editUser(thisUser, FirebaseObjects.GPS_Follow, true);
                    displayLocation();
                } else {
                    firebaseHelper.editUser(thisUser, FirebaseObjects.GPS_Follow, false);
                    disableLocation();
                }
            }
        });
    }

    public void displayLocation(){
        Earth.setVisibility(View.VISIBLE);
    }
    public void disableLocation(){
        Earth.setVisibility(View.INVISIBLE);
    }
    public void displayMenu(){
        Setting.setVisible(true);
    }
    public void disableMenu(){
        Setting.setVisible(false);
    }
    public void displayShareLocation(){
        locationSwitch.setVisibility(View.VISIBLE);
    }
    public void disableShareLocation(){
        locationSwitch.setVisibility(View.INVISIBLE);
    }


}
