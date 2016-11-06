package com.example.aj.scavengersworld.DatabaseModels;

import com.example.aj.scavengersworld.Model.Clue;

import java.util.List;

/**
 * Created by kalyan on 11/3/16.
 */

public class HuntsToClues {
    private String huntName;
    private List<Clue> ClueList;

    public String getHuntName() {
        return huntName;
    }

    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

    public List<Clue> getClueList() {
        return ClueList;
    }

    public void setClueList(List<Clue> clueList) {
        ClueList = clueList;
    }
}
