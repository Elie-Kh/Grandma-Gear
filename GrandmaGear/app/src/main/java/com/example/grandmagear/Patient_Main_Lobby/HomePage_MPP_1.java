package com.example.grandmagear.Patient_Main_Lobby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.grandmagear.BTFragment;
import com.example.grandmagear.BTHelper;
import com.example.grandmagear.DisclaimerFragment;
import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;
import com.example.grandmagear.LogInActivity;
import com.example.grandmagear.R;
import com.example.grandmagear.SharedPreferencesHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.example.grandmagear.UserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.grandmagear.Patient_Main_Lobby.PatientSettingsActivity.location;

public class HomePage_MPP_1 extends AppCompatActivity {
    private static final String TAG = "HomePage_MPP_1";
    private Handler mainHandler = new Handler();


    MenuItem Setting;
    MenuItem Logout;
    MenuItem DeviceSelect;
    Switch locationSwitch;
    TextView FullName;
    TextView Age;
    TextView Weight;
    TextView Height;
    TextView BPM;
    TextView phoneBattery;
    TextView deviceBattery;
    Button reportButton;

    ImageView ProfilePicture;
    ImageView Heart;
    ImageView Battery;
    ImageView Earth;

    Button PanicButton;
    String deviceID;
    String help = "no";

    FirebaseHelper firebaseHelper = new FirebaseHelper();
    SharedPreferencesHelper mSharedPreferencesHelper_Login;
    protected FirebaseObjects.UserDBO thisUser;
    protected BatteryReceiver batteryReceiver = new BatteryReceiver();
    protected IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    int batLevel;
    protected BTHelper btHelper;

    SharedPreferencesHelper mSharedPreferencesHelper_BT;
    protected FirebaseObjects.DevicesDBO tempDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page__mpp_1);

        setUpUI();
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(batteryReceiver, intentFilter);
        resumeSharedLocation();
        uploadWearerInfo();
        uploadDeviceInfo();
        resumeLocation(locationSwitch);
        locationStatus(locationSwitch);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(batteryReceiver);
        super.onPause();

        if (mSharedPreferencesHelper_BT.getHC05() == null) {
           // goToDeviceList();
        } //else btConnect();
    }


    void setUpUI() {

        FullName = findViewById(R.id.textViewFullName_Displayed_MPP_1);
        Age = findViewById(R.id.textView_Age_Displayed_MPP_1);
        Weight = findViewById(R.id.textView_Weight_Displayed_MPP_1);
        Height = findViewById(R.id.textView_Height_Displayed_MPP_1);
        BPM = findViewById(R.id.textView_BPM_Displayed);
        BPM.setText("66");
        phoneBattery = findViewById(R.id.textView_PhoneBatteryLevel_Displayed);
        deviceBattery = findViewById(R.id.textView_BraceletBatteryLevel_Displayed);
        reportButton = findViewById(R.id.buttonReports);

        ProfilePicture = findViewById(R.id.imageView_ProfilePicture_MPP_1);
        Heart = findViewById(R.id.imageView_Heart_MPP_1);
        Earth = findViewById(R.id.imageView_Earth_MPP_1);
        Battery = findViewById(R.id.imageView_BatteryLevel_MPP_1);
        locationSwitch = findViewById(R.id.locationSwitch);
        PanicButton = findViewById(R.id.buttonPanic);

        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        PanicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help = "yes";
                uploadDeviceInfo();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        help = "no";
                        uploadDeviceInfo();
                    }
                }, 3000);
            }
        });

        mSharedPreferencesHelper_BT = new SharedPreferencesHelper(this, "BTList");



        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReportsActivity.class));
            }
        });
    }



    public void btConnect(){
        btHelper = new BTHelper(this, mSharedPreferencesHelper_BT.getHC05(), thisUser);
        btHelper.btEnable(this);
        btHelper.estConnect();
        btHelper.content(BPM);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.patient_action_bar,menu);
        Setting = findViewById(R.id.settings);
        Logout = findViewById(R.id.logout);

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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
        DeviceSelect = findViewById(R.id.btDeviceSelect);

        return super.onCreateOptionsMenu(menu);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {// User chose the "Settings" item, show the app settings UI...
            goToSettings();
        }
        if (item.getItemId() == R.id.logout) {
            logout();
            FirebaseHelper.firebaseFirestore.clearPersistence();
            FirebaseHelper.firebaseFirestore.terminate();
        }

        if (item.getItemId() == R.id.btDeviceSelect) {
            goToDeviceList();
        }
        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    public void goToSettings () {
        Intent intent = new Intent(this, PatientSettingsActivity.class);
        intent.putExtra(getString(R.string.PC_ID), "0");
        startActivity(intent);
    }
    public void goToLocation (View view){
        // Intent intent = new Intent(this, MapsLocationActivity.class);
        //intent.putExtra(getString(R.string.PC_ID), "0");
        //startActivity(intent);
    }

    public void goToDeviceList () {
        BTFragment btFragment = new BTFragment(thisUser);
        btFragment.setCancelable(false);
        btFragment.show(getSupportFragmentManager(), "BTFragment");
    }



    public void resumeSharedLocation() {
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");
        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
                                   @Override
                                   public void onCallback(FirebaseObjects.UserDBO user) {
                                       thisUser = user;

                                       if (thisUser.getAccountType()) {
                                           disableShareLocation();
                                       } else {
                                           displayShareLocation();
                                       }
                                   }
                               }, mSharedPreferencesHelper_Login.getEmail(),
                Boolean.parseBoolean(mSharedPreferencesHelper_Login.getType()));
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





    public void resumeLocation(final Switch ls){
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_1.this, "Login");
        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
                                   @Override
                                   public void onCallback(FirebaseObjects.UserDBO user) {
                                       thisUser = user;
                                       ls.setChecked(thisUser.getGpsFollow());
                                       if (thisUser.getGpsFollow()) {
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

    public void uploadDeviceInfo(){

        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                .whereEqualTo(FirebaseObjects.ID, firebaseHelper.getCurrentUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                Log.d(TAG, "docsnap " + documentSnapshot.getReference().getId());
                                Log.d(TAG, firebaseHelper.getCurrentUserID());
                                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                        .document(documentSnapshot.getReference().getId())
                                        .update(FirebaseObjects.Heartrate, Integer.parseInt(BPM.getText().toString()));
                                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                        .document(documentSnapshot.getReference().getId())
                                        .update(FirebaseObjects.Fall, "fall");
                                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                        .document(documentSnapshot.getReference().getId())
                                        .update(FirebaseObjects.DeviceOn, "on");
                                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                        .document(documentSnapshot.getReference().getId())
                                        .update(FirebaseObjects.Help, help);
                            }
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

    public void displayShareLocation(){
        locationSwitch.setVisibility(View.VISIBLE);
    }
    public void disableShareLocation(){
        locationSwitch.setVisibility(View.INVISIBLE);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        finish();
    }


}
