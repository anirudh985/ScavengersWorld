package com.example.aj.scavengersworld.DatabaseModels;

import java.util.List;

/**
 * Created by aj on 11/4/16.
 */
public class UserProfile {
    private int pointsEarned;
    private List<Integer> badgesEarned;

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public List<Integer> getBadgesEarned() {
        return badgesEarned;
    }

    public void setBadgesEarned(List<Integer> badgesEarned) {
        this.badgesEarned = badgesEarned;
    }
}
