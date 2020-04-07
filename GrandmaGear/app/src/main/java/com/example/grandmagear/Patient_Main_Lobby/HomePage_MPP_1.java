package com.example.grandmagear.Patient_Main_Lobby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;
import com.example.grandmagear.R;
import com.example.grandmagear.SharedPreferencesHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class HomePage_MPP_1 extends AppCompatActivity {
    private static final String TAG = "HomePage_MPP_1";
    private Handler mainHandler = new Handler();


    MenuItem Setting;
    Switch locationSwitch;
    TextView FullName;
    TextView Age;
    TextView Weight;
    TextView Height;
    TextView BPM;
    TextView phoneBattery;
    TextView deviceBattery;

    ImageView ProfilePicture;
    ImageView Heart;
    ImageView Battery;
    ImageView Earth;

    FirebaseHelper firebaseHelper = new FirebaseHelper();
    SharedPreferencesHelper mSharedPreferencesHelper_Login;
    protected FirebaseObjects.UserDBO thisUser;
    protected FirebaseObjects.DevicesDBO tempDevice;



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
            uploadWearerInfo();
            uploadDeviceInfo();
            resumeLocation(locationSwitch);
            locationStatus(locationSwitch);
    }


    void setUpUI(){

        FullName = findViewById(R.id.textViewFullName_Displayed_MPP_1);
        Age = findViewById(R.id.textView_Age_Displayed_MPP_1);
        Weight = findViewById(R.id.textView_Weight_Displayed_MPP_1);
        Height = findViewById(R.id.textView_Height_Displayed_MPP_1);
        BPM = findViewById(R.id.textView_BPM_Displayed);
        phoneBattery = findViewById(R.id.textView_PhoneBatteryLevel_Displayed);
        deviceBattery = findViewById(R.id.textView_BraceletBatteryLevel_Displayed);

        ProfilePicture = findViewById(R.id.imageView_ProfilePicture_MPP_1);
        Heart = findViewById(R.id.imageView_Heart_MPP_1);
        Earth = findViewById(R.id.imageView_Earth_MPP_1);
        Battery = findViewById(R.id.imageView_BatteryLevel_MPP_1);
        locationSwitch = findViewById(R.id.locationSwitch);


    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.patient_action_bar,menu);
        Setting = findViewById(R.id.settings);

        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                .document(firebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        thisUser = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);
                        if (thisUser.getAccountType()) {
                            menu.getItem(0).setVisible(false);
                        } else {
                            menu.getItem(0).setVisible(true);
                        }
                    }
                });
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
       // Intent intent = new Intent(this, MapsLocationActivity.class);
        //intent.putExtra(getString(R.string.PC_ID), "0");
        //startActivity(intent);
    }


    public void uploadWearerInfo(){
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                .document(firebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        thisUser = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);
                        FullName.setText(thisUser.getFirstName() + " " + thisUser.getLastName());
                        Age.setText(String.valueOf(thisUser.getAge()));
                        Weight.setText(String.valueOf(thisUser.getWeight()));
                        Height.setText(String.valueOf(thisUser.getHeight()));
                    }
                });

    }
    public void resumeSharedLocation(){
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                .document(firebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        thisUser = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);
                        if (thisUser.getAccountType()) {
                            disableShareLocation();
                        } else {
                            displayShareLocation();
                        }
                    }
                });
    }

    public void resumeLocation(final Switch ls){
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                .document(firebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        thisUser = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);
                        ls.setChecked(thisUser.getGpsFollow());
                        if (thisUser.getGpsFollow()) {
                            firebaseHelper.editUser(thisUser, FirebaseObjects.GPS_Follow, true);
                            displayLocation();
                        } else {
                            firebaseHelper.editUser(thisUser, FirebaseObjects.GPS_Follow, false);
                            disableLocation();
                        }
                    }
                });

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

    public void uploadDeviceInfo(){

        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");

        //TODO optimize the fetching method for universal input
        firebaseHelper.getDevice(new FirebaseHelper.Callback_Device() {
            @Override
            public void onCallback(FirebaseObjects.DevicesDBO device) {
                tempDevice = device;
                BPM.setText(String.valueOf(tempDevice.getHeartrate()));
                //TODO find where it got interchanged
                deviceBattery.setText(String.valueOf(tempDevice.getPhoneBattery()));
                phoneBattery.setText(String.valueOf(tempDevice.getDeviceBattery()));
            }
        }, firebaseHelper.getCurrentUserID());







    }

    public void displayLocation(){
        Earth.setVisibility(View.VISIBLE);
    }
    public void disableLocation(){
        Earth.setVisibility(View.INVISIBLE);
    }

    public void displayShareLocation(){
        locationSwitch.setVisibility(View.VISIBLE);
    }
    public void disableShareLocation(){
        locationSwitch.setVisibility(View.INVISIBLE);
    }


}
