package com.example.grandmagear;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "Adapter__";
    ArrayList<String> mPatients;
    private FirebaseHelper firebaseHelper;
    private static NotificationHelper notificationHelper;
    private static FirebaseObjects.UserDBO userDBO;
    //private boolean check = true;

    public RecyclerViewAdapter(ArrayList<String> patients, final Context context) {
        FirebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB).document(FirebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot != null && documentSnapshot.exists()){
                    userDBO = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);
                    notificationHelper = new NotificationHelper(context, userDBO);
                }
            }
        });
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mDeviceIdText.setText(mPatients.get(position));

        firebaseHelper.firebaseFirestore
                .collection(FirebaseHelper.deviceDB)
                .document(mPatients.get(position))
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(documentSnapshot != null && documentSnapshot.exists()){
                            FirebaseObjects.DevicesDBO device = documentSnapshot
                                    .toObject(FirebaseObjects.DevicesDBO.class);
                            Log.d(TAG, "logging");
                                holder.mHeartBeatText.setText((device.heartrate + "bpm"));
                                //if (check) {
                                    if ((Integer) device.heartrate < 60) {
                                        notificationHelper.sendOnBpm("Low BPM", "A bpm of " + device.heartrate + " was recorded for");
                                        //check = false;
                                    }
                                //} else {
                                    if ((Integer) device.heartrate >= 60) {
                                        //check = true;
                                    }
                                //}
                                FirebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                                        .document(device.id)
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot snapshot = task.getResult();
                                        if (snapshot != null && snapshot.exists()) {
                                            FirebaseObjects.UserDBO patient = snapshot
                                                    .toObject(FirebaseObjects.UserDBO.class);
                                            String name = (String) patient.firstName + " " + (String) patient.lastName;
                                            holder.mPatientName.setText(name);
                                        }
                                    }
                                });
                                holder.mDeviceBattery.setText((device.deviceBattery + "%"));
                                //notifyItemChanged(position);
                            }

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mPatients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected Button mLocationButton;
        protected Button mBatteryButton;
        protected TextView mHeartBeatText;
        protected TextView mDeviceIdText;
        protected ImageView mPatientImage;
        protected ImageView mHeartGraph;
        protected TextView mDeviceBattery;
        protected TextView mPatientName;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLocationButton = itemView.findViewById(R.id.location_button);
            mHeartBeatText = itemView.findViewById(R.id.heart_beat_text);
            mDeviceIdText = itemView.findViewById(R.id.device_id);
            mPatientImage = itemView.findViewById(R.id.patient_image);
            mHeartGraph = itemView.findViewById(R.id.heartBeatImage);
            mBatteryButton = itemView.findViewById(R.id.battery_button);
            mDeviceBattery = itemView.findViewById(R.id.battery_level);
            mPatientName = itemView.findViewById(R.id.patient_name);

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

            mBatteryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to battery display level
                }
            });
        }
    }
}


