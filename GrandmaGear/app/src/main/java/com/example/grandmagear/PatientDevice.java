package com.example.grandmagear;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PatientDevice extends AppCompatActivity {

    protected String mLocation;
    protected String mHeartBeat;
    protected String mDeviceId;
    protected String mPatient;


    public PatientDevice(String mLocation, String mHeartBeat, String mDeviceId, String mPatient)
                         {
        this.mLocation = mLocation;
        this.mHeartBeat = mHeartBeat;
        this.mDeviceId = mDeviceId;
        this.mPatient = mPatient;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmHeartBeat() {
        return mHeartBeat;
    }

    public String getmDeviceId() {
        return mDeviceId;
    }

    public String getmPatient() {
        return mPatient;
    }
}
