package com.example.aj.scavengersworld.Model;

import java.util.List;

/**
 * Created by kalyan on 10/13/16.
 */

public class User {
    private String mUserId;
    private String mUserName;
    private String mUserEmail;
    private List<Hunt> mCreatedHuntList;
    private List<Hunt> mRegisteredHuntList;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String mUserEmail) {
        this.mUserEmail = mUserEmail;
    }

    public List<Hunt> getCreatedHuntList() {
        return mCreatedHuntList;
    }

    public void setCreatedHuntList(List<Hunt> mCreatedHuntList) {
        this.mCreatedHuntList = mCreatedHuntList;
    }

    public List<Hunt> getRegisteredHuntList() {
        return mRegisteredHuntList;
    }

    public void setRegisteredHuntList(List<Hunt> mRegisteredHuntList) {
        this.mRegisteredHuntList = mRegisteredHuntList;
    }
}
