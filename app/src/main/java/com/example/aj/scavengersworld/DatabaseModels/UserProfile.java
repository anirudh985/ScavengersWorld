package com.example.aj.scavengersworld.DatabaseModels;

import java.util.List;

/**
 * Created by aj on 11/4/16.
 */
public class UserProfile {
    private double pointsEarned;
    private List<Integer> badgesEarned;

    public double getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(double pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public List<Integer> getBadgesEarned() {
        return badgesEarned;
    }

    public void setBadgesEarned(List<Integer> badgesEarned) {
        this.badgesEarned = badgesEarned;
    }
}
