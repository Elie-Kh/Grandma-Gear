package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.grandmagear.Patient_Main_Lobby.HomePage_MPP_1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class PatientActivity extends AppCompatActivity {

    protected Button mHelpButton;
    protected MenuItem mLogout;
    protected Button mOKButton;
    protected EditText mDeviceID;
    protected ImageView mLogo;
    protected FirebaseObjects.UserDBO thisUser;
    protected FirebaseHelper firebaseHelper;
    SharedPreferencesHelper mSharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        initializePage();
    }

    @Override
    public void onBackPressed() {
        return;
    }



    protected void initializePage(){
        mHelpButton = findViewById(R.id.help_button);
        mOKButton = findViewById(R.id.ok_button);
        mDeviceID = findViewById(R.id.device_id_edit_text);
        mLogo = findViewById(R.id.app_logo);
        mLogo.setImageResource(R.drawable.gg_default_pic);
        mSharedPreferencesHelper = new SharedPreferencesHelper(PatientActivity.this,
                "Login");
        firebaseHelper = new FirebaseHelper();
        Log.d("__THISTAG__", String.valueOf(Boolean.parseBoolean(mSharedPreferencesHelper.getType())));
        Log.d("__THISTAG__", mSharedPreferencesHelper.getEmail());
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                .document(firebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        thisUser = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);
                    }
                });

        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpFragment helpFragment = new HelpFragment();
                helpFragment.show(getSupportFragmentManager(),"HelpFragment");
            }
        });

        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceID = mDeviceID.getText().toString();
                if(deviceID.trim().length() < 5){
                    Toast.makeText(PatientActivity.this, "Invalid ID",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    //Check if device ID is in database. If not, create it.
                    goToHomePage_MPP_1();
                }
            }
        });


    }

    public void goToHomePage_MPP_1(){

        Intent intent = new Intent(this, HomePage_MPP_1.class);
        intent.putExtra(getString(R.string.PC_ID), "0");
        startActivity(intent);

    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_action_bar,menu);
        mLogout = findViewById(R.id.logoutAction);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutAction) {// User chose the "Settings" item, show the app settings UI...
            logout();
        }// If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

}
