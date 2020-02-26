package com.example.grandmagear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity {

    protected Button mlogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setupUI();
    }

    void setupUI(){
        mlogoutButton = findViewById(R.id.logoutButton);

        mlogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        finish();
    }
}
