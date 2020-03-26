package com.example.grandmagear;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class PatientsTabFragment extends Fragment {

    private static final String TAG = "PatientsTabFrag__";
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    protected ArrayList<String> mPatientsList = new ArrayList<String>();
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patient_tab_fragment, container, false);
        Log.d(TAG, "Inflated");
        mRecyclerView = view.findViewById(R.id.device_recycler);
        mAdapter = new RecyclerViewAdapter(mPatientsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
        return view;
    }

    public void addPatient(String patientDevice){
        mPatientsList.add(patientDevice);
        mSharedPreferencesHelper.savePatientsList(mPatientsList);

    }

    public ArrayList<String> getmPatientsList(){
        return mPatientsList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String patientDevice = "123456";
        String patientDevice2 = "123";
        String patientDevice3 = "456";
        mPatientsList.add(patientDevice);
        mPatientsList.add(patientDevice2);
        mPatientsList.add(patientDevice3);
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "PatientIDs");
        if(mSharedPreferencesHelper.getIDs() != null) {
            mPatientsList = mSharedPreferencesHelper.getIDs();
        }
    }
}
