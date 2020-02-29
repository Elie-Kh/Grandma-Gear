package com.example.grandmagear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.auth.User;

public class AddPatientFragment extends DialogFragment {

    protected EditText mDeviceId;
    protected Button mAdd;
    protected Button mCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_patient_fragment, container,false);
        layoutSetup(view);

        return view;
    }

    protected void layoutSetup(View view){
        mDeviceId = view.findViewById(R.id.device_id_adding_fragment);
        mAdd = view.findViewById(R.id.add_device_adding_fragment);
        mCancel = view.findViewById(R.id.cancel_device_adding_fragment);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*TODO: Check if Device in Database.
                * TODO: If it is, get information from database and create new patient device.
                * TODO: replace userExists below with the actual check from database.
                *
                * if(userExists) {
                *   PatientDevice patientDevice = new PatientDevice();
                *   ((UserActivity) getActivity()).mPatientsTab.mPatientsList.add(patientDevice);
                * }
                */
                getDialog().dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }
}
