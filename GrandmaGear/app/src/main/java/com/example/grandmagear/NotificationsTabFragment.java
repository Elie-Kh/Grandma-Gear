package com.example.grandmagear;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsTabFragment extends Fragment {

    public static final String TAG = "_NotificationFragment";
    private RecyclerView mRecyclerView;
    private NotificationsRecyclerView mAdapter;
    private ArrayList<String> mNotificationTitle = new ArrayList<String>();
    private ArrayList<String> mNotificationText = new ArrayList<String>();
    private ArrayList<String> mNotificationTime = new ArrayList<String>();
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private FirebaseObjects.UserDBO userDBO;
    private ArrayList<HashMap<String, Object>> thisNotifications;
    private FirebaseHelper firebaseHelper;
    private SharedPreferencesHelper sharedPreferencesHelper_Login;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifications_tab_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.notificationRecycler);
        /*mAdapter = new NotificationsRecyclerView(mNotificationTitle, mNotificationText,mNotificationTime, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));*/
        return view;
    }

    public void addNotification(String title, String text, String time){
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
        sharedPreferencesHelper_Login = new SharedPreferencesHelper(getActivity(), "Login");
        firebaseHelper = new FirebaseHelper();
        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
                                   @Override
                                   public void onCallback(FirebaseObjects.UserDBO user) {
                                       userDBO = user;
                                      firebaseHelper.getNotifications_follower(new FirebaseHelper.Callback_Notifications() {
                                          @Override
                                          public void onCallback(ArrayList<HashMap<String, Object>> notifications) {
                                              thisNotifications = notifications;
                                              for(HashMap<String, Object> entry : thisNotifications ){
                                                  Log.d(TAG, entry.get("notificationTitle").toString());
                                                  Log.d(TAG, entry.get("notificationText").toString());
                                                  Log.d(TAG, entry.get("notificationTime").toString());
                                                  mNotificationTitle.add(entry.get("notificationTitle").toString());
                                                  mNotificationText.add(entry.get("notificationText").toString());
                                                  mNotificationTime.add(entry.get("notificationTime").toString());
                                              }
                                              mAdapter = new NotificationsRecyclerView(mNotificationTitle, mNotificationText,mNotificationTime, getActivity());
                                              mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                              mRecyclerView.setAdapter(mAdapter);
                                              mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                                                      DividerItemDecoration.VERTICAL));
                                          }
                                      });
                                   }
                               }, sharedPreferencesHelper_Login.getEmail(),
                Boolean.parseBoolean(sharedPreferencesHelper_Login.getType()));


        /*mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Title");
        if(mSharedPreferencesHelper.getNotificationTitle() != null){
            mNotificationTitle = mSharedPreferencesHelper.getNotificationTitle();
        }

        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Text");
        if(mSharedPreferencesHelper.getNotificationText() != null){
            mNotificationText = mSharedPreferencesHelper.getNotificationText();
        }
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "Notification Time");
        if(mSharedPreferencesHelper.getNotificationTime() != null){
            mNotificationTime = mSharedPreferencesHelper.getNotificationTime();
        }*/
    }
}
