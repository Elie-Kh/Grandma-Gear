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

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationsTabFragment extends Fragment {

    public static final String TAG = "_NotificationFragment";
    private RecyclerView mRecyclerView;
    private NotificationsRecyclerView mAdapter;
    private ArrayList<String> mNotificationTitle = new ArrayList<String>();
    private ArrayList<String> mNotificationText = new ArrayList<String>();
    private ArrayList<Long> mNotificationTime = new ArrayList<Long>();
    private SharedPreferencesHelper mSharedPreferencesHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifications_tab_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.notificationRecycler);
        mAdapter = new NotificationsRecyclerView(mNotificationTitle, mNotificationText,mNotificationTime, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
        return view;
    }

    public void addNotification(String title, String text, long time){
        mNotificationTitle.add(title);
        mNotificationText.add(text);
        mNotificationTime.add(time);
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Title");
        mSharedPreferencesHelper.saveNotificationTitle(mNotificationTitle);
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Text");
        mSharedPreferencesHelper.saveNotificationText(mNotificationText);
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Time");
        mSharedPreferencesHelper.saveNotificationTime(mNotificationTime);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*String title1 = "Heart Attack";
        String title2 = "Fell Down";
        String title3 = "Fell again";
        String title4 = "Healed";
        String text1 = "Grandma had a heart attack";
        String text2 = "Grandma fell down";
        String text3 = "She fell again";
        String text4 = "She healed";*/
        long time1 = 1;
        long time2 = 2;
        long time3 = 3;
        long time4 = 4;
        /*mNotificationTitle.add(title1);
        mNotificationTitle.add(title2);
        mNotificationTitle.add(title3);
        mNotificationTitle.add(title4);
        Collections.reverse(mNotificationTitle);
        mNotificationText.add(text1);
        mNotificationText.add(text2);
        mNotificationText.add(text3);
        mNotificationText.add(text4);
        Collections.reverse(mNotificationText);*/
        mNotificationTime.add(time1);
        mNotificationTime.add(time2);
        mNotificationTime.add(time3);
        //mNotificationTime.add(time4);
        //Collections.reverse(mNotificationTime);


        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Title");
        //mSharedPreferencesHelper.saveNotificationTitle(mNotificationTitle);
        if(mSharedPreferencesHelper.getNotificationTitle() != null){
            mNotificationTitle = mSharedPreferencesHelper.getNotificationTitle();
        }
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Text");
        //mSharedPreferencesHelper.saveNotificationText(mNotificationText);
        if(mSharedPreferencesHelper.getNotificationText() != null){
            mNotificationText = mSharedPreferencesHelper.getNotificationText();
        }
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Time");
        mSharedPreferencesHelper.saveNotificationTime(mNotificationTime);
        /*if(mSharedPreferencesHelper.getNotificationTime() != null){
            mNotificationTime = mSharedPreferencesHelper.getNotificationTime();
        }*/
    }
}
