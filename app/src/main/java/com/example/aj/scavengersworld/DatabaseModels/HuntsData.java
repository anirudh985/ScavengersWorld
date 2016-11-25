package com.example.aj.scavengersworld.DatabaseModels;

import java.util.HashMap;

/**
 * Created by kalyan on 11/7/16.
 */

public class HuntsData {
    private String createdByUserId;
    private String description;
    private long endTime;
    private String huntName;
    private Boolean isPrivate;
    private long startTime;
    private HashMap<String, String> pendingRequests;

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getHuntName() {
        return huntName;
    }

    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public HashMap<String, String> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(HashMap<String, String> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }
}
