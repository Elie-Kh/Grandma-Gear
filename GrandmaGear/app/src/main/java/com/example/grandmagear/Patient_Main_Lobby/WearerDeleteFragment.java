package com.example.grandmagear.Patient_Main_Lobby;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.grandmagear.R;
import com.example.grandmagear.RecyclerViewAdapter;

public class WearerDeleteFragment extends DialogFragment {
    protected TextView warningHeader;
    protected TextView warningMessageText;
    protected Button confirmButton;
    protected Button cancelButton;
    protected RecyclerViewAdapter recyclerView;
    protected int position;

    public WearerDeleteFragment(RecyclerViewAdapter recyclerView, int position) {
        this.position = position;
        this.recyclerView = recyclerView;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @javax.annotation.Nullable ViewGroup container,
                             @javax.annotation.Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.delete_wearer_fragment, container, false);

        setupLayout(view);
        return view;
    }

    public void setupLayout(View view){
        warningHeader = view.findViewById(R.id.warningLabelTextViewW);
        warningMessageText = view.findViewById(R.id.warningTextViewW);
        confirmButton = view.findViewById(R.id.confirmDeleteButtonW);
        cancelButton = view.findViewById(R.id.cancelDeleteButtonW);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.delete(position);
                getDialog().dismiss();
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
