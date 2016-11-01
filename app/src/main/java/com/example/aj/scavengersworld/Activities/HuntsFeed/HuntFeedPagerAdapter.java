package com.example.aj.scavengersworld.Activities.HuntsFeed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.aj.scavengersworld.Activities.HomeScreen.CreatedHuntsFragment;
import com.example.aj.scavengersworld.Activities.HomeScreen.YourHuntsFragment;

/**
 * Created by aj on 10/16/16.
 */
public class HuntFeedPagerAdapter extends FragmentStatePagerAdapter{
        private int mNumOfTabs;
        private final int ALL = 1;
        private final int POPULAR_FEEDS = 0;

        public HuntFeedPagerAdapter(FragmentManager fm, int numOfTabs){
            super(fm);
            this.mNumOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case POPULAR_FEEDS:
                    return new PopularHuntsFeedFragment();
                case ALL:
                    return new AllHuntsFeedFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
}
