package com.example.grandmagear.Patient_Main_Lobby;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import android.app.DownloadManager;
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
import com.google.firebase.firestore.FirebaseFirestore;
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

    Button reportButton;

    ImageView ProfilePicture;
    ImageView Heart;
    ImageView Earth;

    Button PanicButton;
    String deviceID;
    String help = "no";
    Uri imageUri;
    private final static  int PICK_IMAGE = 1;
    private FirebaseStorage storage;
    StorageReference storageReference;
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
        storage = FirebaseStorage.getInstance();
        setUpUI();
        try {
            setProfilePicture();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()){
                    profilePictureUploadingStartUp();
                }
            }
        });
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

    }


    void setUpUI() {

        FullName = findViewById(R.id.textViewFullName_Displayed_MPP_1);
        Age = findViewById(R.id.textView_Age_Displayed_MPP_1);
        Weight = findViewById(R.id.textView_Weight_Displayed_MPP_1);
        Height = findViewById(R.id.textView_Height_Displayed_MPP_1);
        BPM = findViewById(R.id.textView_BPM_Displayed);

        reportButton = findViewById(R.id.buttonReports);

        ProfilePicture = findViewById(R.id.imageView_ProfilePicture_MPP_1);
        Heart = findViewById(R.id.hrIcon);
        Earth = findViewById(R.id.imageView_Earth_MPP_1);

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

        btConnect();

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
        //Setting = findViewById(R.id.settings);
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
        /*if (item.getItemId() == R.id.settings) {// User chose the "Settings" item, show the app settings UI...
            goToSettings();
        }*/
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

    /*
    public void goToSettings () {
        Intent intent = new Intent(this, PatientSettingsActivity.class);
        intent.putExtra(getString(R.string.PC_ID), "0");
        startActivity(intent);
    }*/

    public void goToLocation (View view){
         Intent intent = new Intent(this, MapsLocationActivity.class);
        intent.putExtra(getString(R.string.PC_ID), "0");
        startActivity(intent);
    }

    public void goToDeviceList () {
        BTFragment btFragment = new BTFragment(thisUser,false);
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

    public void profilePictureUploadingStartUp(){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        uploadProfilePicture();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Would you like to upload a profile picture ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void uploadImage()
    {

        // Code for showing progressDialog while uploading
        final ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Defining the child of storageReference

        Intent t = getIntent();
        Bundle b = t.getExtras();
        String filename = "gs://grandma-gear.appspot.com/WearerProfilePicture/" + firebaseHelper.getCurrentUserID();
        StorageReference gsReference = storage.getReferenceFromUrl(filename);


        // adding listeners on upload
        // or failure of image
        gsReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {

                        // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                    // Progress Listener for loading
                    // percentage on the dialog box
                    @Override
                    public void onProgress(
                            UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress
                                = (100.0
                                * taskSnapshot.getBytesTransferred()
                                / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage(
                                "Uploaded "
                                        + (int)progress + "%");
                    }
                });

    }

    protected void uploadProfilePicture()
    {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                ProfilePicture.setImageBitmap(bitmap);

                FirebaseHelper.firebaseFirestore.collection(firebaseHelper.userDB).document(FirebaseHelper.firebaseAuth.getCurrentUser().getUid()).update("image",true);
                storeProfilePicture(bitmap);
                uploadImage();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

    }





    public void fetchProfilePicture(){
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(getApplicationContext(), "Login");
       /* firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
                                   @Override
                                   public void onCallback(FirebaseObjects.UserDBO user) {
                                       thisUser = user;
                                       if (thisUser.getImage()) {
                                           FirebaseStorage storage = FirebaseStorage.getInstance();

                                           // Get image location

                                           Intent t = getIntent();
                                           Bundle b = t.getExtras();
                                           String filename = "gs://grandma-gear.appspot.com/WearerProfilePicture/" + firebaseHelper.getCurrentUserID();
                                           StorageReference gsReference = storage.getReferenceFromUrl(filename);


                                           final long ONE_MEGABYTE = 1024 * 1024;
                                           gsReference.getBytes(ONE_MEGABYTE*4).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                               @Override
                                               public void onSuccess(byte[] bytes) {
                                                   Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
                               }, mSharedPreferencesHelper_Login.getEmail(),
                Boolean.parseBoolean(mSharedPreferencesHelper_Login.getType()));*/
    }
    public void offLineProfilePicture(File file) {
        if(file.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ProfilePicture.setImageBitmap(myBitmap);
        }
    }
    public void storeProfilePicture(Bitmap bitmap){
        FileOutputStream outStream = null;

        // Write to SD Card

        File file = new File(getApplicationContext().getFilesDir(), "GrandmaGearProfilePicture");
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
    public void setProfilePicture() throws PackageManager.NameNotFoundException {
        File file = new File(getApplicationContext().getFilesDir(), "GrandmaGearProfilePicture");
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



