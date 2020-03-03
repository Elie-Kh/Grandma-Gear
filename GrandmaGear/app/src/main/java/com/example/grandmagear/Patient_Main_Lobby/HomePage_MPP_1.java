package com.example.grandmagear.Patient_Main_Lobby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grandmagear.R;

public class HomePage_MPP_1 extends AppCompatActivity {

    TextView FullName;
    TextView Age;
    TextView Weight;
    TextView Height;

    ImageView ProfilePicture;
    ImageView Heart;
    ImageView Battery;
    ImageView Earth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page__mpp_1);

        setUpUI();




    }



    void setUpUI(){

        FullName = findViewById(R.id.textViewFullName_Displayed_MPP_1);
        Age = findViewById(R.id.textView_Age_Displayed_MPP_1);
        Weight = findViewById(R.id.textView_Weight_Displayed_MPP_1);
        Height = findViewById(R.id.textView_Height_Displayed_MPP_1);

        ProfilePicture = findViewById(R.id.imageView_ProfilePicture_MPP_1);
        Heart = findViewById(R.id.imageView_Heart_MPP_1);
        Earth = findViewById(R.id.imageView_Earth_MPP_1);
        Battery = findViewById(R.id.imageView_BatteryLevel_MPP_1);


    }
    public void goToHeartRate_MPP_1_1(View view){

        Intent intent = new Intent(this, HeartRate_MPP_1_1.class);
        intent.putExtra(getString(R.string.PC_ID), "0");
        startActivity(intent);

    }
    public void goToLocation_MPP_1_2(View view){

        Intent intent = new Intent(this, Location_MPP_1_2.class);
        intent.putExtra(getString(R.string.PC_ID), "0");
        startActivity(intent);

    }
    public void goToBatteryLevel_MPP_1_3(View view){

        Intent intent = new Intent(this, BatteryLevel_MPP_1_3.class);
        intent.putExtra(getString(R.string.PC_ID), "0");
        startActivity(intent);

    }



}
