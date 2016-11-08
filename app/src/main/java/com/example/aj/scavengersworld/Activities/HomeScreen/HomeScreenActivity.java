package com.example.aj.scavengersworld.Activities.HomeScreen;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.Activities.HuntActivity;
import com.example.aj.scavengersworld.DatabaseModels.UserProfile;
import com.example.aj.scavengersworld.DatabaseModels.UserToHunts;
import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.example.aj.scavengersworld.Constants.CREATED_HUNTS;
import static com.example.aj.scavengersworld.Constants.YOUR_HUNTS;


public class HomeScreenActivity extends BaseActivity implements YourHuntsFragment.OnListFragmentInteractionListener, CreatedHuntsFragment.OnListFragmentInteractionListener{

    private final String LOG_TAG = getClass().getSimpleName();

    private UserSessionManager session = UserSessionManager.INSTANCE;
    private List<UserToHunts> listOfUserToHunts = new ArrayList<>();

    private ViewPager viewPager;
    private HomePagerAdapter adapter;

    // FIREBASE STUFF //
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRefUserHunts;
    private DatabaseReference mDatabaseRefUserProfile;
    private DatabaseReference mDatabaseRefHuntClues;

    private int numberOfClueEventListeners = 0;



    // FIREBASE LISTENERS //
    ValueEventListener userToHuntsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot userToHuntsSnapshot : dataSnapshot.getChildren()){
                UserToHunts userToHunts = userToHuntsSnapshot.getValue(UserToHunts.class);
                if(!listOfUserToHunts.contains(userToHunts)){
                    listOfUserToHunts.add(userToHunts);
                }
                Collections.reverse(listOfUserToHunts);
                session.updateHunts(listOfUserToHunts);
            }
            updateUI();
            getCurrentCluesAndSaveInSession();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(LOG_TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    ValueEventListener userProfileListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            UserProfile userProfile = null;
            for(DataSnapshot userProfileSnapshot : dataSnapshot.getChildren()){
                userProfile = userProfileSnapshot.getValue(UserProfile.class);
            }
            if(userProfile != null){
                session.updateUserProfile(userProfile);
            }
            else{
                createNewUserProfile();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate called()");
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        mDatabaseRefUserHunts = mDatabase.getReference(getString(R.string.userToHunts) + "/" + session.getUniqueUserId());
        mDatabaseRefUserProfile = mDatabase.getReference(getString(R.string.userToProfile) + "/" + session.getUniqueUserId());
        if(!(savedInstanceState != null && savedInstanceState.getBoolean("isDataPresent"))){
            getUserHuntsAndSaveInSession();
            getUserProfileAndSaveInSession();
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.home_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.joinedHuntsTab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.createdHuntsTab));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.home_view_pager);
        adapter = new HomePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home_screen;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.welcomeMessage) + " " + session.getUserName();
    }

    @Override
    public void onListYourHuntsFragmentInteraction(Hunt hunt) {
        Log.d(LOG_TAG, "Your Hunt   "+hunt.toString());
        Intent openHuntActivity = new Intent(this, HuntActivity.class);
        openHuntActivity.putExtra("NAME", hunt.getHuntName());
        startActivity(openHuntActivity);

    }

    @Override
    public void onListCreatedHuntsFragmentInteraction(Hunt hunt){
        Log.d(LOG_TAG, "Created Hunt    " + hunt.toString());
        Intent modifyHunt = new Intent(this, HuntActivity.class);
        modifyHunt.putExtra("NAME", hunt.getHuntName());
        startActivity(modifyHunt);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "******* onStart() **********");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "******* onResume() **********");
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "******* onPause() **********");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "******* onStop() **********");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "******* onDestroy() **********");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "******* onRestart() **********");
    }

    private void getUserHuntsAndSaveInSession(){
        Log.d(LOG_TAG, "getUserHuntsAndSaveInSession() called");
        mDatabaseRefUserHunts.orderByChild(getString(R.string.orderByProgress))
                    .addListenerForSingleValueEvent(userToHuntsListener);
    }

    private void updateUI(){
        Log.d(LOG_TAG, "updateUI() called");
        Fragment activeFragment;
        int noOfTabs = adapter.getCount();
        for(int i = 0; i < noOfTabs; i++){
            activeFragment = adapter.getItem(i);
            if(i == YOUR_HUNTS){
                ((YourHuntsFragment)activeFragment).updateUI();
            }
            else if(i == CREATED_HUNTS){
                ((CreatedHuntsFragment)activeFragment).updateUI();
            }
        }
        mDatabaseRefUserHunts.removeEventListener(userToHuntsListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState()");
        outState.putBoolean("isDataPresent", true);
    }

    private void getUserProfileAndSaveInSession(){
        Log.d(LOG_TAG, "getUserProfileAndSaveInSession() called");
        mDatabaseRefUserProfile.addListenerForSingleValueEvent(userProfileListener);
    }

    private void createNewUserProfile(){
        UserProfile newUserProfile = new UserProfile();
        newUserProfile.setPointsEarned(0);
        newUserProfile.setBadgesEarned(new ArrayList<Integer>());
        mDatabaseRefUserProfile.push().setValue(newUserProfile);
    }

    private void getCurrentCluesAndSaveInSession(){
        List<Hunt> allHuntsList = session.getAllHuntsList();
        for(Hunt curHunt : allHuntsList){
            numberOfClueEventListeners+=1;
            mDatabaseRefHuntClues = mDatabase.getReference(getString(R.string.huntsToClues) + "/" +curHunt.getHuntName());
            mDatabaseRefHuntClues.addListenerForSingleValueEvent(huntsToCluesListener);
        }
    }
    ValueEventListener huntsToCluesListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap<String, Hunt> allHunts = session.getAllHunts();
            for(DataSnapshot userToHuntsSnapshot : dataSnapshot.getChildren()){
                Hunt currentHunt = allHunts.get(dataSnapshot.getKey());
                Clue newClue = userToHuntsSnapshot.getValue(Clue.class);
                newClue.setHuntName(currentHunt.getHuntName());
                currentHunt.addClueToClueList(newClue);
                int a  = 1;
            }
            numberOfClueEventListeners-=1;
            if(numberOfClueEventListeners == 0)
                updateUI();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(LOG_TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };


}
