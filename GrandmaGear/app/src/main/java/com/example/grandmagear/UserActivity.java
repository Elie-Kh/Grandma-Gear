package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity {
    protected MenuItem mLogout;
    protected FirebaseAuth firebaseAuth;
    protected Button mSettings;
    protected FloatingActionButton mAddPatient;
    protected PatientsTabFragment mPatientsTabFragment =  new PatientsTabFragment();
    protected TabsAdapter mTabsAdapter;
    protected ViewPager mViewPager;
    public static final int BEHAVIOR_SET_USER_VISIBLE_HINT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setupUI();
    }

    void setupUI(){
        firebaseAuth = FirebaseAuth.getInstance();

        mViewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        mSettings = findViewById(R.id.client_settings_button);
        mAddPatient = findViewById(R.id.add_patient_button);

        mTabsAdapter = new TabsAdapter(getSupportFragmentManager(),
                BEHAVIOR_SET_USER_VISIBLE_HINT, tabLayout);
        mTabsAdapter.addFragment(mPatientsTabFragment, "Patients");
        mTabsAdapter.addFragment(new NotificationsTabFragment(), "Notifications");
        mTabsAdapter.addFragment(new ReportsTabFragment(), "Reports");
        mViewPager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPatientFragment patientFrag = new AddPatientFragment();
                patientFrag.show(getSupportFragmentManager(), "AddPatientFragment");
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
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_action_bar,menu);
        mLogout = findViewById(R.id.logoutAction);
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
