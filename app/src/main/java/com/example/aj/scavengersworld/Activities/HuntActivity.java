package com.example.aj.scavengersworld.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aj.scavengersworld.R;

/**
 * Created by Jennifer on 10/17/2016.
 */
public class HuntActivity extends AppCompatActivity implements View.OnClickListener {
	Toolbar toolbar;

	private final String LOG_TAG = getClass().getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate()");
		setContentView(R.layout.activity_hunt);
		configureToolbar();

		TextView hunt = (TextView) findViewById(R.id.hunt_name);
		hunt.setText("Hunt Name Here"); //TODO

		TextView description = (TextView) findViewById(R.id.hunt_description);
		description.setText("Hunt Description Here"); //TODO

		Button join = (Button) findViewById(R.id.hunt_join_button);
		join.setOnClickListener(this);

		Button leaders = (Button) findViewById(R.id.hunt_leaders_button);
		leaders.setOnClickListener(this);
	}

	//protected abstract String getScreenName(); TODO??

	private void configureToolbar() {
		Log.d(LOG_TAG, "configureToolbar()");
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			//getSupportActionBar().setTitle(getScreenName()); TODO??

		}
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

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.hunt_leaders_button:
				Intent leaderboard = new Intent(HuntActivity.this,LeaderboardActivity.class);
				break;
			default:
				break;
		}
	}
}
