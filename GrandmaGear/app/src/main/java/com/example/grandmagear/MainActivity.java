package com.example.grandmagear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.grandmagear.ui.login.ClientLogin;

public class MainActivity extends AppCompatActivity {

    protected Button mPatientButton;
    protected Button mClientButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeButtons();

    }

    protected void initializeButtons(){
        mPatientButton = findViewById(R.id.patient_button);
        mClientButton = findViewById(R.id.client_button);

        mPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
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
