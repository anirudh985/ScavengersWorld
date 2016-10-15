package com.example.aj.scavengersworld;

/**
 * Created by aj on 10/14/16.
 */
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.aj.scavengersworld.Activities.Login.LoginActivity;
import com.example.aj.scavengersworld.Model.User;


public enum SessionManager {
    INSTANCE;

    private SharedPreferences mPref;
    private Editor mEditor;
    private Context mContext;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Scavengers";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "username";
    public static final String KEY_EMAIL = "email";

    public SessionManager getSession(Context context){
        this.mContext = context;
        mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
        return this;
    }

    public void createLoginSession(User user){
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.putString(KEY_NAME, user.getUserName());
        mEditor.putString(KEY_EMAIL, user.getUserEmail());
        mEditor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, mPref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, mPref.getString(KEY_EMAIL, null));
        return user;
    }

    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
        Intent i = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(i);
    }

    public boolean isLoggedIn(){
        return mPref.getBoolean(IS_LOGIN, false);
    }
}
