package com.example.aj.scavengersworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalyan on 10/13/16.
 */

public class User implements Parcelable{
    private String mUserId;
    private String mUserName;
    private String mUserEmail;
    private List<Hunt> mCreatedHuntList;
    private List<Hunt> mRegisteredHuntList;
    private String mDisplayName;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String mUserEmail) {
        this.mUserEmail = mUserEmail;
    }

    public List<Hunt> getCreatedHuntList() {
        return mCreatedHuntList;
    }

    public void setCreatedHuntList(List<Hunt> mCreatedHuntList) {
        this.mCreatedHuntList = mCreatedHuntList;
    }

    public List<Hunt> getRegisteredHuntList() {
        return mRegisteredHuntList;
    }

    public void setRegisteredHuntList(List<Hunt> mRegisteredHuntList) {
        this.mRegisteredHuntList = mRegisteredHuntList;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    protected User(Parcel in) {
        mUserId = in.readString();
        mUserName = in.readString();
        mUserEmail = in.readString();
        if (in.readByte() == 0x01) {
            mCreatedHuntList = new ArrayList<Hunt>();
            in.readList(mCreatedHuntList, Hunt.class.getClassLoader());
        } else {
            mCreatedHuntList = null;
        }
        if (in.readByte() == 0x01) {
            mRegisteredHuntList = new ArrayList<Hunt>();
            in.readList(mRegisteredHuntList, Hunt.class.getClassLoader());
        } else {
            mRegisteredHuntList = null;
        }
        mDisplayName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserId);
        dest.writeString(mUserName);
        dest.writeString(mUserEmail);
        if (mCreatedHuntList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mCreatedHuntList);
        }
        if (mRegisteredHuntList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRegisteredHuntList);
        }
        dest.writeString(mDisplayName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
