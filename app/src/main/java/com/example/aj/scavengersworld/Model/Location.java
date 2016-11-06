package com.example.aj.scavengersworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kalyan on 10/13/16.
 */

public class Location {
    private double latitude;
    private double longitude;
    public Location()
    {

    }
    public Location(float mLatitude, float mLongitude) {
        this.latitude = mLatitude;
        this.longitude = mLongitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double mLatitude) {
        this.latitude = mLatitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double mLongitude) {
        this.longitude = mLongitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Double.compare(location.latitude, latitude) != 0) return false;
        return Double.compare(location.longitude, longitude) == 0;

    }
/*
    @Override
<<<<<<< Updated upstream
    public int hashCode() {
        int result = (latitude != +0.0f ? Float.floatToIntBits(latitude) : 0);
=======
    public long hashCode() {
        long result = (latitude != +0.0f ? Double.doubleToLongBits(latitude) : 0);
>>>>>>> Stashed changes
        result = 31 * result + (longitude != +0.0f ? Float.floatToIntBits(longitude) : 0);
        return result;
    }*/
    public double distanceFromLocationInMeters(Location location) {
        double el1 = 0;
        double el2 = 0;
        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(location.latitude - this.latitude);
        Double lonDistance = Math.toRadians(location.longitude - longitude);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(location.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    protected Location(Parcel in) {
        latitude = in.readFloat();
        longitude = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest,int flags){
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
