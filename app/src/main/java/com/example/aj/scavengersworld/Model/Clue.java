package com.example.aj.scavengersworld.Model;

/**
 * Created by kalyan on 10/13/16.
 */

public class Clue {
    private int mClueId;
    private String mClueTitle;
    private String mClueDescription;
    private enum mClueType{
        LOCATION ,
        BEACON;
    }
    private int mPreviousClueId;
    private int mNextClueId;
    private String mBeaconId;
    private Location mGPSLocation;
    private String mLandmarkDescription;
    private int mEventId;
    private int mClueSequenceNumberInHunt;


    public int getClueId() {
        return mClueId;
    }

    public void setClueId(int mClueId) {
        this.mClueId = mClueId;
    }

    public String getClueTitle() {
        return mClueTitle;
    }

    public void setClueTitle(String mClueTitle) {
        this.mClueTitle = mClueTitle;
    }

    public String getClueDescription() {
        return mClueDescription;
    }

    public void setClueDescription(String mClueDescription) {
        this.mClueDescription = mClueDescription;
    }

    public int getPreviousClueId() {
        return mPreviousClueId;
    }

    public void setPreviousClueId(int mPreviousClueId) {
        this.mPreviousClueId = mPreviousClueId;
    }

    public int getNextClueId() {
        return mNextClueId;
    }

    public void setNextClueId(int mNextClueId) {
        this.mNextClueId = mNextClueId;
    }

    public String getBeaconId() {
        return mBeaconId;
    }

    public void setBeaconId(String mBeaconId) {
        this.mBeaconId = mBeaconId;
    }

    public Location getGPSLocation() {
        return mGPSLocation;
    }

    public void setGPSLocation(Location mGPSLocation) {
        this.mGPSLocation = mGPSLocation;
    }

    public String getLandmarkDescription() {
        return mLandmarkDescription;
    }

    public void setLandmarkDescription(String mLandmarkDescription) {
        this.mLandmarkDescription = mLandmarkDescription;
    }

    public int getEventId() {
        return mEventId;
    }

    public void setEventId(int mEventId) {
        this.mEventId = mEventId;
    }

    public int getClueSequenceNumberInHunt() {
        return mClueSequenceNumberInHunt;
    }

    public void setClueSequenceNumberInHunt(int mClueSequenceNumberInHunt) {
        this.mClueSequenceNumberInHunt = mClueSequenceNumberInHunt;
    }
}
