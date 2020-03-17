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

public class NotificationsTabFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NotificationsRecyclerView mAdapter;
    private ArrayList<String> mNotifications = new ArrayList<String>();
    private SharedPreferencesHelper mSharedPreferencesHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifications_tab_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.notificationRecycler);
        mAdapter = new NotificationsRecyclerView(mNotifications);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
        return view;
    }

    public void addNotification(String notification){
        mNotifications.add(notification);
        mSharedPreferencesHelper.saveNotification(mNotifications);
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
        mNotifications.add(title1 + "\n" + text1);
        mNotifications.add(title2 + "\n" + text2);
        mNotifications.add(title3 + "\n" + text3);
        mNotifications.add(title4 + "\n" + text4);

        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "NotificationIDs");
        mSharedPreferencesHelper.saveNotification(mNotifications);
        /*if(mSharedPreferencesHelper.getNotification() != null){
            mNotifications = mSharedPreferencesHelper.getNotification();
        }*/
    }
}
