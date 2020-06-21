package com.travisit.travisitbusiness.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.travisit.travisitbusiness.model.Business;

import javax.inject.Inject;
import javax.inject.Singleton;

//@Singleton
public class SharedPrefManager {

    //Shared Preference field used to save and retrieve JSON string
    private SharedPreferences sharedPreferences;

    //Name of Shared Preference file
    private String PREFERENCES_FILE_NAME = "PREFERENCES_TRAVISIT_BUSINESS";

//    @Inject
    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences("Preferences", 0);
    }

    /**
     * Saves object into the Preferences.
     *
     **/
    public void saveUser(Business business){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(business);
        prefsEditor.putString("User", json);
        prefsEditor.commit();
    }
    /**
     * Saves object into the Preferences.
     *
     **/
    public void saveUser(String token){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Business emptyBusiness = new Business(token);
        Gson gson = new Gson();
        String json = gson.toJson(emptyBusiness);
        prefsEditor.putString("User", json);
        prefsEditor.commit();
    }
    public void savePasswordResetCode(String code){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("reset_code", code);
        prefsEditor.commit();
    }
    public String getPasswordResetCode(){
        return sharedPreferences.getString("reset_code", null);
    }
    public Business getUser(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("User", "");
        Business business = gson.fromJson(json, Business.class);
        return business;
    }
    public String getUserToken(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("User", "");
        Business business = gson.fromJson(json, Business.class);
        return business.getToken();
    }

//    public void setNotificationsEnabled(boolean enabled){
//        SharedPreferences.Editor edit = sharedPreferences.edit();
//        edit.putBoolean(NOTIFICATIONS_ENABLED, enabled);
//        edit.commit();
//    }
//
//    public boolean isNotificationsEnabled(){
//        return sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED, true);
//    }



}
