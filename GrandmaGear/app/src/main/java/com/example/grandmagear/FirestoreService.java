package com.example.grandmagear;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.grandmagear.Patient_Main_Lobby.HomePage_MPP_1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirestoreService extends Service {

    FirebaseHelper firebaseHelper;
    SharedPreferencesHelper mSharedPreferencesHelper;
    FirebaseObjects.UserDBO user;
    NotificationHelper notificationHelper;
    boolean fg;

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseHelper = new FirebaseHelper();
        mSharedPreferencesHelper = new SharedPreferencesHelper(this, "Login");
        final String email = mSharedPreferencesHelper.getEmail();
        final String password = mSharedPreferencesHelper.getPassword();
        firebaseHelper.firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    mSharedPreferencesHelper.saveEmail(email);
                    mSharedPreferencesHelper.savePassword(password);
                    Toast.makeText(FirestoreService.this, "Logged In Successfully.", Toast.LENGTH_SHORT).show();
                    final boolean[] emails = new boolean[1];
                    FirebaseHelper.firebaseFirestore
                            .collection(FirebaseHelper.userDB)
                            .document(firebaseHelper.firebaseAuth.getCurrentUser().getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    user = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);

                                }
                            });
                }
            }
        });

        try {
            fg = new ForegroundCheckTask().execute(this).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationHelper = new NotificationHelper(this, user);
        Intent notificationIntent = new Intent(this, LogInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this, App.FOLLOWER_CHANNEL)
                .setContentTitle("GrandmaGear")
                .setContentText("Making sure you have everything up to date")
                .setSmallIcon(R.drawable.sooken)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(8,notification);

    if(!fg) {
        if(user == null){
            FirebaseHelper.firebaseFirestore
                    .collection(FirebaseHelper.userDB)
                    .document(firebaseHelper.firebaseAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            user = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);
                            if (user.accountType) {
                                //follower service
                                for(int i = 0; i < user.devicesFollowed.size(); i++){
                                    FirebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                            .document(user.devicesFollowed.get(i))
                                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                    FirebaseObjects.DevicesDBO device = documentSnapshot
                                                            .toObject(FirebaseObjects.DevicesDBO.class);
                                                    if ((Integer) device.heartrate < 60) {
                                                        notificationHelper.sendOnBpm("Low BPM", "A bpm of " + device.heartrate + " was recorded for", firebaseHelper.getCurrentUserID());
                                                        //check = false;
                                                    }
                                                    if ((Integer) device.heartrate >= 60) {
                                                        //check = true;
                                                    }
                                                    if(((String) device.helpRequested).equals("yes")){
                                                        notificationHelper.sendOnFall("Panic button pressed", "The panic button has been pressed by ", firebaseHelper.getCurrentUserID());
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
                                                            }
                                                        }
                                                    });


                                                }
                                            });
                                }
                            } else {
                                startActivity(new Intent(getApplicationContext(), HomePage_MPP_1.class));
                                //wearer service
                            }

                        }
                    });

        }
        else {
        if (user.accountType) {
            //follower service
            for(int i = 0; i < user.devicesFollowed.size(); i++){
                FirebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                        .document(user.devicesFollowed.get(i))
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                FirebaseObjects.DevicesDBO device = documentSnapshot
                                        .toObject(FirebaseObjects.DevicesDBO.class);
                                if ((Integer) device.heartrate < 60) {
                                    notificationHelper.sendOnBpm("Low BPM", "A bpm of " + device.heartrate + " was recorded for", firebaseHelper.getCurrentUserID());
                                    //check = false;
                                }
                                if ((Integer) device.heartrate >= 60) {
                                    //check = true;
                                }
                                if(((String) device.helpRequested).equals("yes")){
                                    notificationHelper.sendOnFall("Panic button pressed", "The panic button has been pressed by ", firebaseHelper.getCurrentUserID());
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
                                        }
                                    }
                                });


                            }
                        });
            }
        } else {
            startActivity(new Intent(getApplicationContext(), HomePage_MPP_1.class));
            //wearer service
        }


        return START_STICKY;
        }
    }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }


}
