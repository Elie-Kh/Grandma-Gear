package com.example.grandmagear;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationsRecyclerView extends RecyclerView.Adapter<NotificationsRecyclerView.ViewHolder> {

    private ArrayList<String> notificationTitle;
    private ArrayList<String> notificationText;
    private ArrayList<Long> notificationTime;

    public NotificationsRecyclerView(ArrayList<String> notificationTitle, ArrayList<String> notificationText,
                                     ArrayList<Long> notificationTime){
        this.notificationTitle = notificationTitle;
        this.notificationText = notificationText;
        this.notificationTime = notificationTime;
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
        for(Long l : notificationTime){
            Log.d("Logger___", String.valueOf(l));
        }
        holder.mNotificationTime.setText(Long.toString(notificationTime.get(position)));
    }

    @Override
    public int getItemCount() {
        return (notificationTitle.size() + notificationText.size())/2;
    }

    public void delete(int position){
        notificationTitle.remove(position);
        notificationText.remove(position);
        notificationTime.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mPatientImage;
        protected ImageView mDeleteNotification;
        protected TextView mNotificationTitle;
        protected TextView mNotificationText;
        protected TextView mNotificationTime;

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
                    delete(getAdapterPosition());
                }
            });
        }
    }
}
