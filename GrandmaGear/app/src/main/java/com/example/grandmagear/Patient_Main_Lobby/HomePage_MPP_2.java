package com.example.grandmagear.Patient_Main_Lobby;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grandmagear.BTFragment;
import com.example.grandmagear.BTHelper;
import com.example.grandmagear.DisclaimerFragment;
import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;
import com.example.grandmagear.LogInActivity;
import com.example.grandmagear.R;
import com.example.grandmagear.SharedPreferencesHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.example.grandmagear.UserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.grandmagear.Patient_Main_Lobby.PatientSettingsActivity.location;

public class HomePage_MPP_2 extends AppCompatActivity {
    private static final String TAG = "HomePage_MPP_2";
    private Handler mainHandler = new Handler();




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


    String WearerID;
    String help = "no";
    Uri imageUri;
    private final static  int PICK_IMAGE = 1;
    private FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    SharedPreferencesHelper mSharedPreferencesHelper_Login;
    protected FirebaseObjects.UserDBO thisUser;
    protected FirebaseObjects.DevicesDBO device;


    protected BTHelper btHelper;

    SharedPreferencesHelper mSharedPreferencesHelper_BT;
    protected FirebaseObjects.DevicesDBO tempDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page__m_p_p_2);
        storage = FirebaseStorage.getInstance();
        setUpUI();
        Bundle b = getIntent().getExtras();
        WearerID = b.getString("wearerID");
        Log.d(TAG, WearerID);
        setProfilePicture();


        Earth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB).document(WearerID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        device = document.toObject(FirebaseObjects.DevicesDBO.class);
                        String ID = device.getId();
                        Log.d(TAG,ID);
                        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB).document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                thisUser = document.toObject(FirebaseObjects.UserDBO.class);
                                Boolean gps_follow = thisUser.getGpsFollow();

                                if(gps_follow){
                                    goToTrackWearerLocation();
                                }
                                else{
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which){
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    //Yes button clicked
                                                    //TODO Send the user a location request to turn the Wearer's Share location Switch to True

                                                    Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_LONG).show();
                                                    break;

                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    //Cancel button clicked
                                                    break;
                                            }
                                        }
                                    };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(HomePage_MPP_2.this);
                                    builder.setMessage("The wearer hasn't enable Shared Location. Would you like to send them a location request ?")
                                            .setPositiveButton("Yes", dialogClickListener)
                                            .setNegativeButton("Cancel", dialogClickListener).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        uploadWearerInfo();
        uploadDeviceInfo();
    }

    @Override
    protected void onPause() {

        super.onPause();

        if (mSharedPreferencesHelper_BT.getHC05() == null) {
            // goToDeviceList();
        } //else btConnect();
    }


    void setUpUI() {

        FullName = findViewById(R.id.textViewFullName_Displayed_MPP_2);
        Age = findViewById(R.id.textView_Age_Displayed_MPP_2);
        Weight = findViewById(R.id.textView_Weight_Displayed_MPP_2);
        Height = findViewById(R.id.textView_Height_Displayed_MPP_2);
        BPM = findViewById(R.id.textView_BPM_Displayed_MPP_2);
        phoneBattery = findViewById(R.id.textView_PhoneBatteryLevel_Displayed_MPP_2);
        //deviceBattery = findViewById(R.id.textView_BraceletBatteryLevel_Displayed_MPP_2);
        reportButton = findViewById(R.id.buttonReports_MPP_2);

        ProfilePicture = findViewById(R.id.imageView_ProfilePicture_MPP_2);
        Heart = findViewById(R.id.imageView_Heart_MPP_2);
        Earth = findViewById(R.id.imageView_Earth_MPP_2);
        Battery = findViewById(R.id.imageView_BatteryLevel_MPP_2);





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




    public void goToTrackWearerLocation (){
        Intent intent = new Intent(this, MapsTrackingActivity.class);
        intent.putExtra("wearerID_MPP_2", WearerID);
        startActivity(intent);
    }

    public void goToDeviceList () {
        BTFragment btFragment = new BTFragment(thisUser);
        btFragment.setCancelable(false);
        btFragment.show(getSupportFragmentManager(), "BTFragment");
    }





    public void uploadWearerInfo(){
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(HomePage_MPP_2.this, "Login");
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB).document(WearerID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null && documentSnapshot.exists()){
                    device = documentSnapshot.toObject(FirebaseObjects.DevicesDBO.class);
                    BPM.setText(Integer.toString(device.getHeartrate()));
                    phoneBattery.setText(Integer.toString(device.getPhoneBattery()));
                    String ID = device.getId();
                    Log.d(TAG,ID);
                    firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB).document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            thisUser = document.toObject(FirebaseObjects.UserDBO.class);
                            FullName.setText(thisUser.getFirstName() + " " + thisUser.getLastName());
                            Age.setText(String.valueOf(thisUser.getAge()));
                            Weight.setText(String.valueOf(thisUser.getWeight()));
                            Height.setText(String.valueOf(thisUser.getHeight()));

                        }
                    });
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













    public void fetchProfilePicture(){
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(getApplicationContext(), "Login");
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB).document(WearerID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                device = document.toObject(FirebaseObjects.DevicesDBO.class);
                final String ID = device.getId();
                Log.d(TAG,ID);
                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB).document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        thisUser = document.toObject(FirebaseObjects.UserDBO.class);
                        Boolean gps_follow = thisUser.getGpsFollow();

                        if (thisUser.getImage()) {
                            FirebaseStorage storage = FirebaseStorage.getInstance();

                            // Get image location

                            Intent t = getIntent();
                            Bundle b = t.getExtras();
                            String filename = "gs://grandma-gear.appspot.com/WearerProfilePicture/" + ID;
                            StorageReference gsReference = storage.getReferenceFromUrl(filename);


                            final long ONE_MEGABYTE = 1024 * 1024;
                            gsReference.getBytes(ONE_MEGABYTE*4).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    storeProfilePicture(bmp);
                                    ProfilePicture.setImageBitmap(bmp);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        } else {
                            ProfilePicture.setImageResource(R.drawable.sooken);
                        }
                    }
                });
            }

        });
    }


    public void offLineProfilePicture(File file) {
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        ProfilePicture.setImageBitmap(myBitmap);
    }
    public void storeProfilePicture(Bitmap bitmap){
        FileOutputStream outStream = null;

        // Write to SD Card

        File file = new File(getApplicationContext().getFilesDir(), WearerID);
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private boolean isOnline()
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }


    public boolean fileExists(Context context, File file) {

        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }
    public void setProfilePicture() {
        File file = new File(getApplicationContext().getFilesDir(), WearerID);
        if (isOnline()){
            fetchProfilePicture();
        }
        else if (fileExists(this, file)){
            offLineProfilePicture(file);
            Log.d(TAG, "2nd else statement activated");
        }
        else {
            ProfilePicture.setImageResource(R.drawable.sooken);
            Log.d(TAG, "Last else statement activated");

        }

    }




}



