package com.example.aj.scavengersworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kalyan on 10/13/16.
 * Modified by anirudh on 11/01/16.
 */

public class Hunt implements Comparable<Hunt>, Parcelable {
    private int mHuntId;
    private String mHuntName;
    private String mCreatedByUserId;
    private Date mStartTime;
    private Date mEndTime;
    //private Boolean miIsTeam;
    private Boolean mIsPrivate;
    private List<User> mListOfAdmins;
    private int mFirstClueId;
    private String mCurrentClueId;

    private String mState;
    private int mScore;
    private double mProgress;

    public Hunt(){

    }

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

    public String getCurrentClueId() {
        return mCurrentClueId;
    }

    public void setCurrentClueId(String currentClueId) {
        this.mCurrentClueId = currentClueId;
    }


    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        this.mScore = score;
    }

    public double getProgress() {
        return mProgress;
    }

    public void setProgress(double mProgress) {
        this.mProgress = mProgress;
    }

    @Override
    public String toString(){
        return String.valueOf(mHuntId) + mHuntName + mScore;
    }

    @Override
    public int compareTo(Hunt hunt) {
        return mHuntName.compareTo(hunt.getHuntName());
    }

    protected Hunt(Parcel in) {
        mHuntId = in.readInt();
        mHuntName = in.readString();
        mCreatedByUserId = in.readString();
        long tmpMStartTime = in.readLong();
        mStartTime = tmpMStartTime != -1 ? new Date(tmpMStartTime) : null;
        long tmpMEndTime = in.readLong();
        mEndTime = tmpMEndTime != -1 ? new Date(tmpMEndTime) : null;
        byte mIsPrivateVal = in.readByte();
        mIsPrivate = mIsPrivateVal == 0x02 ? null : mIsPrivateVal != 0x00;
        if (in.readByte() == 0x01) {
            mListOfAdmins = new ArrayList<User>();
            in.readList(mListOfAdmins, User.class.getClassLoader());
        } else {
            mListOfAdmins = null;
        }
        mFirstClueId = in.readInt();
        mCurrentClueId = in.readString();
        mState = in.readString();
        mScore = in.readInt();
        mProgress = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mHuntId);
        dest.writeString(mHuntName);
        dest.writeString(mCreatedByUserId);
        dest.writeLong(mStartTime != null ? mStartTime.getTime() : -1L);
        dest.writeLong(mEndTime != null ? mEndTime.getTime() : -1L);
        if (mIsPrivate == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (mIsPrivate ? 0x01 : 0x00));
        }
        if (mListOfAdmins == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mListOfAdmins);
        }
        dest.writeInt(mFirstClueId);
        dest.writeString(mCurrentClueId);
        dest.writeString(mState);
        dest.writeInt(mScore);
        dest.writeDouble(mProgress);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Hunt> CREATOR = new Parcelable.Creator<Hunt>() {
        @Override
        public Hunt createFromParcel(Parcel in) {
            return new Hunt(in);
        }

        @Override
        public Hunt[] newArray(int size) {
            return new Hunt[size];
        }
    };
}