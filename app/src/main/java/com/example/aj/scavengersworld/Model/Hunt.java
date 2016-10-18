package com.example.aj.scavengersworld.Model;

import java.util.Date;
import java.util.List;

/**
 * Created by kalyan on 10/13/16.
 */

public class Hunt {
    private int mHuntId;
    private String mHuntName;
    private String mCreatedByUserId;
    private Date mStartTime;
    private Date mEndTime;
    //private Boolean miIsTeam;
    private Boolean mIsPrivate;
    private List<User> mListOfAdmins;
    private int mFirstClueId;

    public int getHuntId() {
        return mHuntId;
    }

    public void setHuntId(int mHuntId) {
        this.mHuntId = mHuntId;
    }

    public String getHuntName() {
        return mHuntName;
    }

    public void setHuntName(String mHuntName) {
        this.mHuntName = mHuntName;
    }

    public String getCreatedByUserId() {
        return mCreatedByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.mCreatedByUserId = createdByUserId;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Date startTime) {
        this.mStartTime = startTime;
    }

    public Date getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Date endTime) {
        this.mEndTime = endTime;
    }

    public Boolean getPrivate() {
        return mIsPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        mIsPrivate = aPrivate;
    }

    public List<User> getListOfAdmins() {
        return mListOfAdmins;
    }

    public void setListOfAdmins(List<User> listOfAdmins) {
        this.mListOfAdmins = listOfAdmins;
    }

    public int getFirstClueId() {
        return mFirstClueId;
    }

    public void setFirstClueId(int firstClueId) {
        this.mFirstClueId = firstClueId;
    }

    @Override
    public String toString(){
        return String.valueOf(mHuntId) + mHuntName + mCreatedByUserId;
    }
}
