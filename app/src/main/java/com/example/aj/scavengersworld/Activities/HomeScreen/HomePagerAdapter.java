package com.example.aj.scavengersworld.Activities.HomeScreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import static com.example.aj.scavengersworld.Constants.YOUR_HUNTS;
import static com.example.aj.scavengersworld.Constants.CREATED_HUNTS;

/**
 * Created by aj on 10/16/16.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter{
    private int mNumOfTabs;


    private Fragment yourHuntsFragment, createdHuntsFragment;

    public HomePagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case YOUR_HUNTS:
                if(yourHuntsFragment == null){
                    yourHuntsFragment = new YourHuntsFragment();
                }
                return yourHuntsFragment;
            case CREATED_HUNTS:
                if(createdHuntsFragment == null){
                    createdHuntsFragment = new CreatedHuntsFragment();
                }
                return createdHuntsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
