package com.example.aj.scavengersworld.Activities.HuntsFeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.Activities.HomeScreen.CreatedHuntsFragment;
import com.example.aj.scavengersworld.Activities.HomeScreen.HomePagerAdapter;
import com.example.aj.scavengersworld.Activities.HomeScreen.YourHuntsFragment;
import com.example.aj.scavengersworld.Activities.HuntActivity;
import com.example.aj.scavengersworld.GamePlayActivity;
import com.example.aj.scavengersworld.HuntCreateModify;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;

public class HuntFeedActivity extends BaseActivity implements AllHuntsFeedFragment.OnListFragmentInteractionListener{

    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
//            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "");
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.huntfeed_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.allFeeds));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.popularFeeds));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.huntfeed_view_pager);
        final HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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
        return R.layout.activity_huntfeed_screen;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.welcomeMessage) + " " + UserSessionManager.INSTANCE.getUserName();
    }

    @Override
    public void onListAllHuntsFeedFragmentInteraction(Hunt hunt) {
        Log.d(LOG_TAG, "open hunt from all hunts   "+hunt.toString());
        Intent huntActivity = new Intent(this, HuntActivity.class);
        huntActivity.putExtra(getString(R.string.huntKey), hunt);
        startActivity(huntActivity);
    }

//    @Override
//    public void onListPopularHuntsFeedFragmentInteraction(Hunt hunt){
//        Log.d(LOG_TAG, "Created Hunt    " + hunt.toString());
//        Intent modifyHunt = new Intent(this, HuntCreateModify.class);
//        modifyHunt.putExtra("NAME", hunt.getHuntName());
//        startActivity(modifyHunt);
//    }

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
//        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }
}
