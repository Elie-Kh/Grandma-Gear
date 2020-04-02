package com.example.grandmagear;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;

public class SharedPreferencesHelper {

    private SharedPreferences mSharedPreferences;

    public SharedPreferencesHelper(Context context, String pref){
        mSharedPreferences = context.getSharedPreferences(pref,
                Context.MODE_PRIVATE);
    };

    public void saveEmail(String email){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    public void saveType(Boolean type){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("type", type.toString());
        editor.apply();
    }

    public void savePassword(String password){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("password", password);
        editor.apply();
    }

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

    public void savePatientsList(ArrayList<String> IDs){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet("PatientIDs", new HashSet<String>(IDs));
        editor.apply();
    }

    public void saveNotificationTitle(ArrayList<String> title){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet("Notification Title", new HashSet<String>(title));
        editor.apply();
    }

    public void saveNotificationText(ArrayList<String> text){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet("Notification Text", new HashSet<String>(text));
        editor.apply();
    }

    public void saveNotificationTime(ArrayList<Long> time){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        HashSet<String> temp = new HashSet<String>(time.size());
        for(long longs : time){
            temp.add(time.toString());
        }
        editor.putStringSet("Notification Time", new HashSet<String>(temp));
        editor.apply();
    }

    public ArrayList<String> getNotificationTitle(){
        ArrayList<String> notificationTitle = new ArrayList<String>
                (mSharedPreferences.getStringSet("Notification Title", new HashSet<String>()));
        if(notificationTitle.size() < 1){
            return new ArrayList<>();
        }else{
            return notificationTitle;
        }
    }

    public ArrayList<Long> getNotificationTime(){
        ArrayList<Long> time = new ArrayList<Long>();
        ArrayList<String> notificationTime = new ArrayList<String>(
                mSharedPreferences.getStringSet("Notification Time", new HashSet<String>())
        );
        if(notificationTime.size() < 1){
            return new ArrayList<>();
        }else{
            for(String nt : notificationTime)
                time.add(Long.valueOf(nt));
            return time;
        }
    }

    public String getEmail(){
        return mSharedPreferences.getString("email",null);
    }

    public String getType(){
        return mSharedPreferences.getString("type",null);
    }

    public String getPassword(){
        return mSharedPreferences.getString("password",null);
    }

    public ArrayList<String> getNotificationText(){
        ArrayList<String> notificationText = new ArrayList<String>
                (mSharedPreferences.getStringSet("Notification Text", new HashSet<String>()));
        if(notificationText.size() < 1){
            return new ArrayList<>();
        }else{
            return notificationText;
        }
    }

    public ArrayList<String> getIDs(){
        ArrayList<String> IDs = new ArrayList<String>
                (mSharedPreferences.getStringSet("PatientIDs",new HashSet<String>()));
        if(IDs.size() < 1){
            return new ArrayList<>();
        }
        else {
            return IDs;
        }
    }
}
