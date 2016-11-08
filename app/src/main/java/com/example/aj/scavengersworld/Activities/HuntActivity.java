package com.example.aj.scavengersworld.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aj.scavengersworld.DatabaseModels.SearchableHunt;
import com.example.aj.scavengersworld.HuntCreateModify;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.aj.scavengersworld.Constants.ADMIN;
import static com.example.aj.scavengersworld.Constants.COMPLETED;
import static com.example.aj.scavengersworld.Constants.INPROGRESS;

/**
 * Created by Jennifer on 10/17/2016.
 */
public class HuntActivity extends BaseActivity implements View.OnClickListener {

	private Intent intent;
	private String huntName;

	private Hunt hunt;

	private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
	private DatabaseReference mDatabaseRefSearchableHunts = mDatabase.getReference("searchable-hunts");

	private UserSessionManager session = UserSessionManager.INSTANCE;

	private final String LOG_TAG = getClass().getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, getString(R.string.log_onCreate));

		intent = getIntent();
		huntName = intent.getStringExtra("NAME");

		TextView huntNameView = (TextView) findViewById(R.id.hunt_name);
		huntNameView.setText(huntName);

		String userHuntStatus = session.getHuntStatusByName(huntName);
		if(userHuntStatus != null) {
			if(userHuntStatus.equals(INPROGRESS)) {
				hunt = session.getParticipatingHuntByName(huntName);
			} else if(userHuntStatus.equals(ADMIN)) {
				hunt = session.getAdminHuntByName(huntName);
			} else if(userHuntStatus.equals(COMPLETED)) {
				hunt = session.getCompletedHuntByName(huntName);
			}
		} else {
			//TODO retrieve hunt from database
		}

		TextView description = (TextView) findViewById(R.id.hunt_description);
		description.setText(hunt.getDescription());

		Button join = (Button) findViewById(R.id.hunt_join_button);
		if(userHuntStatus != null) {
			if(userHuntStatus.equals("INPROGRESS") || userHuntStatus.equals("COMPLETED")) {
				join.setText(R.string.view_clues);
				join.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//TODO intent for hunt's clue page
					}
				});
			} else if(userHuntStatus.equals("ADMIN")) {
				join.setText(R.string.update_hunt);
				join.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent updateHunt = new Intent(view.getContext(), HuntCreateModify.class);
						updateHunt.putExtra("Name", huntName);
						startActivity(updateHunt);

					}
				});
			}
		} else {
			join.setText(R.string.hunt_join);
		}

		Button leaders = (Button) findViewById(R.id.hunt_leaders_button);
		leaders.setOnClickListener(this);
	}

	@Override
	public int getLayoutResource(){
		return R.layout.activity_hunt;
	}

	public String getScreenName(){
		return huntName;
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
			case R.id.hunt_join_button:
				//TODO change user status to "INPROGRESS"
				break;
			case R.id.hunt_leaders_button:
				Intent leaderboard = new Intent(HuntActivity.this,LeaderboardActivity.class);
				startActivity(leaderboard);
				break;
			default:
				break;
		}
	}

	ValueEventListener searchableHuntsListener = new ValueEventListener() {
		@Override
		public void onDataChange(DataSnapshot dataSnapshot) {
			for(DataSnapshot searchableHuntsSnapshot : dataSnapshot.getChildren()){
				SearchableHunt searchableHunt = searchableHuntsSnapshot.getValue(SearchableHunt.class);
				if(searchableHunt.getHuntName().equals(huntName)){
					hunt.setHuntName(searchableHunt.getHuntName());
					//TODO need to get description and the like from database
					break;
				}
			}
		}

		@Override
		public void onCancelled(DatabaseError databaseError) {
			// Getting Post failed, log a message
			Log.w(LOG_TAG, "loadPost:onCancelled", databaseError.toException());
			// ...
		}
	};
}
