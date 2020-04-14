package com.example.grandmagear.Patient_Main_Lobby;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LocationService extends BroadcastReceiver {

    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    public static final String ACTION_PROCESS_UPDATES = "com.example.grandmagear.Patient_Main_Lobby.UPDATE_LOCATION";
    public LocationService() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATES.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if(result != null){
                    final Location location = result.getLastLocation();

                    try {
                        HomePage_MPP_1.getInstance().updateLocation(location.getLatitude(), location.getLongitude());
                    }catch (Exception ex){
                        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                .whereEqualTo(FirebaseObjects.ID, firebaseHelper.getCurrentUserID())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                                        .document(documentSnapshot.getId())
                                                        .update(FirebaseObjects.Latitude, location.getLatitude());
                                                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                                        .document(documentSnapshot.getId())
                                                        .update(FirebaseObjects.Longitude, location.getLongitude());
                                            }
                                        }
                                    }
                                });
                    }
                }
            }
        }
    }
}
