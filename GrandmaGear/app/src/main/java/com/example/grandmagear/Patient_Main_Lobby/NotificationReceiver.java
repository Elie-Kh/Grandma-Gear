package com.example.grandmagear.Patient_Main_Lobby;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;


public class NotificationReceiver extends BroadcastReceiver {
    private  FirebaseHelper firebaseHelper = new FirebaseHelper();
    private FirebaseObjects.UserDBO thisUser;


    @Override
    public void onReceive(Context context, Intent intent) {
        notificationEnableLocation();
    }


    public void notificationEnableLocation(){

        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                .document(firebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        thisUser = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);
                        firebaseHelper.editUser(thisUser, FirebaseObjects.GPS_Follow, true);
                    }
                });
    }
}