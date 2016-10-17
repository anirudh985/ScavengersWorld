package com.example.aj.scavengersworld;

import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by kalyan on 10/16/16.
 */

public class MapUtils {
    public static void setUpEula(FragmentActivity activity, SharedPreferences mSettings){
        boolean isEulaAccepted = mSettings.getBoolean(activity.getString(R.string.eula_accepted_key), false);
        if (!isEulaAccepted) {
            DialogFragment eulaDialogFragment = new EulaDialogFragment();
            eulaDialogFragment.show(activity.getSupportFragmentManager(), "eula");
        }
    }
}
