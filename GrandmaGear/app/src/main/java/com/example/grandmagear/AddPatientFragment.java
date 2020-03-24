package com.example.grandmagear;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ConcurrentModificationException;

public class AddPatientFragment extends DialogFragment {

    private static final String TAG = "AddPatient__";
    protected EditText mDeviceId;
    protected Button mAdd;
    protected Button mCancel;
    protected String patientDevice;
    protected FirebaseObjects.UserDBO user;
    protected FirebaseHelper firebaseHelper;

    public AddPatientFragment(FirebaseObjects.UserDBO userDBO) {
        this.user = userDBO;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_patient_fragment, container,false);
        layoutSetup(view);

        return view;
    }

    protected void layoutSetup(View view){
        mDeviceId = view.findViewById(R.id.device_id_adding_fragment);
        mAdd = view.findViewById(R.id.add_device_adding_fragment);
        mCancel = view.findViewById(R.id.cancel_device_adding_fragment);
        firebaseHelper = new FirebaseHelper();

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientDevice = mDeviceId.getText().toString();
                Log.d(TAG, "Created Patient");
                if(!patientDevice.trim().isEmpty()) {
                    final FirebaseObjects.DevicesDBO device = new FirebaseObjects.DevicesDBO(patientDevice);
                    ((UserActivity)getActivity()).firebaseFirestore
                            .collection(FirebaseHelper.deviceDB)
                            .whereEqualTo(FirebaseObjects.ID,patientDevice)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        firebaseHelper.addDevice(device);
                                        firebaseHelper.addDeviceFollowed(user, patientDevice);
                                    }else{

                                    }
                                }
                            });
                    ((UserActivity) getActivity()).mPatientsTabFragment.addPatient(patientDevice);
                    Log.d(TAG, "Added Patient");
                }
                //((UserActivity)getActivity()).mViewPager.findViewWithTag("recyclerView");
                /*TODO: Check if Device in Database.
                * TODO: If it is, get information from database and create new patient device.
                * TODO: replace userExists below with the actual check from database.
                *
                * if(userExists) {
                *   PatientDevice patientDevice = new PatientDevice();
                *   ((UserActivity) getActivity()).mPatientsTab.mPatientsList.add(patientDevice);
                * }
                */
                getDialog().dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }
}
