package com.example.aj.scavengersworld.DatabaseModels;

/**
 * Created by aj on 11/1/16.
 */
public class UserToHunts implements Comparable<UserToHunts>{
    private String huntName;
    private int score;
    private String state;
    private String clueId;
    private double progress;

    public String getHuntName() {
        return huntName;
    }

    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getClueId() {
        return clueId;
    }

    public void setClueId(String clueId) {
        this.clueId = clueId;
    }

    public double getProgress() {return this.progress; }

    public void setProgress(double progress) { this.progress = progress; }

    @Override
    public int compareTo(UserToHunts userToHunts) {
        return Double.compare(progress, userToHunts.getProgress());
    }
}
