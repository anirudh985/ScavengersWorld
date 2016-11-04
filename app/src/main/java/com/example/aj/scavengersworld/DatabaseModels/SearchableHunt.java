package com.example.aj.scavengersworld.DatabaseModels;

import com.example.aj.scavengersworld.Model.Location;

/**
 * Created by aj on 11/2/16.
 */
public class SearchableHunt {
    private String huntName;
    private int numberOfPlayers;
    private Location locationOfFirstClue;
    private boolean privateHunt;
    private double maxRadius;

    public String getHuntName() {
        return huntName;
    }

    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Location getLocationOfFirstClue() {
        return locationOfFirstClue;
    }

    public void setLocationOfFirstClue(Location locationOfFirstClue) {
        this.locationOfFirstClue = locationOfFirstClue;
    }

    public boolean isPrivateHunt() {
        return privateHunt;
    }

    public void setPrivateHunt(boolean privateHunt) {
        this.privateHunt = privateHunt;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(double maxRadius) {
        this.maxRadius = maxRadius;
    }
}
