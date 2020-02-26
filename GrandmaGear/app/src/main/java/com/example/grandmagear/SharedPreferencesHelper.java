package com.example.grandmagear;

import android.content.ContentQueryMap;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private SharedPreferences mSharedPreferences;

    public SharedPreferencesHelper(Context context){
        mSharedPreferences = context.getSharedPreferences("DisclaimerPreferences",
                Context.MODE_PRIVATE);
    };

    public void saveDisclaimerStatus(boolean stat){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if(stat){
            editor.putString("disclaimerStatus", "True");
        }
        else {
            editor.putString("disclaimerStatus","False");
        }
        editor.apply();
    }

    public String getDisclaimerStatus(){
        return mSharedPreferences.getString("disclaimerStatus", null);
    };
}
