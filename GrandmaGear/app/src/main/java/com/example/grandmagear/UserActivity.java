package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.auth.User;

public class UserActivity extends AppCompatActivity {
    protected MenuItem mLogout;
    protected FloatingActionButton mAddPatient;
    protected PatientsTabFragment mPatientsTabFragment =  new PatientsTabFragment();
    protected NotificationsTabFragment mNotificationTabFragment = new NotificationsTabFragment();
    protected TabsAdapter mTabsAdapter;
    protected ViewPager mViewPager;
    protected TabLayout tabLayout;
    protected FirebaseObjects.UserDBO thisUser;
    protected FirebaseHelper firebaseHelper;
    protected NotificationHelper notificationHelper;
    SharedPreferencesHelper mSharedPreferencesHelper;
    SharedPreferencesHelper mSharedPreferencesHelper_Login;
    public static final int BEHAVIOR_SET_USER_VISIBLE_HINT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setupUI();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    void setupUI(){
        firebaseHelper = new FirebaseHelper();
        mSharedPreferencesHelper = new SharedPreferencesHelper(UserActivity.this,
                "PatientIDs");
        mSharedPreferencesHelper_Login = new SharedPreferencesHelper(UserActivity.this,
                "Login");
        Log.d("__THISTAG__", String.valueOf(Boolean.parseBoolean(mSharedPreferencesHelper_Login.getType())));
        Log.d("__THISTAG__", mSharedPreferencesHelper_Login.getEmail());

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
        mViewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);

        mTabsAdapter = new TabsAdapter(getSupportFragmentManager(),
                BEHAVIOR_SET_USER_VISIBLE_HINT, tabLayout);
        mTabsAdapter.addFragment(mPatientsTabFragment, "Patients");
        mTabsAdapter.addFragment(mNotificationTabFragment, "Reports");
        //mTabsAdapter.addFragment(new ReportsTabFragment(), "Reports");
        mViewPager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        //mSettings = findViewById(R.id.client_settings_button);
        mAddPatient = findViewById(R.id.add_patient_button);
        mAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                //thisUser.setDevice_ids(mPatientsTabFragment.getmPatientsList());
                AddPatientFragment patientFrag = new AddPatientFragment(thisUser);
                patientFrag.show(getSupportFragmentManager(), "AddPatientFragment");
            }
        });


        

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position!=0){
                    mAddPatient.setVisibility(View.INVISIBLE);
                }
                else {
                    mAddPatient.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseHelper.firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseHelper.firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        }
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
            FirebaseHelper.firebaseFirestore.clearPersistence();
            FirebaseHelper.firebaseFirestore.terminate();
        }// If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    public void updateUser(FirebaseObjects.UserDBO userDBO){
        thisUser = userDBO;
    }


}
