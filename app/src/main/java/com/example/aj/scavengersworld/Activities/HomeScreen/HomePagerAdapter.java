package com.example.aj.scavengersworld.Activities.HomeScreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.aj.scavengersworld.Activities.Login.SignUpFragment;

/**
 * Created by aj on 10/16/16.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter{
        private int mNumOfTabs;
        private final int YOUR_HUNTS = 0;
        private final int CREATED_HUNTS = 1;

        public HomePagerAdapter(FragmentManager fm, int numOfTabs){
            super(fm);
            this.mNumOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case YOUR_HUNTS:
                    return new YourHuntsFragment();
                case CREATED_HUNTS:
                    return new CreatedHuntsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
}
