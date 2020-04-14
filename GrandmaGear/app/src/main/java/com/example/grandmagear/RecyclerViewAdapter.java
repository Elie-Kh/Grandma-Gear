package com.example.grandmagear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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

import com.example.grandmagear.Patient_Main_Lobby.WearerDeleteFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "Adapter__";
    ArrayList<String> mPatients;
    private FirebaseHelper firebaseHelper;
    private static NotificationHelper notificationHelper;
    private static FirebaseObjects.UserDBO userDBO;
    private OnItemClickedListener onItemClickedListener;
    private Context context;
    //private boolean check = true;

    public RecyclerViewAdapter(ArrayList<String> patients, final OnItemClickedListener onItemClickedListener, final Context context) {
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
        this.context = context;
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_recycler_item,
                parent,false);
        view.setTag("recyclerView");
        return new ViewHolder(view, onItemClickedListener);
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
                            final FirebaseObjects.DevicesDBO device = documentSnapshot
                                    .toObject(FirebaseObjects.DevicesDBO.class);
                            Log.d(TAG, "logging");
                            holder.mHeartBeatText.setText((device.heartrate + "bpm"));
                            if ((Integer) device.heartrate < 60) {
                                holder.highBPM =0;
                                holder.lowBPM++;
                                holder.mHeartBeatText.setTextColor(Color.RED);

                                if(holder.lowBPM > 3) {

                                    notificationHelper.sendOnBpm("Low BPM", "A bpm of " + device.heartrate + " was recorded for", documentSnapshot.getId());
                                }
                                //check = false;
                            }else if ((Integer) device.heartrate >= 100) {
                                holder.highBPM++;
                                holder.lowBPM =0;
                                holder.mHeartBeatText.setTextColor(Color.RED);
                                if(holder.highBPM > 3){
                                    notificationHelper.sendOnBpm("High BPM", "A bpm of " + device.heartrate + " was recorded for", documentSnapshot.getId());
                                }
                                //check = true;
                            } else {
                                holder.highBPM =0;
                                holder.lowBPM =0;
                                holder.mHeartBeatText.setTextColor(Color.GREEN);
                            }

                            if(((String) device.helpRequested).equals("yes")){
                                notificationHelper.sendOnFall("Panic button pressed", "The panic button has been pressed by ", documentSnapshot.getId());
                            }


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
                                        if(device.getDeviceOn().equals("on")){
                                        if (patient.getImage()) {
                                            FirebaseStorage storage = FirebaseStorage.getInstance();

                                            // Get image location

                                            //Intent t = getIntent();
                                            //Bundle b = t.getExtras();
                                            String filename = "gs://grandma-gear.appspot.com/WearerProfilePicture/" + patient.getUsername();
                                            StorageReference gsReference = storage.getReferenceFromUrl(filename);


                                            final long ONE_MEGABYTE = 1024 * 1024;
                                            gsReference.getBytes(ONE_MEGABYTE*4).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {
                                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                    storeProfilePicture(bmp);
                                                    holder.mPatientImage.setImageBitmap(bmp);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle any errors
                                                }
                                            });
                                        } else {
                                            holder.mPatientImage.setImageResource(R.drawable.gg_default_pic);
                                        }
                                    }
                                        else {
                                                holder.mPatientImage.setImageResource(R.drawable.offfline_icon);

                                        }
                                    }
                                }
                            });
                            holder.mDeviceBattery.setText((device.deviceBattery + "%"));

                            //notifyItemChanged(position);
                        }

                    }
                });



        //Kizito, this is the code.

    }

    public void storeProfilePicture(Bitmap bitmap){
        FileOutputStream outStream = null;

        // Write to SD Card

        File file = new File(((UserActivity)context).getFilesDir(), "GrandmaGearProfilePicture");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public long getDeviceID(@NonNull final ViewHolder view, final int position){
        return Long.parseLong(view.mDeviceIdText.getText().toString());
    }

    @Override
    public int getItemCount() {
        return mPatients.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView mLocationButton;
        protected TextView mBatteryButton;
        protected TextView mHeartBeatText;
        protected TextView mDeviceIdText;
        protected ImageView mPatientImage;
        protected ImageView mHeartGraph;
        protected TextView mDeviceBattery;
        protected TextView mPatientName;
        protected ImageView wearerDelete;
        OnItemClickedListener onItemClickedListener;
        protected int highBPM = 0;
        protected int lowBPM = 0;


        public ViewHolder(@NonNull View itemView, OnItemClickedListener onItemClickedListener) {
            super(itemView);


            mLocationButton = itemView.findViewById(R.id.location_button);
            mHeartBeatText = itemView.findViewById(R.id.heart_beat_text);
            mDeviceIdText = itemView.findViewById(R.id.device_id);
            mPatientImage = itemView.findViewById(R.id.patient_image);
            mHeartGraph = itemView.findViewById(R.id.heartBeatImage);
            mBatteryButton = itemView.findViewById(R.id.battery_button);
            mDeviceBattery = itemView.findViewById(R.id.battery_level);
            mPatientName = itemView.findViewById(R.id.patient_name);
            wearerDelete = itemView.findViewById(R.id.wearerDeleteIcon);

//            mLocationButton.setClickable(false);
//            mBatteryButton.setClickable(false);
//            mHeartGraph.setClickable(false);

            wearerDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getWearerDeleteView(getAdapterPosition());
                }
            });

            this.onItemClickedListener = onItemClickedListener;
            itemView.setOnClickListener(this);

//            mLocationButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //go to GPS page.
//                }
//            });
//
//            mHeartGraph.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //go to heart graph
//                }
//            });
//
//            mBatteryButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //go to battery display level
//
//                }
//            });
        }

        @Override
        public void onClick(View view) {
            onItemClickedListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickedListener{
        void onItemClick(int position);
    }

    public void getWearerDeleteView(int position){
        WearerDeleteFragment wearerDeleteFragment = new WearerDeleteFragment(this, position);
        wearerDeleteFragment.setCancelable(false);
        wearerDeleteFragment.show(((UserActivity)context).getSupportFragmentManager(), "WearerDeleteFragment");
    }

    public void delete(final int position){
        mPatients.remove(position);
        notifyItemRemoved(position);
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                .document(FirebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                userDBO = task.getResult().toObject(FirebaseObjects.UserDBO.class);
                userDBO.devicesFollowed.remove(position);
                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                        .document(FirebaseHelper.firebaseAuth.getCurrentUser().getUid())
                        .update(FirebaseObjects.Devices_Followed, userDBO.devicesFollowed);
            }
        });
        notifyDataSetChanged();
    }
}


