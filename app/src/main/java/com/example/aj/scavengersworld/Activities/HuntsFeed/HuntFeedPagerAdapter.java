package com.example.aj.scavengersworld.Activities.HuntsFeed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import static com.example.aj.scavengersworld.Constants.ALL;
import static com.example.aj.scavengersworld.Constants.POPULAR_FEEDS;

/**
 * Created by aj on 10/16/16.
 */
public class HuntFeedPagerAdapter extends FragmentStatePagerAdapter{
        private int mNumOfTabs;


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
