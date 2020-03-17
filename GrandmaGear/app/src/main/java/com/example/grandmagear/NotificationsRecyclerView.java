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

    private ArrayList<String> notifications;

    public NotificationsRecyclerView(ArrayList<String> notifications){
        this.notifications = notifications;
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
        holder.mNotification.setText(notifications.get(getItemCount() - position -1));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mPatientImage;
        protected TextView mNotification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPatientImage = itemView.findViewById(R.id.patient_image_notifications);
            mNotification = itemView.findViewById(R.id.notification);
        }
    }
}
