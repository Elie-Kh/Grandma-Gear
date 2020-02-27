package com.example.grandmagear;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


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
        protected Button mLocation;
        protected EditText mHeartBeat;
        protected TextView mDeviceId;
        protected ImageView mPatientImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLocation = itemView.findViewById(R.id.location_button);
            mHeartBeat = itemView.findViewById(R.id.heart_beat_text);
            mDeviceId = itemView.findViewById(R.id.device_id);
            mPatientImage = itemView.findViewById(R.id.patient_image);

            mLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to GPS page.
                }
            });
        }
    }
}


