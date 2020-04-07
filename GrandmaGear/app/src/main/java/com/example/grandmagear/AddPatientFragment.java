package com.example.grandmagear;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Collections;

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
//                    ((UserActivity)getActivity()).firebaseFirestore
//                            .collection(FirebaseHelper.deviceDB)
//                            .whereEqualTo(FirebaseObjects.ID,patientDevice)
//                            .get()
//                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if(task.isSuccessful()){
//                                        //firebaseHelper.addDevice(device);
//                                        Log.d("__ADDPATIENT__", user.email);
//                                        firebaseHelper.addDeviceFollowed(user, patientDevice);
//                                    }else{
//
//                                    }
//                                }
//                            });


                    /**TEMPORARY FIX FOR ADDING TWICE IN DB.
                     * **/
//                    if(((UserActivity) getActivity()).mPatientsTabFragment.mPatientsList.size() != 0){
//                        ((UserActivity) getActivity()).mPatientsTabFragment.mPatientsList.remove(
//                                (((UserActivity) getActivity()).mPatientsTabFragment.mPatientsList.size()-1)
//                        );
//                    }
                    ((UserActivity)getActivity()).thisUser.setDevicesFollowed(((UserActivity) getActivity()).mPatientsTabFragment.getmPatientsList());
                    user.setDevicesFollowed(((UserActivity)getActivity()).mPatientsTabFragment.mPatientsList);

                    firebaseHelper.addingPatient(new FirebaseHelper.Callback_AddPatient() {
                        @Override
                        public void onCallback(FirebaseObjects.UserDBO users) {
                            user = users;
                            ((UserActivity)getActivity()).mPatientsTabFragment.mPatientsList =
                                    users.devicesFollowed;
                            Collections.sort(((UserActivity)getActivity()).mPatientsTabFragment.mPatientsList);
                            ((UserActivity)getActivity()).mPatientsTabFragment.mAdapter = new RecyclerViewAdapter(((UserActivity)getActivity()).mPatientsTabFragment.mPatientsList);
                            ((UserActivity)getActivity()).mPatientsTabFragment.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            ((UserActivity)getActivity()).mPatientsTabFragment.mRecyclerView.setAdapter(((UserActivity)getActivity()).mPatientsTabFragment.mAdapter);
                            ((UserActivity)getActivity()).mPatientsTabFragment.mRecyclerView.addItemDecoration(new DividerItemDecoration(((UserActivity)getActivity()).mPatientsTabFragment.mRecyclerView.getContext(),
                                    DividerItemDecoration.VERTICAL));
                           Log.d(TAG, "Added Patient");
                            getDialog().dismiss();
                        }
                    }, user, patientDevice);
                    //((UserActivity)getActivity()).thisUser.device_ids.add(patientDevice);
                    //((UserActivity) getActivity()).mPatientsTabFragment.addPatient(patientDevice);
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
