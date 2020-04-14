package com.example.grandmagear;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.grandmagear.Patient_Main_Lobby.ReportsViewAdapter;

public class NotifDeleteFragment extends DialogFragment {

    protected TextView warningHeader;
    protected TextView warningText;
    protected Button confirmButton;
    protected Button cancelButton;
    private int position;
    private NotificationsRecyclerView recyclerView;
    private ReportsViewAdapter recyclerView2;

    public NotifDeleteFragment(NotificationsRecyclerView recyclerView, int position) {
        this.position = position;
        this.recyclerView = recyclerView;
    }

    public NotifDeleteFragment(ReportsViewAdapter recyclerView, int position) {
        this.position = position;
        this.recyclerView2 = recyclerView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @javax.annotation.Nullable ViewGroup container,
                             @javax.annotation.Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.delete_notif_fragment, container, false);

        if(getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setupLayout(view);
        return view;
    }

    public void setupLayout(View view){
        warningHeader = view.findViewById(R.id.warningLabelTextViewW);
        warningText = view.findViewById(R.id.warningTextViewN);
        confirmButton = view.findViewById(R.id.confirmDeleteNotifButton);
        cancelButton = view.findViewById(R.id.cancelDeleteNotifButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerView != null){
                    recyclerView.delete(position);
                    getDialog().dismiss();
                } else if(recyclerView2 != null){
                    recyclerView2.delete(position);
                    getDialog().dismiss();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }
}
