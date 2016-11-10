package com.example.aj.scavengersworld;

/**
 * Created by aj on 11/1/16.
 */
public final class Constants {
    public static final int YOUR_HUNTS = 0;
    public static final int CREATED_HUNTS = 1;

    public static final int POPULAR_FEEDS = 0;
    public static final int ALL = 1;

    // STATE OF HUNTS //
    public static final String ADMIN = "A";
    public static final String INPROGRESS = "P";
    public static final String COMPLETED = "C";
    public static final String REQUESTED = "R";
    public static final String INVITED = "I";

    // DISPLAY MESSAGES //
    public static final String progressMessage = "Progress: ";

    // KEYS //
    public static final String MODIFY_HUNT = "MODIFY_HUNTS";

    // POPULARITY THRESHOLD //
    public static final float POPULARITY_THRESHOLD = 5000.00f;

    // BADGES //
    public static final int FIRST_TO_COMPLETE = 1;
    public static final int TEN_HUNTS_COMPLETED = 2;
    public static final int FIFTY_HUNTS_COMPLETED = 3;
    // add others as well

    // CLUES //
    public static final int SEQUENCE_OF_FIRST_CLUE = 1;
    public static final double RADIUS_OF_ACCEPTANCE = 100.0;

}
