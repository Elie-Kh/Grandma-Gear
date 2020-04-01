package com.example.grandmagear;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.getSystemService;

public class BTHelper {

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;
    private boolean streamOn;

    private BluetoothAdapter myBTAdapter;
    private BluetoothSocket btSocket = null;
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

    public BluetoothDevice getHC05(){
        return hc05;
    }

    public void connectnConfirm(){

        do {
            try {
                btSocket = hc05.createInsecureRfcommSocketToServiceRecord(mUUID);
                // System.out.println(btSocket);
                btSocket.connect();
                // System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (!btSocket.isConnected());

        if(!btSocket.isConnected()){
            popToast("Bluetooth connection failed");
        } else {
            popToast("Bluetooth connection established");

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
        String message = "";

        if(btSocket.isConnected()) {
            try {
                inputStream = btSocket.getInputStream();
                inputStream.skip(inputStream.available());

                for (int i = 0; i < 2; i++) {
                    byte b = (byte) inputStream.read();

                    if(((char) b) == 'f'){
                        message = "FALL";
                        break;
                    } else
                    message += (char) b;
                }

                textview.setText(message);

            } catch (IOException e) {
                e.printStackTrace();
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

        /*ArrayList<BluetoothDevice> deviceList = new ArrayList<>();

        if(pairedDevices.size() >0){

            for(BluetoothDevice device : pairedDevices){
                deviceList
            }
        }*/
    }
}