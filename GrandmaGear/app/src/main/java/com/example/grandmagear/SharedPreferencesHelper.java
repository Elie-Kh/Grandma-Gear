package com.example.grandmagear;

import android.content.ContentQueryMap;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SharedPreferencesHelper {

    private SharedPreferences mSharedPreferences;

    public SharedPreferencesHelper(Context context, String pref){
        mSharedPreferences = context.getSharedPreferences(pref,
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

    public void savePatientsList(ArrayList<String> IDs){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet("PatientIDs", new HashSet<String>(IDs));
        editor.apply();
    }

    public void saveNotification(ArrayList<String> notification){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet("NotificationIDs", new HashSet<String>(notification));
        editor.apply();
    }

   /* public void saveNotificationText(ArrayList<String> text){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet("Notification Text", new HashSet<String>(text));
        editor.apply();
    }*/

    public ArrayList<String> getNotification(){
        ArrayList<String> notification = new ArrayList<String>
                (mSharedPreferences.getStringSet("NotificationIDs", new HashSet<String>()));
        if(notification.size() < 1){
            return new ArrayList<>();
        }else{
            return notification;
        }
    }

    /*public ArrayList<String> getNotificationText(){
        ArrayList<String> notificationText = new ArrayList<String>
                (mSharedPreferences.getStringSet("Notification Text", new HashSet<String>()));
        if(notificationText.size() < 1){
            return new ArrayList<>();
        }else{
            return notificationText;
        }
    }*/

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
