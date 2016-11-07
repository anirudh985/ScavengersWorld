package com.example.aj.scavengersworld.Activities;

import android.os.Bundle;
import android.util.Log;

import com.example.aj.scavengersworld.R;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Jennifer on 10/17/2016.
 */
public class LeaderboardActivity extends BaseActivity {

	private DatabaseReference mDatabaseRef;

	private final String LOG_TAG = getClass().getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, getString(R.string.log_onCreate));
	}

	@Override
	public int getLayoutResource(){
		return R.layout.activity_leaderboard;
	}

	public String getScreenName(){
		return "Leader Board";
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
