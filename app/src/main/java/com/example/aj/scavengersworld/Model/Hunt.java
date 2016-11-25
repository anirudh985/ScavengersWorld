package com.example.aj.scavengersworld.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kalyan on 10/13/16.
 * Modified by anirudh on 11/01/16.
 */

@IgnoreExtraProperties
public class Hunt implements Comparable<Hunt> {
    private int huntId;
    private String huntName;
    private String description;
    private String createdByUserId;
    private long startTime;
    private long endTime;
    //private Boolean miIsTeam;
    private boolean privateHunt;
    private List<User> listOfAdmins;
    private int firstClueId;
    private int currentClueSequence;

    private String state;
    private int score;
    private double progress;

    private List<Clue> clueList;

    private HashMap<String, String> pendingRequests;

    public Hunt(){
        this.clueList = new ArrayList<>();
        this.privateHunt = false;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isPrivateHunt() {
        return privateHunt;
    }

    public void setPrivateHunt(boolean aPrivate) {
        privateHunt = aPrivate;
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

    public int getCurrentClueSequence() {
        return currentClueSequence;
    }

    public void setCurrentClueSequence(int currentClueId) {
        this.currentClueSequence = currentClueId;
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

    public Clue getClueWithId(int clueId){
        for(Clue clue : clueList){
            if(clue.getClueId() == clueId)
                return clue;
        }
        return null;
    }

    public  Clue getCurrentClue(){
        return getClueAtSequence(currentClueSequence);
    }

    public Clue getClueAtSequence(int sequenceNumber){
        for(Clue clue : clueList){
            if(clue.getSequenceNumberInHunt() == sequenceNumber)
                return clue;
        }
        return null;
    }

    public Date getStartDate(){
        return new Date(this.startTime);
    }

    public Date getEndDate(){
        return new Date(this.endTime);
    }

    public void setStartDate(Date startDate){
        this.startTime = startDate.getTime();
    }

    public void setEndDate(Date endDate){
        this.endTime = endDate.getTime();
    }

    public HashMap<String, String> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(HashMap<String, String> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }
}