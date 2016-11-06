package com.example.aj.scavengersworld.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.aj.scavengersworld.DatabaseModels.UserProfile;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;

/**
 * Created by aj on 10/16/16.
 */
public class ProfileActivity extends BaseActivity {

    private final String LOG_TAG = getClass().getSimpleName();
    private UserSessionManager session = UserSessionManager.INSTANCE;
    //private List<Hunt> yourHunts = session.getParticipatingHuntsList() TODO utilize YourHuntsFragment?

    private UserProfile profile = session.getUserProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, getString(R.string.log_onCreate));

        TextView username = (TextView) findViewById(R.id.profile_username);
        username.setText(getScreenName());

        TextView score = (TextView) findViewById(R.id.profile_points);
        score.setText(String.valueOf(profile.getPointsEarned()));
    }

    @Override
    public int getLayoutResource(){
        return R.layout.activity_profile;
    }

    public String getScreenName(){
        return session.getUserName();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, getString(R.string.log_onStart));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, getString(R.string.log_onResume));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, getString(R.string.log_onPause));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, getString(R.string.log_onStop));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, getString(R.string.log_onDestroy));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, getString(R.string.log_onRestart));
    }
}
