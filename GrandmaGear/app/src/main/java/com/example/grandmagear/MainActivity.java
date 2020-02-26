package com.example.grandmagear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.grandmagear.ui.login.ClientLogin;

public class MainActivity extends AppCompatActivity {

    protected Button mPatientButton;
    protected Button mClientButton;
    protected ImageView mLogo;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializePage();

    }

    protected void initializePage(){
        mPatientButton = findViewById(R.id.patient_button);
        mClientButton = findViewById(R.id.client_button);
        mLogo = findViewById(R.id.app_logo);
        mLogo.setImageResource(R.drawable.sooken);

        mPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PatientActivity.class);
                startActivity(intent);
            }
        });

        mClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ClientLogin.class);
                startActivity(intent);
            }
        });
    }
}
