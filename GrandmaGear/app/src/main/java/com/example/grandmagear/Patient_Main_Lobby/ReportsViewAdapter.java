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

import com.example.grandmagear.R;

import java.util.ArrayList;

public class ReportsViewAdapter extends RecyclerView.Adapter<ReportsViewAdapter.ViewHolder> {

    private ArrayList<String> reportTitle;
    private ArrayList<String> reportText;
    private ArrayList<String> reportTime;

    public ReportsViewAdapter(ArrayList<String> reportTitle, ArrayList<String> reportText, ArrayList<String> reportTime) {
        this.reportTitle = reportTitle;
        this.reportText = reportText;
        this.reportTime = reportTime;
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
    }

    @Override
    public int getItemCount() {
        return reportTitle.size();
    }

    public void delete(int position){
        reportTitle.remove(position);
        reportText.remove(position);
        reportTime.remove(position);
        notifyItemRemoved(position);
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
