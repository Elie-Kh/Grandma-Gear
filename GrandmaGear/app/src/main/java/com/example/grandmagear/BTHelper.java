package com.example.grandmagear;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import static androidx.core.app.ActivityCompat.startActivityForResult;

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

    public BTHelper(Context context){
        sharedPreferencesHelper = new SharedPreferencesHelper(context, "BTList");
        myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        streamOn = false;
        btContext = context;
    }

    public BTHelper(Context context, String mac){
        sharedPreferencesHelper = new SharedPreferencesHelper(context, "BTList");
        myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        streamOn = false;
        btContext = context;
        setHc05(mac);
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
        private boolean isConnected =true;

        @Override
        protected void onPreExecute(){
            popToast("Connecting to Grandma Gear Hardware");
        }

        @Override
        protected Void doInBackground(Void...devices){
            try{
                if(btSocket==null || !streamOn){
                    btSocket = hc05.createInsecureRfcommSocketToServiceRecord(mUUID);
                    btSocket.connect();
                }
            } catch (IOException e){
                isConnected = false;
            }
            if(!isConnected){
                //TODO notify follower device is off
            } else{
                //TODO notify follower device is on
            }
            return null;
        }


        @Override
        protected void onPostExecute (Void result){
            super.onPostExecute(result);

            if(!isConnected){
                popToast("Connection failed please try again");
            } else {
                popToast("Device Connected");
                streamOn = true;
            }
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

    public void content(TextView textview) {

        InputStream inputStream = null;
        String heartRate = "";

        if(btSocket != null){
            if(btSocket.isConnected()) {
                try {
                    inputStream = btSocket.getInputStream();
                    inputStream.skip(inputStream.available());

                    for (int i = 0; i < 4; i++) {
                        byte b = (byte) inputStream.read();

                        if (((char) b) == 'F') {
                            //TODO signal fall

                        } else if (((char) b) == 'N'){
                            //TODO signal device off
                        }else {
                            heartRate += (char) b;
                        }
                    }

                    if(Integer.parseInt(heartRate) > 100){
                        highHR ++;
                        lowHR = 0;
                        textview.setText(heartRate);
                        //TODO send HR to followers
                        if(highHR > 3){
                            //TODO send High HR notif
                        }

                    } else if ( Integer.parseInt(heartRate) < 60){
                        lowHR ++;
                        highHR =0;
                        textview.setText(heartRate);
                        //TODO send HR to followers
                        if(lowHR > 3){
                            //TODO send Low HR notif
                        }

                    } else {
                        highHR = 0;
                        lowHR = 0;
                        textview.setText(heartRate);
                        //TODO send heart rate to followers
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        refresh(textview);


    }

    private void refresh(final TextView textview){

        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content(textview);
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