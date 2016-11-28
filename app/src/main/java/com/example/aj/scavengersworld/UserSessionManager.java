package com.example.aj.scavengersworld;

/**
 * Created by aj on 10/14/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.aj.scavengersworld.Activities.Login.LoginActivity;
import com.example.aj.scavengersworld.DatabaseModels.UserProfile;
import com.example.aj.scavengersworld.DatabaseModels.UserToHunts;
import com.example.aj.scavengersworld.Model.Hunt;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static com.example.aj.scavengersworld.Constants.ADMIN;
import static com.example.aj.scavengersworld.Constants.COMPLETED;
import static com.example.aj.scavengersworld.Constants.INPROGRESS;
import static com.example.aj.scavengersworld.Constants.INVITED;
import static com.example.aj.scavengersworld.Constants.REQUESTED;


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

    private HashMap<String, Hunt> adminHunts = new HashMap<>();
    private HashMap<String, Hunt> participatingHunts = new HashMap<>();
    private HashMap<String, Hunt> completedHunts = new HashMap<>();
    private HashMap<String, Hunt> requestedHunts = new HashMap<>();
    private HashMap<String, Hunt> invitedHunts = new HashMap<>();
    private HashMap<String, Hunt> allHunts = new HashMap<>();

    private List<Hunt> adminHuntsList = new ArrayList<>();
    private List<Hunt> participatingHuntsList = new ArrayList<>();
    private List<Hunt> completedHuntsList = new ArrayList<>();
    private List<Hunt> requestedHuntsList = new ArrayList<>();
    private List<Hunt> invitedHuntsList = new ArrayList<>();
    private List<Hunt> allHuntsList = new ArrayList<>();

    private UserProfile userProfile;
    private String userProfileKey;

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

    public String getUserProfileKey(){ return this.userProfileKey; }

    public void setUpSession(FirebaseUser firebaseUser, Context context){
        if(firebaseUser != null && context != null){
            this.mContext = context;
            this.mFirebaseUser = firebaseUser;
            this.mUniqueUserId = firebaseUser.getUid();
            this.mUserName = firebaseUser.getDisplayName();
            this.mUserEmail = firebaseUser.getEmail();
        }
    }

    public void updateHunts(@NonNull List<UserToHunts> listOfUserToHunts){
        for(UserToHunts userToHunts : listOfUserToHunts) {
            if (userToHunts.getState().equals(ADMIN)) {
                // update Created Hunts
                if (adminHunts.containsKey(userToHunts.getHuntName())) {
                    Hunt hunt = adminHunts.get(userToHunts.getHuntName());
                    updateHuntObject(hunt, userToHunts);
                } else {
                    Hunt hunt = createHuntObject(userToHunts);
                    adminHunts.put(userToHunts.getHuntName(), hunt);
                    //TODO: need to add to adminHuntsList and sort OR some other mechanism if we want to
                    //TODO  display the hunts in sorted order
                    adminHuntsList.add(hunt);
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
                if (requestedHunts.containsKey(userToHunts.getHuntName())) {
                    Hunt hunt = requestedHunts.get(userToHunts.getHuntName());
                    updateHuntObject(hunt, userToHunts);
                } else {
                    Hunt hunt = createHuntObject(userToHunts);
                    requestedHunts.put(userToHunts.getHuntName(), hunt);
                    //TODO: need to add to requestedHuntsList and sort OR some other mechanism if we want to
                    //TODO  display the hunts in sorted order
                   requestedHuntsList.add(hunt);
                }
            } else if (userToHunts.getState().equals(INVITED)) {
                if (invitedHunts.containsKey(userToHunts.getHuntName())) {
                    Hunt hunt = invitedHunts.get(userToHunts.getHuntName());
                    updateHuntObject(hunt, userToHunts);
                } else {
                    Hunt hunt = createHuntObject(userToHunts);
                    invitedHunts.put(userToHunts.getHuntName(), hunt);
                    //TODO: need to add to participatingHuntsList and sort OR some other mechanism if we want to
                    //TODO  display the hunts in sorted order
                    invitedHuntsList.add(hunt);
                }
            } else if (userToHunts.getState().equals(COMPLETED)) {
                // update Created Hunts
                if (completedHunts.containsKey(userToHunts.getHuntName())) {
                    Hunt hunt = completedHunts.get(userToHunts.getHuntName());
                    updateHuntObject(hunt, userToHunts);
                } else {
                    Hunt hunt = createHuntObject(userToHunts);
                    completedHunts.put(userToHunts.getHuntName(), hunt);
                    //TODO: need to add to completedHunts and sort OR some other mechanism if we want to
                    //TODO  display the hunts in sorted order
                    completedHuntsList.add(hunt);
                }
            }
        }
    }

    private void updateHuntObject(@NonNull Hunt hunt, @NonNull UserToHunts userToHunts){
        hunt.setHuntName(userToHunts.getHuntName());
        hunt.setCurrentClueSequence(userToHunts.getCurrentClueSequence());
        hunt.setState(userToHunts.getState());
        hunt.setScore(userToHunts.getScore());
        hunt.setProgress(userToHunts.getProgress());
    }

    private Hunt createHuntObject(@NonNull UserToHunts userToHunts){
        Hunt hunt = new Hunt();
        updateHuntObject(hunt, userToHunts);
        return hunt;
    }

    public void updateUserProfile(@NonNull UserProfile userProfile, @NonNull String key){
        this.userProfile = userProfile;
        this.userProfileKey = key;
    }

    public UserProfile getUserProfile(){
        return userProfile;
    }

    public List<Hunt> getAdminHunts(){
        return adminHuntsList;
    }
    public Hunt getParticipatingHuntByName(String huntName){
        return participatingHunts.get(huntName);
    }
	public Hunt getAdminHuntByName(String huntName){
		return adminHunts.get(huntName);
	}
	public Hunt getCompletedHuntByName(String huntName){
		return completedHunts.get(huntName);
	}
    public Hunt getInvitedHuntByName(String huntName){
        return invitedHunts.get(huntName);
    }
    public Hunt getRequestedHuntByName(String huntName){
        return requestedHunts.get(huntName);
    }
    public List<Hunt> getParticipatingHuntsList(){
        return participatingHuntsList;
    }

    public List<Hunt> getCompletedHuntsList(){
        return completedHuntsList;
    }

    public String getHuntStatusByName(String huntName) {
        if (participatingHunts.get(huntName) != null) {
            return INPROGRESS;
        } else if (adminHunts.get(huntName) != null) {
            return ADMIN;
        } else if(completedHunts.get(huntName) != null) {
            return COMPLETED;
        } else if(requestedHunts.get(huntName) != null) {
            return REQUESTED;
        } else if(invitedHunts.get(huntName) != null) {
            return INVITED;
        } else {
            return null;
        }
    }

    public void clearSession(){
        this.mFirebaseUser = null;
        this.mUserEmail = null;
        this.mUserName = null;
        this.mUniqueUserId = null;
        this.mContext = null;
        this.userProfile = null;

        this.adminHunts = new HashMap<>();
        this.participatingHunts = new HashMap<>();
        this.completedHunts = new HashMap<>();

        this.adminHuntsList = new ArrayList<>();
        this.participatingHuntsList = new ArrayList<>();
        this.completedHuntsList = new ArrayList<>();
    }

    public void addHunt(String typeOfHunt, Hunt hunt){
        if(hunt != null){
            if(typeOfHunt.equals(INPROGRESS)){
                participatingHunts.put(hunt.getHuntName(), hunt);
                participatingHuntsList.add(hunt);
            }
            else if(typeOfHunt.equals(ADMIN)){
                adminHunts.put(hunt.getHuntName(), hunt);
                adminHuntsList.add(hunt);
            }
            else if(typeOfHunt.equals(COMPLETED)){
                completedHunts.put(hunt.getHuntName(), hunt);
                completedHuntsList.add(hunt);
            }
        }
    }

    public void removeHunt(String typeOfHunt, Hunt hunt){
        if(hunt != null){
            if(typeOfHunt.equals(INPROGRESS)){
                participatingHunts.remove(hunt.getHuntName());
                participatingHuntsList.remove(hunt);
            }
            else if(typeOfHunt.equals(ADMIN)){
                adminHunts.remove(hunt.getHuntName());
                adminHuntsList.remove(hunt);
            }
            else if(typeOfHunt.equals(COMPLETED)){
                completedHunts.remove(hunt.getHuntName());
                completedHuntsList.remove(hunt);
            }
        }
    }

    public List<Hunt> getAllHuntsList(){
        allHuntsList.clear();
        allHuntsList.addAll(participatingHuntsList);
//        allHuntsList.addAll(adminHuntsList);
        allHuntsList.addAll(completedHuntsList);
        return allHuntsList;
    }

    public HashMap<String, Hunt> getAllHunts(){
        allHunts.clear();
        allHunts.putAll(participatingHunts);
//        allHunts.putAll(adminHunts);
        allHunts.putAll(completedHunts);
        return allHunts;
    }

    private class SendNotificationThread extends Thread{
        private String userToken;
        private String title;
        private String body;

        public SendNotificationThread(String userToken, String title, String body){
            this.userToken = userToken;
            this.title = title;
            this.body = body;
        }

        @Override
        public void run(){
            sendNotification(userToken, title, body);
        }

        private void sendNotification(String userToken, String title, String body){
            String serverKey = "AIzaSyBr6hwMLMGdoQFF92_RTY3jXlNnwmnFK3s";
            try{
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);

                // HTTP request header
                //con.setRequestProperty("project_id", senderId);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Authorization", "key="+serverKey);
                con.setRequestMethod("POST");
                con.connect();

                JSONObject messagedata = new JSONObject();
                messagedata.put("body",body);
                messagedata.put("title",title);
                //messagedata.put("body","This is a Firebase Cloud Messaging Device Group Message!");
                // HTTP request
                JSONObject data = new JSONObject();
                data.put("to", userToken);
                data.put("notification", messagedata);
                // data.put("registration_ids", new JSONArray(Arrays.asList(registrationId)));
                //data.put("id_token", idToken);

                OutputStream os = con.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.close();

                // Read the response into a string
                InputStream is = con.getInputStream();
                String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
                is.close();

                // Parse the JSON string and return the notification key
                JSONObject response = new JSONObject(responseString);
                //return response.getString("notification_key");
            }catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendNotification(String userToken, String title, String body){
        if(userToken != null && title != null && body != null){
            SendNotificationThread notificationThread = new SendNotificationThread(userToken, title, body);
            notificationThread.start();
        }
    }

}

