package com.example.grandmagear;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PatientDevice {
    protected Button mLocation;
    protected EditText mHeartBeat;
    protected TextView mDeviceId;
    protected ImageView mPatientImage;

    public PatientDevice(Button mLocation, EditText mHeartBeat, TextView mDeviceId, ImageView mPatientImage) {
        this.mLocation = mLocation;
        this.mHeartBeat = mHeartBeat;
        this.mDeviceId = mDeviceId;
        this.mPatientImage = mPatientImage;
    }

    public Button getmLocation() {
        return mLocation;
    }

    public EditText getmHeartBeat() {
        return mHeartBeat;
    }

    public TextView getmDeviceId() {
        return mDeviceId;
    }

    public ImageView getmPatientImage() {
        return mPatientImage;
    }
}
