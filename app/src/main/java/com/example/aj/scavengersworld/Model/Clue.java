package com.example.aj.scavengersworld.Model;

/**
 * Created by kalyan on 10/13/16.
 */

public class Clue {
    private int clueId;
    private String huntName;
    private int previousClueId;
    private int nextClueId;
    private String clueTitle;
    private String clueDescription;
//    private enum mClueType{
//        LOCATION ,
//        BEACON;
//    }

    //private String mBeaconId;
    private Location location;
    private String landmarkDescription;
//    private int mEventId;
    private int sequenceNumberInHunt;



    public int getClueId() {
        return clueId;
    }

    public void setClueId(int clueId) {
        this.clueId = clueId;
    }

    public String getClueTitle() {
        return clueTitle;
    }

    public void setClueTitle(String clueTitle) {
        this.clueTitle = clueTitle;
    }

    public String getClueDescription() {
        return clueDescription;
    }

    public void setClueDescription(String clueDescription) {
        this.clueDescription = clueDescription;
    }

    public int getPreviousClueId() {
        return previousClueId;
    }

    public void setPreviousClueId(int previousClueId) {
        this.previousClueId = previousClueId;
    }

    public int getNextClueId() {
        return nextClueId;
    }

    public void setNextClueId(int nextClueId) {
        this.nextClueId = nextClueId;
    }

    public int getSequenceNumberInHunt() {
        return sequenceNumberInHunt;
    }

    public void setSequenceNumberInHunt(int sequenceNumberInHunt) {
        this.sequenceNumberInHunt = sequenceNumberInHunt;
    }

    public String getHuntName() {
        return huntName;
    }

    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

//    public int getClueId() {
//        return mClueId;
//    }
//
//    public void setClueId(int mClueId) {
//        this.mClueId = mClueId;
//    }
//
//    public String getClueTitle() {
//        return mClueTitle;
//    }
//
//    public void setClueTitle(String mClueTitle) {
//        this.mClueTitle = mClueTitle;
//    }
//
//    public String getClueDescription() {
//        return mClueDescription;
//    }
//
//    public void setClueDescription(String mClueDescription) {
//        this.mClueDescription = mClueDescription;
//    }
//
//    public int getPreviousClueId() {
//        return mPreviousClueId;
//    }
//
//    public void setPreviousClueId(int mPreviousClueId) {
//        this.mPreviousClueId = mPreviousClueId;
//    }
//
//    public int getNextClueId() {
//        return mNextClueId;
//    }
//
//    public void setNextClueId(int mNextClueId) {
//        this.mNextClueId = mNextClueId;
//    }
//
//    public String getBeaconId() {
//        return mBeaconId;
//    }
//
//    public void setBeaconId(String mBeaconId) {
//        this.mBeaconId = mBeaconId;
//    }
//
    public Location getLocation() {
        return this.location;
    }

    public void setGPSLocation(Location mGPSLocation) {
        this.location = mGPSLocation;
    }
//
    public String getLandmarkDescription() {
        return landmarkDescription;
    }

    public void setLandmarkDescription(String mLandmarkDescription) {
        this.landmarkDescription = mLandmarkDescription;
    }
//
//    public int getEventId() {
//        return mEventId;
//    }
//
//    public void setEventId(int mEventId) {
//        this.mEventId = mEventId;
//    }
//
//    public int getClueSequenceNumberInHunt() {
//        return mClueSequenceNumberInHunt;
//    }
//
//    public void setClueSequenceNumberInHunt(int mClueSequenceNumberInHunt) {
//        this.mClueSequenceNumberInHunt = mClueSequenceNumberInHunt;
//    }
}
