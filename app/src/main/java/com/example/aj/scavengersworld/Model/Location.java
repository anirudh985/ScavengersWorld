package com.example.aj.scavengersworld.Model;

/**
 * Created by kalyan on 10/13/16.
 */

public class Location {
    private float mLatitude;
    private float mLongitude;

    public Location(float mLatitude, float mLongitude) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float mLatitude) {
        this.mLatitude = mLatitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float mLongitude) {
        this.mLongitude = mLongitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Float.compare(location.mLatitude, mLatitude) != 0) return false;
        return Float.compare(location.mLongitude, mLongitude) == 0;

    }

    @Override
    public int hashCode() {
        int result = (mLatitude != +0.0f ? Float.floatToIntBits(mLatitude) : 0);
        result = 31 * result + (mLongitude != +0.0f ? Float.floatToIntBits(mLongitude) : 0);
        return result;
    }
}
