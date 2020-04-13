package com.example.grandmagear;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsRecyclerView extends RecyclerView.Adapter<NotificationsRecyclerView.ViewHolder> {

    private ArrayList<String> notificationTitle;
    private ArrayList<String> notificationText;
    private ArrayList<String> notificationTime;
    private ArrayList<String> deviceIDs;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferencesHelper sharedPreferencesHelper_Login;
    private Context context;
    private FirebaseHelper firebaseHelper;
    private FirebaseObjects.UserDBO userDBO;
    private ArrayList<HashMap<String, Object>> thisNotifications;

    public NotificationsRecyclerView(ArrayList<String> notificationTitle, ArrayList<String> notificationText,
                                     ArrayList<String> notificationTime,ArrayList<String> deviceIDs, Context context){
        this.notificationTitle = notificationTitle;
        this.notificationText = notificationText;
        this.notificationTime = notificationTime;
        this.deviceIDs = deviceIDs;
        this.context = context;
        this.firebaseHelper = new FirebaseHelper();
        this.sharedPreferencesHelper_Login = new SharedPreferencesHelper(context, "Login");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_recycler_item,
                parent, false);
        view.setTag("notificationsRecyclerView");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mNotificationTitle.setText(notificationTitle.get(position));
        holder.mNotificationText.setText(notificationText.get(position));
        holder.mNotificationTime.setText(notificationTime.get(position));
        holder.deviceID = deviceIDs.get(position);

    }

    @Override
    public int getItemCount() {
        return (notificationTitle.size() + notificationText.size())/2;
    }

    public void delete(final int position){
        notificationTitle.remove(position);
        //sharedPreferencesHelper = new SharedPreferencesHelper(context, "Notification Title");
        //sharedPreferencesHelper.saveNotificationTitle(notificationTitle);
        notificationText.remove(position);
        //sharedPreferencesHelper = new SharedPreferencesHelper(context, "Notification Text");
        //sharedPreferencesHelper.saveNotificationText(notificationText);
        notificationTime.remove(position);
        //sharedPreferencesHelper = new SharedPreferencesHelper(context, "Notification Time");
        //sharedPreferencesHelper.saveNotificationTime(notificationTime);
        deviceIDs.remove(position);
        notifyItemRemoved(position);

//        firebaseHelper.getUser(new FirebaseHelper.Callback_getUser() {
//            @Override
//            public void onCallback(FirebaseObjects.UserDBO user) {
//                userDBO = user;
//                firebaseHelper.getNotifications_follower(new FirebaseHelper.Callback_Notifications() {
//                    @Override
//                    public void onCallback(ArrayList<HashMap<String, Object>> notifications) {
//                        thisNotifications = notifications;
//                        thisNotifications.remove(position);
//                        firebaseHelper.editUser(userDBO, FirebaseObjects.Notifications, thisNotifications);
//                    }
//                });
//            }
//        },sharedPreferencesHelper_Login.getEmail(),
//                Boolean.parseBoolean(sharedPreferencesHelper_Login.getType()));
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                .document(FirebaseHelper.firebaseAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                userDBO = task.getResult().toObject(FirebaseObjects.UserDBO.class);
                userDBO.notifications.remove(position);
                firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                        .document(FirebaseHelper.firebaseAuth.getCurrentUser().getUid())
                        .update("notifications", userDBO.notifications);
            }
        });
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mPatientImage;
        protected ImageView mDeleteNotification;
        protected TextView mNotificationTitle;
        protected TextView mNotificationText;
        protected TextView mNotificationTime;
        protected String deviceID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPatientImage = itemView.findViewById(R.id.patient_image_notifications);
            mDeleteNotification = itemView.findViewById(R.id.deleteNotification);
            mNotificationTitle = itemView.findViewById(R.id.notificationTitle);
            mNotificationText = itemView.findViewById(R.id.notificationText);
            mNotificationTime = itemView.findViewById(R.id.notificationTime);

            mDeleteNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToNotifDelete(getAdapterPosition());
                    //delete(getAdapterPosition());
                }
            });

            //SELECT IMAGE FROM FIREBASE.
//            FirebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
//                    .document(deviceID)
//                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    //GET IMAGE OF USER
//                }
//            });
        }
    }

    public void goToNotifDelete(int position){
        NotifDeleteFragment deleteFragment = new NotifDeleteFragment(this, position);
        deleteFragment.setCancelable(false);
        deleteFragment.show(((UserActivity)context).getSupportFragmentManager(), "NotifDeleteFragment");
    }
}
