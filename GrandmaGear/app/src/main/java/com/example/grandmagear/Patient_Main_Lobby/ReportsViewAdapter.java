package com.example.grandmagear.Patient_Main_Lobby;

import android.icu.text.MessagePattern;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;
import com.example.grandmagear.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ReportsViewAdapter extends RecyclerView.Adapter<ReportsViewAdapter.ViewHolder> {

    private ArrayList<String> reportTitle;
    private ArrayList<String> reportText;
    private ArrayList<String> reportTime;
    private FirebaseHelper firebaseHelper;
    private FirebaseObjects.UserDBO userDBO;

    public ReportsViewAdapter(ArrayList<String> reportTitle, ArrayList<String> reportText,
                              ArrayList<String> reportTime) {
        this.reportTitle = reportTitle;
        this.reportText = reportText;
        this.reportTime = reportTime;
        firebaseHelper = new FirebaseHelper();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_recycler_item,
                parent, false);
        view.setTag("reportsRecyclerView");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mReportTitle.setText(reportTitle.get(position));
            holder.mReportText.setText(reportText.get(position));
            holder.mReportTime.setText(reportTime.get(position));
            if(reportTitle.get(position).contains("BPM")){
                holder.mReportImage.setImageResource(R.drawable.heartbeat);
            }
            if(reportTitle.get(position).contains("Fall")){
                holder.mReportImage.setImageResource(R.drawable.falling);
            }
    }

    @Override
    public int getItemCount() {
        return reportTitle.size();
    }

    public void delete(final int position){
        reportTitle.remove(position);
        reportText.remove(position);
        reportTime.remove(position);
        notifyItemRemoved(position);

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

    public class ViewHolder extends RecyclerView.ViewHolder{

        protected ImageView mReportImage;
        protected ImageView mDeleteReportImage;
        protected TextView mReportTitle;
        protected TextView mReportText;
        protected TextView mReportTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mReportImage = itemView.findViewById(R.id.patientImage);
            mDeleteReportImage = itemView.findViewById(R.id.deleteReport);
            mReportTitle = itemView.findViewById(R.id.reportTitle);
            mReportText = itemView.findViewById(R.id.reportText);
            mReportTime = itemView.findViewById(R.id.reportTime);

            mDeleteReportImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(getAdapterPosition());
                }
            });
        }
    }
}
