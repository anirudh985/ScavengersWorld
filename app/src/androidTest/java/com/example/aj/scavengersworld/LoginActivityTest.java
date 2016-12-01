package com.example.aj.scavengersworld;

import android.test.ActivityInstrumentationTestCase2;

import com.example.aj.scavengersworld.Activities.HomeScreen.HomeScreenActivity;
import com.example.aj.scavengersworld.Activities.HuntActivity;
import com.example.aj.scavengersworld.Activities.HuntsFeed.HuntFeedActivity;
import com.example.aj.scavengersworld.Activities.Login.LoginActivity;
import com.example.aj.scavengersworld.CluesRelated.ClueFeedbackActivity;
import com.example.aj.scavengersworld.CluesRelated.CurrentClueActivity;
import com.robotium.solo.Solo;

/**
 * Created by aj on 11/28/16.
 */


public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{

    private Solo solo;

    public LoginActivityTest(){
        super(LoginActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testScenario1(){
        solo.enterText(0, "poiuyu@xz.com");
        solo.enterText(1, "qwertypoiuyu");
        solo.clickOnButton("Login");
        solo.waitForActivity(HomeScreenActivity.class);
        solo.assertCurrentActivity("HomeScreen Activity", HomeScreenActivity.class, true);
        solo.clickOnView(solo.getView(R.id.signout));
        solo.waitForActivity(LoginActivity.class);
        solo.assertCurrentActivity("Login Activity", LoginActivity.class, true);
    }

    public void testScenario2(){
        solo.enterText(0, "poiuyu@xz.com");
        solo.enterText(1, "qwertypoiuyu");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("HomeScreen Activity", HomeScreenActivity.class, true);
        solo.clickOnView(solo.getView(R.id.gameplay_button));
        solo.waitForActivity(GamePlayActivity.class);
        solo.assertCurrentActivity("GamePlay Activity", GamePlayActivity.class, true);
        solo.waitForFragmentById(R.id.map);
//        solo.clickOnView(solo.getView(R.id.button6));
//        solo.clickInRecyclerView(0);
//        solo.waitForActivity(ClueFeedbackActivity.class);
        solo.clickOnActionBarHomeButton();
        solo.waitForActivity(HomeScreenActivity.class);
        solo.assertCurrentActivity("HomeScreen Activity", HomeScreenActivity.class, true);
        solo.clickOnView(solo.getView(R.id.signout));
        solo.waitForActivity(LoginActivity.class);
        solo.assertCurrentActivity("Login Activity", LoginActivity.class, true);
    }

    public void testScenario3(){
        solo.enterText(0, "poiuyu@xz.com");
        solo.enterText(1, "qwertypoiuyu");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("HomeScreen Activity", HomeScreenActivity.class, true);
        solo.clickOnView(solo.getView(R.id.hunt_join_button));
        solo.waitForActivity(HuntFeedActivity.class);
        solo.assertCurrentActivity("HuntFeed Activity", HuntFeedActivity.class, true);
        solo.clickInRecyclerView(1);
        solo.waitForActivity(HuntActivity.class);
        solo.assertCurrentActivity("Hunt Activity", HuntActivity.class, true);
        solo.clickOnView(solo.getView(R.id.hunt_join_button));
        solo.waitForActivity(CurrentClueActivity.class);
        solo.assertCurrentActivity("View Clues Activity", CurrentClueActivity.class, true);
        solo.clickOnActionBarHomeButton();
        solo.waitForActivity(HomeScreenActivity.class);
//        solo.assertCurrentActivity("HomeScreen Activity", HomeScreenActivity.class, true);
        solo.clickOnView(solo.getView(R.id.signout));
        solo.waitForActivity(LoginActivity.class);
        solo.assertCurrentActivity("Login Activity", LoginActivity.class, true);
    }

    //TODO: Rotating the screen in each test method


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
