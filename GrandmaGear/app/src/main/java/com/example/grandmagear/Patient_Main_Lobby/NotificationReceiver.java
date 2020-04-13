package com.example.grandmagear.Patient_Main_Lobby;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;

public class NotificationReceiver extends BroadcastReceiver {
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    @Override
    public void onReceive(Context context, Intent intent) {
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB).document(firebaseHelper.getCurrentUserID()).update(FirebaseObjects.GPS_Follow, true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(5);
    }
}
