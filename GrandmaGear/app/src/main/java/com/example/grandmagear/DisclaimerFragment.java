package com.example.grandmagear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DisclaimerFragment extends DialogFragment {

    protected Button mAcceptButton;
    protected Button mDeclineButton;
    protected SharedPreferencesHelper mSharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disclaimer_fragment, container, false);
        setupLayout(view);
        return view;
    }

    protected void setupLayout(View view){
        mAcceptButton = view.findViewById(R.id.accept_disclaimer_button);
        mDeclineButton = view.findViewById(R.id.decline_disclaimer_button);

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //User accept disclaimer. Save it on phone, doesn't ask anymore.
                mSharedPreferences = new SharedPreferencesHelper(getActivity());
                mSharedPreferences.saveDisclaimerStatus(true);
                getDialog().dismiss();
            }
        });

        mDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user declines disclaimer. close app.
                mSharedPreferences = new SharedPreferencesHelper(getActivity());
                mSharedPreferences.saveDisclaimerStatus(false);
                getDialog().dismiss();
                ((MainActivity)getActivity()).finish();
            }
        });
    }

}
