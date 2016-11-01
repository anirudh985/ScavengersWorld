package com.example.aj.scavengersworld;

/**
 * Created by aj on 10/14/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.aj.scavengersworld.Activities.Login.LoginActivity;
import com.example.aj.scavengersworld.DatabaseModels.UserToHunts;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.Model.User;
import com.google.firebase.auth.FirebaseUser;

import static com.example.aj.scavengersworld.Constants.ADMIN;
import static com.example.aj.scavengersworld.Constants.INPROGRESS;
import static com.example.aj.scavengersworld.Constants.COMPLETED;
import static com.example.aj.scavengersworld.Constants.REQUESTED;
import static com.example.aj.scavengersworld.Constants.INVITED;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public enum UserSessionManager {
    INSTANCE;

    private FirebaseUser mFirebaseUser;
    private String mUniqueUserId;
    private String mUserName;
    private String mUserEmail;



    //    private SharedPreferences mPref;
//    private Editor mEditor;
    private Context mContext;
//    private int PRIVATE_MODE = 0;
//
//    private static final String PREF_NAME = "Scavengers";
//    private static final String IS_LOGIN = "IsLoggedIn";
//    public static final String KEY_NAME = "username";
//    public static final String KEY_EMAIL = "email";

//    public UserSessionManager getSession(Context context){
//        this.mContext = context;
//        mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
//        mEditor = mPref.edit();
//        return this;
//    }

//    public void createLoginSession(User user){
//        mEditor.putBoolean(IS_LOGIN, true);
//        mEditor.putString(KEY_NAME, user.getUserName());
//        mEditor.putString(KEY_EMAIL, user.getUserEmail());
//        mEditor.commit();
//    }

    private HashMap<String, Hunt> createdHunts = new HashMap<>();
    private HashMap<String, Hunt> participatingHunts = new HashMap<>();

    private List<Hunt> createdHuntsList = new ArrayList<>();
    private List<Hunt> participatingHuntsList = new ArrayList<>();

    public void checkLogin(){
        if(!this.isLoggedIn() && mContext != null){
            Intent i = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(i);
        }
    }

//    public HashMap<String, String> getUserDetails(){
//        HashMap<String, String> user = new HashMap<String, String>();
//        user.put(KEY_NAME, mPref.getString(KEY_NAME, null));
//        user.put(KEY_EMAIL, mPref.getString(KEY_EMAIL, null));
//        return user;
//    }
//
//    public void logoutUser(){
//        mEditor.clear();
//        mEditor.commit();
//        Intent i = new Intent(mContext, LoginActivity.class);
//        mContext.startActivity(i);
//    }

    public boolean isLoggedIn(){
        return this.mFirebaseUser != null;
    }

    public String getUniqueUserId(){
        return this.mUniqueUserId;
    }

    public String getUserEmailId(){
        return this.mUserEmail;
    }

    public String getUserName(){
        return this.mUserName;
    }

    public void setUpSession(FirebaseUser firebaseUser, Context context){
        if(firebaseUser != null && context != null){
            this.mContext = context;
            this.mFirebaseUser = firebaseUser;
            this.mUniqueUserId = firebaseUser.getUid();
            this.mUserName = firebaseUser.getDisplayName();
            this.mUserEmail = firebaseUser.getEmail();
        }
    }

    public void clearSession(){
        this.mFirebaseUser = null;
        this.mContext = null;
        this.mUniqueUserId = null;
        this.mUserName = null;
        this.mUserEmail = null;
    }

    public void updateHunts(@NonNull List<UserToHunts> listOfUserToHunts){
        for(UserToHunts userToHunts : listOfUserToHunts) {
            if (userToHunts.getState().equals(ADMIN)) {
                // update Created Hunts
                if (createdHunts.containsKey(userToHunts.getHuntName())) {
                    Hunt hunt = createdHunts.get(userToHunts.getHuntName());
                    updateHuntObject(hunt, userToHunts);
                } else {
                    Hunt hunt = createHuntObject(userToHunts);
                    createdHunts.put(userToHunts.getHuntName(), hunt);
                    //TODO: need to add to createdHuntsList and sort OR some other mechanism if we want to
                    //TODO  display the hunts in sorted order
                    createdHuntsList.add(hunt);
                }
            } else if (userToHunts.getState().equals(INPROGRESS)) {
                // update Participating Hunts
                if (participatingHunts.containsKey(userToHunts.getHuntName())) {
                    Hunt hunt = participatingHunts.get(userToHunts.getHuntName());
                    updateHuntObject(hunt, userToHunts);
                } else {
                    Hunt hunt = createHuntObject(userToHunts);
                    participatingHunts.put(userToHunts.getHuntName(), hunt);
                    //TODO: need to add to participatingHuntsList and sort OR some other mechanism if we want to
                    //TODO  display the hunts in sorted order
                    participatingHuntsList.add(hunt);
                }
            } else if (userToHunts.getState().equals(REQUESTED)) {

            } else if (userToHunts.getState().equals(INVITED)) {

            } else if (userToHunts.getState().equals(COMPLETED)) {

            }
        }
    }

    private void updateHuntObject(@NonNull Hunt hunt, @NonNull UserToHunts userToHunts){
        hunt.setHuntName(userToHunts.getHuntName());
        hunt.setCurrentClueId(userToHunts.getClueId());
        hunt.setState(userToHunts.getState());
        hunt.setScore(userToHunts.getScore());
        hunt.setProgress(userToHunts.getProgress());
    }

    private Hunt createHuntObject(@NonNull UserToHunts userToHunts){
        Hunt hunt = new Hunt();
        updateHuntObject(hunt, userToHunts);
        return hunt;
    }

    public List<Hunt> getCreatedHunts(){
        return createdHuntsList;
    }

    public List<Hunt> getParticipatingHuntsList(){
        return participatingHuntsList;
    }
}

