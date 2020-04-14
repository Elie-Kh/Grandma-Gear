package com.example.grandmagear;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.grandmagear.NotificationHelper.TAG;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mNotificationTitle.setText(notificationTitle.get(position));
        holder.mNotificationText.setText(notificationText.get(position));
        holder.mNotificationTime.setText(notificationTime.get(position));
        holder.deviceID = deviceIDs.get(position);
        Log.d(TAG, "onBindViewHolder: " + deviceIDs.get(position));
//        if(notificationTitle.get(position).contains("BPM")){
//            holder.mPatientImage.setImageResource(R.drawable.heartbeat);
//        }
//        if(notificationTitle.get(position).contains("Fall")){
//            holder.mPatientImage.setImageResource(R.drawable.falling);
//        }
//        if (notificationTitle.get(position).contains("Battery")){
//            holder.mPatientImage.setImageResource(R.drawable.battery);
//        }
//        if(notificationTitle.get(position).contains("S.O.S.")){
//            holder.mPatientImage.setImageResource(R.drawable.sos_icon);
//        }
//        if(notificationTitle.get(position).contains("Offline")){
//            holder.mPatientImage.setImageResource(R.drawable.offfline_icon);
//        }
        FirebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                .document(deviceIDs.get(position))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final FirebaseObjects.DevicesDBO device = task.getResult().toObject(FirebaseObjects.DevicesDBO.class);

                    FirebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                            .document(device.id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            FirebaseObjects.UserDBO patient = task.getResult().toObject(FirebaseObjects.UserDBO.class);
                                if (patient.getImage()) {
                                    FirebaseStorage storage = FirebaseStorage.getInstance();

                                    // Get image location

                                    //Intent t = getIntent();
                                    //Bundle b = t.getExtras();
                                    String filename = "gs://grandma-gear.appspot.com/WearerProfilePicture/" + patient.getUsername();
                                    StorageReference gsReference = storage.getReferenceFromUrl(filename);


                                    final long ONE_MEGABYTE = 1024 * 1024;
                                    gsReference.getBytes(ONE_MEGABYTE * 4).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            storeProfilePicture(bmp);
                                            holder.mPatientImage.setImageBitmap(bmp);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });
                                } else {
                                    holder.mPatientImage.setImageResource(R.drawable.gg_default_pic);
                                }

                        }
                    });


                }
            }
        });

    }

    public void storeProfilePicture(Bitmap bitmap){
        FileOutputStream outStream = null;

        // Write to SD Card

        File file = new File(((UserActivity)context).getFilesDir(), "GrandmaGearProfilePicture");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }

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
