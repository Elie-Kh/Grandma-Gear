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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
    private ArrayList<FirebaseObjects.Notifications> thisNotifications;
    private FirebaseHelper firebaseHelper;
    private SharedPreferencesHelper sharedPreferencesHelper_Login;


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
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                .document(FirebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        userDBO = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);
                        thisNotifications = userDBO.notifications;
                        if(userDBO.notifications.size() != 0) {
                            mNotificationTitle.add(thisNotifications.get(thisNotifications.size() - 1).getNotificationTitle());
                            mNotificationText.add(thisNotifications.get(thisNotifications.size() - 1).getNotificationText());
                            mNotificationTime.add(thisNotifications.get(thisNotifications.size() - 1).getNotificationTime());
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
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
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                .document(FirebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                     userDBO = task.getResult().toObject(FirebaseObjects.UserDBO.class);
                                                     thisNotifications = userDBO.notifications;
                                                     for (FirebaseObjects.Notifications entry : thisNotifications) {
                                                         Log.d(TAG, entry.getNotificationTitle());
                                                         Log.d(TAG, entry.getNotificationText());
                                                         Log.d(TAG, entry.getNotificationTime());
                                                         mNotificationTitle.add(entry.getNotificationTitle());
                                                         mNotificationText.add(entry.getNotificationText());
                                                         mNotificationTime.add(entry.getNotificationTime());
//                                                         mAdapter = new NotificationsRecyclerView(mNotificationTitle, mNotificationText, mNotificationTime, getActivity());
//                                                         mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                                                         mRecyclerView.setAdapter(mAdapter);
//                                                         mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
//                                                                 DividerItemDecoration.VERTICAL));
                                                     }
                                                 }
                                             }
        );




//                mAdapter = new NotificationsRecyclerView(mNotificationTitle, mNotificationText, mNotificationTime, getActivity());
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                mRecyclerView.setAdapter(mAdapter);
//                mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
//                        DividerItemDecoration.VERTICAL));
//                firebaseHelper.getNotifications_follower(new FirebaseHelper.Callback_Notifications() {
//                    @Override
//                    public void onCallback(ArrayList<HashMap<String, Object>> notifications) {
//                        thisNotifications = notifications;
//                        for (FirebaseObjects.Notifications entry : thisNotifications) {
//                            Log.d(TAG, entry.getNotificationTitle());
//                            Log.d(TAG, entry.getNotificationText());
//                            Log.d(TAG, entry.getNotificationTime());
//                            mNotificationTitle.add(entry.getNotificationTitle());
//                            mNotificationText.add(entry.getNotificationText());
//                            mNotificationTime.add(entry.getNotificationTime());
//                        }
//                        mAdapter = new NotificationsRecyclerView(mNotificationTitle, mNotificationText, mNotificationTime, getActivity());
//                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        mRecyclerView.setAdapter(mAdapter);
//                        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
//                                DividerItemDecoration.VERTICAL));
//                    }



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
