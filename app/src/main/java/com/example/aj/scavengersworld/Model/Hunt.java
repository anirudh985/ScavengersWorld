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
    private int huntId;
    private String huntName;
    private String description;
    private String createdByUserId;
    private Date startTime;
    private Date endTime;
    //private Boolean miIsTeam;
    private Boolean isPrivate;
    private List<User> listOfAdmins;
    private int firstClueId;
    private int currentClueId;

    private String state;
    private int score;
    private double progress;

    private List<Clue> clueList;

    public Hunt(){

    }

    public List<Clue> getClueList() {
        return clueList;
    }

    public void setClueList(List<Clue> clueList) {
        this.clueList = clueList;
    }
    public void addClueToClueList(Clue aclue){
        if(this.clueList == null)
            this.clueList = new ArrayList<>();
        this.clueList.add(aclue);
    }

    public int getHuntId() {
        return huntId;
    }

    public void setHuntId(int mHuntId) {
        this.huntId = mHuntId;
    }

    public String getHuntName() {
        return huntName;
    }

    public void setHuntName(String mHuntName) {
        this.huntName = mHuntName;
    }

	public String getDescription() {return description;}

	public void setDescription(String description) {this.description = description;}

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<User> getListOfAdmins() {
        return listOfAdmins;
    }

    public void setListOfAdmins(List<User> listOfAdmins) {
        this.listOfAdmins = listOfAdmins;
    }

    public int getFirstClueId() {
        return firstClueId;
    }

    public void setFirstClueId(int firstClueId) {
        this.firstClueId = firstClueId;
    }

    public int getCurrentClueId() {
        return currentClueId;
    }

    public void setCurrentClueId(int currentClueId) {
        this.currentClueId = currentClueId;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double mProgress) {
        this.progress = mProgress;
    }

    @Override
    public String toString(){
        return String.valueOf(huntId) + huntName + score;
    }

    @Override
    public int compareTo(Hunt hunt) {
        return huntName.compareTo(hunt.getHuntName());
    }

    protected Hunt(Parcel in) {
        huntId = in.readInt();
        huntName = in.readString();
        createdByUserId = in.readString();
        long tmpMStartTime = in.readLong();
        startTime = tmpMStartTime != -1 ? new Date(tmpMStartTime) : null;
        long tmpMEndTime = in.readLong();
        endTime = tmpMEndTime != -1 ? new Date(tmpMEndTime) : null;
        byte mIsPrivateVal = in.readByte();
        isPrivate = mIsPrivateVal == 0x02 ? null : mIsPrivateVal != 0x00;
        if (in.readByte() == 0x01) {
            listOfAdmins = new ArrayList<User>();
            in.readList(listOfAdmins, User.class.getClassLoader());
        } else {
            listOfAdmins = null;
        }
        firstClueId = in.readInt();
        currentClueId = in.readInt();
        state = in.readString();
        score = in.readInt();
        progress = in.readDouble();
    }
    public Clue getClueWithId(int clueId){
        for(Clue clue : clueList){
            if(clue.getClueId() == clueId)
                return clue;
        }
        return null;
    }
    public  Clue getCurrentClue(){
        return getClueWithId(currentClueId);
    }
    public Clue getClueAtSequence(int sequenceNumber){
        for(Clue clue : clueList){
            if(clue.getSequenceNumberInHunt() == sequenceNumber)
                return clue;
        }
        return null;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(huntId);
        dest.writeString(huntName);
        dest.writeString(createdByUserId);
        dest.writeLong(startTime != null ? startTime.getTime() : -1L);
        dest.writeLong(endTime != null ? endTime.getTime() : -1L);
        if (isPrivate == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isPrivate ? 0x01 : 0x00));
        }
        if (listOfAdmins == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(listOfAdmins);
        }
        dest.writeInt(firstClueId);
        dest.writeInt(currentClueId);
        dest.writeString(state);
        dest.writeInt(score);
        dest.writeDouble(progress);
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