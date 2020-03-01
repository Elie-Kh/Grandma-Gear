package com.example.grandmagear;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientsTabFragment extends Fragment {

    private static final String TAG = "PatientsTabFrag__";
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    ArrayList<PatientDevice> mPatientsList = new ArrayList<PatientDevice>();

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

    public void addPatient(PatientDevice patientDevice){
        mPatientsList.add(patientDevice);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PatientDevice patientDevice = new PatientDevice("Yes", "Yes",
                "ok","no");
        mPatientsList.add(patientDevice);
        mPatientsList.add(patientDevice);
        mPatientsList.add(patientDevice);
    }
}
