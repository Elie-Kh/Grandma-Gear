package com.example.grandmagear.Patient_Main_Lobby;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;
import com.example.grandmagear.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class BatteryReceiver extends BroadcastReceiver {

    private FirebaseHelper firebaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        firebaseHelper = new FirebaseHelper();

        TextView phoneBattery = ((HomePage_MPP_1)context).findViewById(R.id.textView_PhoneBatteryLevel_Displayed);

        String action = intent.getAction();

        if(action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)){
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            final int percentage = level * 100 / scale;
            phoneBattery.setText(Integer.toString(percentage));

            firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                    .whereEqualTo(FirebaseObjects.ID, firebaseHelper.getCurrentUserID())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                            .document(documentSnapshot.getReference().getId())
                                            .update(FirebaseObjects.PhoneBattery, percentage);
                                }
                            }
                        }
                    });
        }
    }
}
