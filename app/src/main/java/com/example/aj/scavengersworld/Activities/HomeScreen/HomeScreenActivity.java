package com.example.aj.scavengersworld.Activities.HomeScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.DatabaseModels.UserToHunts;
import com.example.aj.scavengersworld.GamePlayActivity;
import com.example.aj.scavengersworld.HuntCreateModify;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import static com.example.aj.scavengersworld.Constants.YOUR_HUNTS;
import static com.example.aj.scavengersworld.Constants.CREATED_HUNTS;


public class HomeScreenActivity extends BaseActivity implements YourHuntsFragment.OnListFragmentInteractionListener, CreatedHuntsFragment.OnListFragmentInteractionListener{

    private final String LOG_TAG = getClass().getSimpleName();

    private List<UserToHunts> listOfUserToHunts;

    private ViewPager viewPager;
    private HomePagerAdapter adapter;

    // FIREBASE STUFF //
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference(getString(R.string.userToHunts));

    private UserSessionManager session = UserSessionManager.INSTANCE;



    // FIREBASE LISTENERS //
    ValueEventListener userToHuntsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot userToHuntsSnapshot : dataSnapshot.getChildren()){
                UserToHunts userToHunts = userToHuntsSnapshot.getValue(UserToHunts.class);
                if(!listOfUserToHunts.contains(userToHunts)){
                    listOfUserToHunts.add(userToHunts);
                    session.updateHunts(userToHunts);
                }
            }
            updateUI();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(LOG_TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){

        }

        getUserHuntsAndSaveInSession();

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
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home_screen;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.welcomeMessage) + UserSessionManager.INSTANCE.getUserName();
    }

    @Override
    public void onListYourHuntsFragmentInteraction(Hunt hunt) {
        Log.d(LOG_TAG, "Your Hunt   "+hunt.toString());
        Intent modifyHunt = new Intent(this, GamePlayActivity.class);
        modifyHunt.putExtra("NAME", hunt.getHuntName());
        startActivity(modifyHunt);

    }

    @Override
    public void onListCreatedHuntsFragmentInteraction(Hunt hunt){
        Log.d(LOG_TAG, "Created Hunt    " + hunt.toString());
        Intent modifyHunt = new Intent(this, HuntCreateModify.class);
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
        mDatabaseRef.child(session.getUniqueUserId())
                .orderByChild(getString(R.string.orderByHuntId))
                .addListenerForSingleValueEvent(userToHuntsListener);
    }

    private void updateUI(){
        int position = viewPager.getCurrentItem();
        Fragment activeFragment = adapter.getItem(position);
        if(position == YOUR_HUNTS){
            ((YourHuntsFragment)activeFragment).updateUI();
        }
        else if(position == CREATED_HUNTS){
            ((CreatedHuntsFragment)activeFragment).updateUI();
        }
        mDatabaseRef.removeEventListener(userToHuntsListener);
    }

    private void saveToSession(UserToHunts userToHunts){

    }
}
