package com.example.aj.scavengersworld.DatabaseModels;

/**
 * Created by aj on 11/1/16.
 */
public class UserToHunts {
    private String huntName;
    private int score;
    private char state;
    private String clueId;

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

    public char getState() {
        return state;
    }

    public void setState(char state) {
        this.state = state;
    }

    public String getClueId() {
        return clueId;
    }

    public void setClueId(String clueId) {
        this.clueId = clueId;
    }
}
