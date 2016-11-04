package com.example.aj.scavengersworld.Model;

/**
 * Created by kalyan on 10/13/16.
 */

public class Location {
    private float latitude;
    private float longitude;

    public Location(){

    }

    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float mLatitude) {
        this.latitude = mLatitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float mLongitude) {
        this.longitude = mLongitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Float.compare(location.latitude, latitude) != 0) return false;
        return Float.compare(location.longitude, longitude) == 0;

    }

    @Override
    public int hashCode() {
        int result = (latitude != +0.0f ? Float.floatToIntBits(latitude) : 0);
        result = 31 * result + (longitude != +0.0f ? Float.floatToIntBits(longitude) : 0);
        return result;
    }
}
