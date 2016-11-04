package com.example.aj.scavengersworld.DatabaseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.aj.scavengersworld.Model.Location;

/**
 * Created by aj on 11/2/16.
 */
public class SearchableHunt implements Parcelable {
    private String huntName;
    private int numberOfPlayers;
    private Location locationOfFirstClue;
    private boolean privateHunt;
    private double maxRadius;

    public SearchableHunt(){

    }

    public String getHuntName() {
        return huntName;
    }

    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Location getLocationOfFirstClue() {
        return locationOfFirstClue;
    }

    public void setLocationOfFirstClue(Location locationOfFirstClue) {
        this.locationOfFirstClue = locationOfFirstClue;
    }

    public boolean isPrivateHunt() {
        return privateHunt;
    }

    public void setPrivateHunt(boolean privateHunt) {
        this.privateHunt = privateHunt;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(double maxRadius) {
        this.maxRadius = maxRadius;
    }

    protected SearchableHunt(Parcel in) {
        huntName = in.readString();
        numberOfPlayers = in.readInt();
        locationOfFirstClue = in.readParcelable(Location.class.getClassLoader());
//        locationOfFirstClue = (Location) in.readValue(Location.class.getClassLoader());
        privateHunt = in.readByte() != 0x00;
        maxRadius = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(huntName);
        dest.writeInt(numberOfPlayers);
        dest.writeParcelable(locationOfFirstClue, flags);
//        dest.writeValue(locationOfFirstClue);
        dest.writeByte((byte) (privateHunt ? 0x01 : 0x00));
        dest.writeDouble(maxRadius);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SearchableHunt> CREATOR = new Parcelable.Creator<SearchableHunt>() {
        @Override
        public SearchableHunt createFromParcel(Parcel in) {
            return new SearchableHunt(in);
        }

        @Override
        public SearchableHunt[] newArray(int size) {
            return new SearchableHunt[size];
        }
    };
}
