package com.example.aj.scavengersworld.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;

/**
 * Created by Jennifer on 10/17/2016.
 */
public class HuntActivity extends BaseActivity implements View.OnClickListener {

	private Intent intent = getIntent();
	private String huntName = intent.getStringExtra("NAME");

	private UserSessionManager session = UserSessionManager.INSTANCE;

	private final String LOG_TAG = getClass().getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, getString(R.string.log_onCreate));

		TextView huntNameView = (TextView) findViewById(R.id.hunt_name);
		huntNameView.setText(huntName);

		String userHuntStatus = session.getHuntStatusByName(huntName);
		if(userHuntStatus != null) {
			if(userHuntStatus.equals("INPROGRESS")) {
				Hunt hunt = session.getParticipatingHuntByName(huntName);
			} else if(userHuntStatus.equals("ADMIN")) {
				Hunt hunt = session.getAdminHuntByName(huntName);
			} else if(userHuntStatus.equals("INPROGRESS")) {
				Hunt hunt = session.getCompletedHuntByName(huntName);
			}
		} else {
			//TODO get hunt from db
		}

		TextView description = (TextView) findViewById(R.id.hunt_description);
		//description.setText(hunt.); //TODO get description from Hunt object

		//Button join = (Button) findViewById(R.id.hunt_join_button);
		//join.setOnClickListener(this); TODO set listener based on button type

		Button leaders = (Button) findViewById(R.id.hunt_leaders_button);
		leaders.setOnClickListener(this);
	}

	@Override
	public int getLayoutResource(){
		return R.layout.activity_hunt;
	}

	public String getScreenName(){
		return "Hunt";
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

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.hunt_leaders_button:
				Intent leaderboard = new Intent(HuntActivity.this,LeaderboardActivity.class);
				startActivity(leaderboard);
				break;
			default:
				break;
		}
	}
}
