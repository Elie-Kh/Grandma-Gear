package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    protected MenuItem mLogout;
    protected FirebaseAuth firebaseAuth;
    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mAdapter;
    protected Button mPatients;
    protected Button mNotifications;
    protected Button mReports;
    protected Button mSettings;
    protected FloatingActionButton mAddPatient;
    protected ArrayList<PatientDevice> mPatientsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setupUI();
    }

    void setupUI(){
        firebaseAuth = FirebaseAuth.getInstance();

        mPatients = findViewById(R.id.patients_view_button);
        mNotifications = findViewById(R.id.notifications_button);
        mReports = findViewById(R.id.reports_button);
        mSettings = findViewById(R.id.client_settings_button);
        mAddPatient = findViewById(R.id.add_patient_button);

        mAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPatientFragment patientFrag = new AddPatientFragment();
                patientFrag.show(getSupportFragmentManager(), "AddPatientFragment");
            }
        });

        mRecyclerView = findViewById(R.id.device_recycler);
        mAdapter = new RecyclerViewAdapter(mPatientsList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
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
