package com.example.grandmagear;

import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.grandmagear.Patient_Main_Lobby.HomePage_MPP_1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class BTFragment extends DialogFragment {

    protected ListView btList;
    protected TextView header;
    protected Button cancelButton;
    protected ArrayList<BluetoothDevice> deviceList;
    protected BTHelper btHelper;
    private ArrayAdapter<String> listAdapter;
    protected FirebaseObjects.UserDBO thisUser;
    protected FirebaseHelper firebaseHelper;
    private ArrayList<String> btNames = new ArrayList<String>();
    protected boolean firstSync;

    public BTFragment(FirebaseObjects.UserDBO thisUser, boolean firstSync) {
        this.thisUser = thisUser;
        this.firstSync = firstSync;
        firebaseHelper = new FirebaseHelper();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.bt_fragment, container, false);

        if(getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setupLayout(view);
        return view;
    }

    protected void setupLayout(View view){
        header = view.findViewById(R.id.btTitle);
        btList = view.findViewById(R.id.BTList);
        cancelButton = view.findViewById(R.id.btFragCancel);
        btHelper = new BTHelper(getContext(), thisUser);
        deviceList = btHelper.deviceList();


        for(int i =0; i<deviceList.size(); i++){
            btNames.add(deviceList.get(i).getName());
        }
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, btNames);
        btList.setAdapter(listAdapter);

        btList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    btHelper.setHc05(deviceList.get(position).getAddress());
                    if(!firstSync) {
                        try{
                            btHelper.disconnectnConfirm();
                        } catch (Exception e){

                        }
                        ((HomePage_MPP_1) getActivity()).btConnect();
                        final String address = btHelper.getHC05().getAddress().replaceAll(":","");
                        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                .whereEqualTo(FirebaseObjects.ID, firebaseHelper.getCurrentUserID())
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(DocumentSnapshot snap : task.getResult()){
                                    if(snap.getId() == address){

                                    }
                                    else {
                                        FirebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                                                .document(snap.getId())
                                                .delete();
                                        createDevice(address);
                                    }
                                }

                            }
                        });
                    }
                    else {
                        createDevice(btHelper.getHC05().getAddress().replaceAll(":",""));
                    }
                    getDialog().dismiss();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    public void createDevice(String deviceID){
        final FirebaseObjects.DevicesDBO newDevice = new FirebaseObjects.DevicesDBO(deviceID);
        firebaseHelper.addDevice(newDevice);
    }
}
