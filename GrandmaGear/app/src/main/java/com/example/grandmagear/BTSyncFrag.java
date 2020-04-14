package com.example.grandmagear;

import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.DialogFragment;

import javax.annotation.Nullable;

public class BTSyncFrag extends DialogFragment {

    protected TextView warning;
    protected TextView disclaimer;
    protected Button continueButton;
    protected Button cancelButton;
    protected SharedPreferencesHelper mSharedPreferences;
    private Context context;

    public BTSyncFrag(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.bt_sync_disclaimer, container, false);

        if(getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setupLayout(view);
        return view;
    }

    protected void setupLayout(View view){
        warning = view.findViewById(R.id.warningLabelTextViewW);
        disclaimer = view.findViewById(R.id.warningTextViewS);
        continueButton = view.findViewById(R.id.continueRegButton);
        cancelButton = view.findViewById(R.id.cancelRegButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
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
