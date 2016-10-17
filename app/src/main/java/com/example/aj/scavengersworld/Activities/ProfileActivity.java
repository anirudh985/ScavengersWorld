package com.example.aj.scavengersworld.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.aj.scavengersworld.R;

/**
 * Created by aj on 10/16/16.
 */
public class ProfileActivity extends BaseActivity {

    Toolbar toolbar;

    private final String LOG_TAG = getClass().getSimpleName();
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()");

        TextView username = (TextView) findViewById(R.id.profile_username);
        username.setText("Username here"); //TODO utilize User.getUserName()

        TextView score = (TextView) findViewById(R.id.profile_points);
        score.setText("0/5000"); //TODO utilize User class to get score
    }

    @Override
    public int getLayoutResource(){
        return R.layout.activity_profile;
    }

    public String getScreenName(){
        return "Profile";
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
}
