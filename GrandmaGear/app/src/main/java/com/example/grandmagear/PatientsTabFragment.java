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
import java.util.Collection;
import java.util.Collections;

public class PatientsTabFragment extends Fragment {

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
        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
                                   @Override
                                   public void onCallback(final FirebaseObjects.UserDBO user) {
                                       //thisUser = user;
                                       firebaseHelper.getUser_followers(new FirebaseHelper.Callback_getUserFollowers() {
                                           @Override
                                           public void onCallback(ArrayList<String> followers) {
                                               thisUser = user;
                                               thisUser.setDevice_ids(followers);
                                               mPatientsList = followers;
                                               Collections.sort(mPatientsList);
                                               Log.d(TAG, "Inflated");
                                               mRecyclerView = view.findViewById(R.id.device_recycler);
                                               mAdapter = new RecyclerViewAdapter(mPatientsList);
                                               mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                               mRecyclerView.setAdapter(mAdapter);
                                               mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                                                       DividerItemDecoration.VERTICAL));
                                               ((UserActivity)getActivity()).updateUser(thisUser);
                                           }
                                       }, mPatientsList, user);
                                   }
                               }, mSharedPreferencesHelper_login.getEmail(),
                Boolean.parseBoolean(mSharedPreferencesHelper_login.getType()));

        return view;
    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
//                                   @Override
//                                   public void onCallback(final FirebaseObjects.UserDBO user) {
//                                       //thisUser = user;
//                                       firebaseHelper.getUser_followers(new FirebaseHelper.Callback_getUserFollowers() {
//                                           @Override
//                                           public void onCallback(ArrayList<String> followers) {
//                                               thisUser = user;
//                                               thisUser.setDevice_ids(followers);
//                                               mPatientsList = followers;
//                                               ((UserActivity)getActivity()).updateUser(thisUser);
//
//                                           }
//                                       }, mPatientsList, user);
//                                   }
//                               }, mSharedPreferencesHelper_login.getEmail(),
//                Boolean.parseBoolean(mSharedPreferencesHelper_login.getType()));
//    }

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
