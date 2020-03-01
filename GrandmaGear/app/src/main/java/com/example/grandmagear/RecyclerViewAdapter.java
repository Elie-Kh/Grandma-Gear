package com.example.grandmagear;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "Adapter__";
    ArrayList<PatientDevice> mPatients;

    public RecyclerViewAdapter(ArrayList<PatientDevice> patients) {
        this.mPatients = patients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_recycler_item,
                parent,false);
        view.setTag("recyclerView");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mHeartBeatText.setText(mPatients.get(position).mHeartBeat);
        holder.mDeviceIdText.setText(mPatients.get(position).mDeviceId);
    }

    @Override
    public int getItemCount() {
        return mPatients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected Button mLocationButton;
        protected EditText mHeartBeatText;
        protected TextView mDeviceIdText;
        protected ImageView mPatientImage;
        protected ImageView mHeartGraph;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLocationButton = itemView.findViewById(R.id.location_button);
            mHeartBeatText = itemView.findViewById(R.id.heart_beat_text);
            mDeviceIdText = itemView.findViewById(R.id.device_id);
            mPatientImage = itemView.findViewById(R.id.patient_image);
            mHeartGraph = itemView.findViewById(R.id.heartBeatImage);


            mLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to GPS page.

                }
            });

            mHeartGraph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to heart graph
                }
            });



        }
    }
}


