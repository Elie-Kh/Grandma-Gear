package com.example.grandmagear;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<PatientDevice> mPatients;

    public RecyclerViewAdapter(ArrayList<PatientDevice> patients) {
        mPatients = patients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        protected PatientDevice patient;
        protected Button mLocationButton;
        protected EditText mHeartBeatText;
        protected TextView mDeviceIdText;
        protected ImageView mPatientImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLocationButton = itemView.findViewById(R.id.location_button);
            mHeartBeatText = itemView.findViewById(R.id.heart_beat_text);
            mHeartBeatText.setText(patient.mHeartBeat);
            mDeviceIdText = itemView.findViewById(R.id.device_id);
            mDeviceIdText.setText(patient.mDeviceId);
            mPatientImage = itemView.findViewById(R.id.patient_image);

            mLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to GPS page.

                }
            });

        }
    }
}


