package com.example.grandmagear;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.grandmagear.Patient_Main_Lobby.HomePage_MPP_1;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class BTFragment extends DialogFragment {

    protected ListView btList;
    protected TextView header;
    protected ArrayList<BluetoothDevice> deviceList;
    protected BTHelper btHelper;
    private ArrayAdapter<BluetoothDevice> listAdapter;
    protected FirebaseObjects.UserDBO thisUser;

    public BTFragment(FirebaseObjects.UserDBO thisUser) {
        this.thisUser = thisUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.bt_fragment, container, false);

        setupLayout(view);
        return view;
    }

    protected void setupLayout(View view){
        header = view.findViewById(R.id.btTitle);
        btList = view.findViewById(R.id.BTList);
        btHelper = new BTHelper(getContext(), thisUser);
        deviceList = btHelper.deviceList();

        listAdapter = new ArrayAdapter<BluetoothDevice>(getActivity(), android.R.layout.simple_list_item_1, deviceList);
        btList.setAdapter(listAdapter);

        btList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                btHelper.setHc05(deviceList.get(position).getAddress());
                ((HomePage_MPP_1)getActivity()).btConnect();
                getDialog().dismiss();
            }
        });
    }
}
