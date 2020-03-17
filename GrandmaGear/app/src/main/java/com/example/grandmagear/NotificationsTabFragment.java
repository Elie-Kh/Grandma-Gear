package com.example.grandmagear;

import android.os.Bundle;
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
import java.util.Collections;

public class NotificationsTabFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NotificationsRecyclerView mAdapter;
    private ArrayList<String> mNotificationTitle = new ArrayList<String>();
    private ArrayList<String> mNotificationText = new ArrayList<String>();
    private SharedPreferencesHelper mSharedPreferencesHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifications_tab_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.notificationRecycler);
        mAdapter = new NotificationsRecyclerView(mNotificationTitle, mNotificationText);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
        return view;
    }

    public void addNotification(String title, String text){
        mNotificationTitle.add(title);
        mNotificationText.add(text);
        mSharedPreferencesHelper.saveNotificationTitle(mNotificationTitle);
        mSharedPreferencesHelper.saveNotificationText(mNotificationText);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title1 = "Heart Attack";
        String title2 = "Fell Down";
        String title3 = "Fell again";
        String title4 = "Healed";
        String text1 = "Grandma had a heart attack";
        String text2 = "Grandma fell down";
        String text3 = "She fell again";
        String text4 = "She healed";
        mNotificationTitle.add(title1);
        mNotificationTitle.add(title2);
        mNotificationTitle.add(title3);
        mNotificationTitle.add(title4);
        Collections.reverse(mNotificationTitle);
        mNotificationText.add(text1);
        mNotificationText.add(text2);
        mNotificationText.add(text3);
        mNotificationText.add(text4);
        Collections.reverse(mNotificationText);


        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Title");
        mSharedPreferencesHelper.saveNotificationTitle(mNotificationTitle);
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Text");
        mSharedPreferencesHelper.saveNotificationText(mNotificationText);
        /*if(mSharedPreferencesHelper.getNotification() != null){
            mNotifications = mSharedPreferencesHelper.getNotification();
        }*/
    }
}
