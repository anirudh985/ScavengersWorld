package com.example.aj.scavengersworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kalyan on 10/13/16.
 */

public class Clue implements Parcelable {
    private int clueId;
    private String huntName;
    private int previousClueId;
    private int nextClueId;
    private String clueTitle;
    private String clueDescription;
    private Location location;
    private String landmarkDescription;
    private int sequenceNumberInHunt;

    public Clue(){

    }

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

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location mGPSLocation) {
        this.location = mGPSLocation;
    }

    public String getLandmarkDescription() {
        return landmarkDescription;
    }

    public void setLandmarkDescription(String mLandmarkDescription) {
        this.landmarkDescription = mLandmarkDescription;
    }

    protected Clue(Parcel in) {
        clueId = in.readInt();
        huntName = in.readString();
        previousClueId = in.readInt();
        nextClueId = in.readInt();
        clueTitle = in.readString();
        clueDescription = in.readString();
        location = (Location) in.readValue(Location.class.getClassLoader());
        landmarkDescription = in.readString();
        sequenceNumberInHunt = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(clueId);
        dest.writeString(huntName);
        dest.writeInt(previousClueId);
        dest.writeInt(nextClueId);
        dest.writeString(clueTitle);
        dest.writeString(clueDescription);
        dest.writeValue(location);
        dest.writeString(landmarkDescription);
        dest.writeInt(sequenceNumberInHunt);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Clue> CREATOR = new Parcelable.Creator<Clue>() {
        @Override
        public Clue createFromParcel(Parcel in) {
            return new Clue(in);
        }

        @Override
        public Clue[] newArray(int size) {
            return new Clue[size];
        }
    };
}