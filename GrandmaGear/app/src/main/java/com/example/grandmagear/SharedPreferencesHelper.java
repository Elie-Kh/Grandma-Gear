package com.example.grandmagear;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
        Gson gson = new Gson();
        String json = gson.toJson(IDs);
        editor.putString("PatientIDs", json);
        editor.apply();
    }

    public void saveNotificationTitle(ArrayList<String> title){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(title);
        editor.putString("Notification Title", json);
        editor.apply();
    }

    public void saveNotificationText(ArrayList<String> text){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(text);
        editor.putString("Notification Text", json);
        editor.apply();
    }

    public void saveNotificationTime(ArrayList<String> time){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(time);
        editor.putString("Notification Time", json);
        editor.apply();
    }

    public ArrayList<String> getNotificationTitle(){
        Gson gson = new Gson();
        String response = mSharedPreferences.getString("Notification Title", "");
        ArrayList<String> title = gson.fromJson(response, new TypeToken<List<String>>() {
        }.getType());
        return title;
    }

    public ArrayList<String> getNotificationTime(){
       Gson gson = new Gson();
       String response = mSharedPreferences.getString("Notification Time", "");
       ArrayList<String> time = gson.fromJson(response, new TypeToken<List<String>>(){}.getType());
       return time;
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
        Gson gson = new Gson();
        String response = mSharedPreferences.getString("Notification Text", "");
        ArrayList<String> text = gson.fromJson(response, new TypeToken<List<String>>() {
        }.getType());
        return text;
    }

    public ArrayList<String> getIDs(){
        Gson gson = new Gson();
        String response = mSharedPreferences.getString("PatientIDs", "");
        ArrayList<String> IDs = gson.fromJson(response, new TypeToken<List<String>>(){}.getType());
        return IDs;
    }
}
