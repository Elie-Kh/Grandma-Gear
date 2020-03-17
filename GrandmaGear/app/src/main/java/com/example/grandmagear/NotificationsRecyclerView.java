package com.example.grandmagear;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationsRecyclerView extends RecyclerView.Adapter<NotificationsRecyclerView.ViewHolder> {

    private ArrayList<String> notificationTitle;
    private ArrayList<String> notificationText;

    public NotificationsRecyclerView(ArrayList<String> notificationTitle, ArrayList<String> notificationText){
        this.notificationTitle = notificationTitle;
        this.notificationText = notificationText;
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
    }

    @Override
    public int getItemCount() {
        return (notificationTitle.size() + notificationText.size())/2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mPatientImage;
        protected TextView mNotificationTitle;
        protected TextView mNotificationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPatientImage = itemView.findViewById(R.id.patient_image_notifications);
            mNotificationTitle = itemView.findViewById(R.id.notificationTitle);
            mNotificationText = itemView.findViewById(R.id.notificationText);
        }
    }
}
