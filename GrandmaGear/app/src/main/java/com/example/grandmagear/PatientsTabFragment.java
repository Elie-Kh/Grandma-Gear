package com.example.grandmagear;

import android.content.Context;
import android.content.Intent;
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

import com.example.grandmagear.Patient_Main_Lobby.HomePage_MPP_1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PatientsTabFragment extends Fragment implements RecyclerViewAdapter.OnItemClickedListener {

    private static final String TAG = "PatientsTabFrag__";
    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mAdapter;
    protected ArrayList<String> mPatientsList = new ArrayList<String>();
    protected FirebaseHelper firebaseHelper = new FirebaseHelper();
    protected FirebaseObjects.UserDBO thisUser;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private SharedPreferencesHelper mSharedPreferencesHelper_login;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.patient_tab_fragment, container, false);
        mSharedPreferencesHelper_login = new SharedPreferencesHelper(getActivity(), "Login");
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
        final DocumentReference documentReference = firebaseHelper.firebaseFirestore
                .collection((FirebaseHelper.userDB))
                .document(firebaseHelper.firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                thisUser = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);
                mPatientsList = thisUser.devicesFollowed;
                mRecyclerView = getView().findViewById(R.id.device_recycler);
                mAdapter = new RecyclerViewAdapter(mPatientsList, PatientsTabFragment.this, getContext());
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                        DividerItemDecoration.VERTICAL));
                //mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        //KIZITO_ENTER_THE_ACTIVITY_NAME.class
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("wearerID", mPatientsList.get(position));
        Log.d(TAG, intent.getExtras().toString());
        startActivity(intent);
    }
}
