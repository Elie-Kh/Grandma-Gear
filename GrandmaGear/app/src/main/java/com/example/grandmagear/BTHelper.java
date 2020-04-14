package com.example.grandmagear;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.grandmagear.Patient_Main_Lobby.HomePage_MPP_1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.$Gson$Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.example.grandmagear.FirebaseHelper.TAG;

public class BTHelper {

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;
    private boolean streamOn;

    private BluetoothAdapter myBTAdapter;
    private BluetoothSocket btSocket = null;
    private int highHR = 0;
    private int lowHR = 0;
    protected BluetoothDevice hc05;
    protected SharedPreferencesHelper sharedPreferencesHelper;
    protected Context btContext;
    protected NotificationHelper notificationHelper;
    protected FirebaseHelper firebaseHelper;
    protected FirebaseObjects.DevicesDBO device;
    protected boolean firstOn;


    public BTHelper(Context context, FirebaseObjects.UserDBO thisUser){
        sharedPreferencesHelper = new SharedPreferencesHelper(context, "BTList");
        myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        firebaseHelper = new FirebaseHelper();
        notificationHelper = new NotificationHelper(context, thisUser);
        streamOn = false;
        btContext = context;
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                .whereEqualTo(FirebaseObjects.ID, firebaseHelper.getCurrentUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                               // Log.d(TAG, "docsnap " + documentSnapshot.getReference().getId());
                               // Log.d(TAG, firebaseHelper.getCurrentUserID());
                                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                        .document(documentSnapshot.getId())
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        device = task.getResult().toObject(FirebaseObjects.DevicesDBO.class);
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public BTHelper(Context context, String mac, FirebaseObjects.UserDBO thisUser){
        sharedPreferencesHelper = new SharedPreferencesHelper(context, "BTList");
        myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        streamOn = false;
        btContext = context;
        notificationHelper = new NotificationHelper(context, thisUser);
        setHc05(mac);
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                .whereEqualTo(FirebaseObjects.ID, firebaseHelper.firebaseAuth.getCurrentUser())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                // Log.d(TAG, "docsnap " + documentSnapshot.getReference().getId());
                                // Log.d(TAG, firebaseHelper.getCurrentUserID());
                                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                        .document(documentSnapshot.getReference().getId())
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        device = task.getResult().toObject(FirebaseObjects.DevicesDBO.class);
                                    }
                                });
                            }
                        }
                        else {
                            FirebaseObjects.DevicesDBO device = new FirebaseObjects.DevicesDBO(getHC05().getAddress());
                            firebaseHelper.addDevice(device);
                        }
                    }
                });
    }

    public void btEnable(Activity activity){
        if(!myBTAdapter.isEnabled()){
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(activity, enableBT, REQUEST_ENABLE_BT, null);
        }
    }

    public void setHc05(String mac){
        sharedPreferencesHelper.saveHC05(mac);
        hc05 = myBTAdapter.getRemoteDevice(mac);

    }

    public void estConnect(){

        new BTconnection().execute();

    }

    public BluetoothDevice getHC05(){
        return hc05;

    }

    private class BTconnection extends AsyncTask<Void, Void, Void> {
        boolean isConnected = true;

        @Override
        protected void onPreExecute(){
            popToast("Connecting to Grandma Gear Hardware");
        }

        @Override
        protected Void doInBackground(Void...devices){

            try{
                if(btSocket==null || !streamOn){
                    //disconnectnConfirm();
                    hc05 = getHC05();
                    btSocket = hc05.createInsecureRfcommSocketToServiceRecord(mUUID);
                    btSocket.connect();
                }
            } catch (IOException e){
                isConnected = false;
            }
            if(!isConnected){
                //TODO notify follower device is off
                firebaseHelper = new FirebaseHelper();
                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                        .document(firebaseHelper.getCurrentUserID())
                        .update(FirebaseObjects.DeviceOn,"off");
            } else{
                //TODO notify follower device is on
                firebaseHelper = new FirebaseHelper();
                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                        .document(firebaseHelper.getCurrentUserID())
                        .update(FirebaseObjects.DeviceOn,"on");
            }
            return null;
        }


        @Override
        protected void onPostExecute (Void result){
            super.onPostExecute(result);

            if(!isConnected){
                popToast("Connection failed please try again");
                if(btSocket != null){
                    try {
                        disconnectnConfirm();
                        btSocket = null;
                        hc05 = getHC05();
                        btSocket = hc05.createInsecureRfcommSocketToServiceRecord(mUUID);
                        btSocket.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ;
                }
            } else {
                popToast("Device Connected");
                streamOn = true;
            }
        }


    }

    private class ContentAsync extends AsyncTask<Void,Void,Void>{

        TextView textView;
        boolean active;
       Context context;

        public ContentAsync(TextView textView, boolean active) {
            this.textView = textView;
            this.active = active;
            //this.context = context;
        }


        @Override
        protected Void doInBackground(Void... voids) {
//            Looper.prepare();

            content(textView, active);
            return null;
        }


    }


    public void disconnectnConfirm(){
        int counter = 0;
        do {
            try {
                btSocket.close();
                // System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        } while (btSocket.isConnected() && counter < 3);

        if(btSocket.isConnected()){
            popToast("Bluetooth failed to disconnect");
        } else {
            popToast("Bluetooth disconnected");

        }
    }

    public void content(TextView textview, boolean active) {

        if(firebaseHelper == null){
            firebaseHelper = new FirebaseHelper();
        }
        InputStream inputStream = null;
        String heartRate = "";
        Log.d(TAG, "content: BTSOCKET" + btSocket);
        if(btSocket != null){
            if(btSocket.isConnected()) {
                try {
                    inputStream = btSocket.getInputStream();
                    inputStream.skip(inputStream.available());

                    for (int i = 0; i < 3; i++) {
                        byte b = (byte) inputStream.read();

                        if (((char) b) == 'F') {
                            //TODO signal fall
                            notificationHelper.sendOnFall("A Fall", "A fall was detected", firebaseHelper.getCurrentUserID());

                        } else if (((char) b) == 'N'){
                            //TODO signal device off
                            if(firstOn) {
                                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                                        .document(FirebaseHelper.firebaseAuth.getCurrentUser().getUid())
                                        .update(FirebaseObjects.DeviceOn, "off");
                                firstOn = false;
                            }
                        }else if(((char) b) == ' '){

                        }else {
                            heartRate += (char) b;
                        }
                    }

                    if(Integer.parseInt(heartRate)!=0){
                        if(!firstOn) {

                            firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB).whereEqualTo(FirebaseObjects.ID, firebaseHelper.getCurrentUserID())
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(DocumentSnapshot doc : task.getResult().getDocuments()){
                                            firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB).document(doc.getId())
                                                    .update(FirebaseObjects.DeviceOn, "on");
                                        }
                                    }
                                }
                            });
                            firstOn = true;
                        }
                        final String finalHeartRate = heartRate;
                        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB).whereEqualTo(FirebaseObjects.ID, firebaseHelper.getCurrentUserID())
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DocumentSnapshot doc : task.getResult().getDocuments()){
                                        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB).document(doc.getId())
                                                .update(FirebaseObjects.Heartrate,Integer.parseInt( finalHeartRate));
                                    }
                                }
                            }
                        });
                    }

                    if(Integer.parseInt(heartRate) > 100){
                        highHR ++;
                        lowHR = 0;
                        if(active){
//                            textview.setTextColor(Color.RED);
//                            textview.setText(heartRate);
                            ((HomePage_MPP_1)btContext).updateBPM(Integer.parseInt(heartRate));
                        }


                        if(highHR > 3){

                                notificationHelper.sendOnBpm("High BPM", "A bpm of " + heartRate + " was recorded", firebaseHelper.getCurrentUserID());
                                highHR = 0;
                        }

                    } else if ( Integer.parseInt(heartRate) < 60){
                        lowHR ++;
                        highHR =0;
                        if(active) {
//                            textview.setTextColor(Color.RED);
//                            textview.setText(heartRate);
                            ((HomePage_MPP_1)btContext).updateBPM(Integer.parseInt(heartRate));
                        }

                        if(lowHR > 3){
                            notificationHelper.sendOnBpm("Low BPM", "A bpm of " + heartRate + " was recorded", firebaseHelper.getCurrentUserID());
                            lowHR = 0;
                        }

                    } else {
                        highHR = 0;
                        lowHR = 0;
                        if(active) {
//                            textview.setText(heartRate);
//                            textview.setTextColor(Color.GREEN);
                            ((HomePage_MPP_1)btContext).updateBPM(Integer.parseInt(heartRate));
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        refresh(textview, (btContext));


    }

    private void refresh(final TextView textview, final Context context){

//        Looper.prepare();
        final Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                //content(textview);
                if(((HomePage_MPP_1)context).getActive()) {
                    ContentAsync contentAsync = new ContentAsync(textview, true);
                    contentAsync.execute();
                }
                else {
                    ContentAsync contentAsync = new ContentAsync(textview, false);
                    contentAsync.execute();
                }

            }
        };

        handler.postDelayed(runnable, 4000);
    }

    protected void popToast(String msg){
        Toast.makeText(btContext, msg,Toast.LENGTH_SHORT).show();
    }

    public ArrayList<BluetoothDevice> deviceList(){
        return new ArrayList<>(myBTAdapter.getBondedDevices());

    }
}