package com.example.aj.scavengersworld.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jennifer on 10/17/2016.
 */
public class LeaderboardActivity extends BaseActivity {
	private UserSessionManager session;

	private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
	private DatabaseReference mDataBaseRefHunts;
	private DatabaseReference mDataBaseRefLeaders;

	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	private String huntName;
	private Hunt hunt;

	private Map<String, Integer> leaders = new HashMap<>();

	private final String LOG_TAG = getClass().getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, getString(R.string.log_onCreate));

		session = UserSessionManager.INSTANCE;

		Intent intent = getIntent();
		huntName = intent.getStringExtra("HUNTNAME");

		mDataBaseRefHunts = mDatabase.getReference(R.string.hunts + "/" + huntName);
		mDataBaseRefHunts.addListenerForSingleValueEvent(huntListener);

		if(hunt != null) {

			mDataBaseRefLeaders = mDatabase.getReference(getString(R.string.huntsToLeaders) + "/" + huntName);
			mDataBaseRefLeaders.addListenerForSingleValueEvent(huntsToLeadersListener);

			//set up "leaders" recycler
			mRecyclerView = (RecyclerView) findViewById(R.id.leaders_recycler);
			mRecyclerView.setHasFixedSize(true);

			mLayoutManager = new LinearLayoutManager(this);
			mRecyclerView.setLayoutManager(mLayoutManager);

			mAdapter = new LeadersRecyclerViewAdapter(leaders);
			mRecyclerView.setAdapter(mAdapter);
		}
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

	ValueEventListener huntsToLeadersListener = new ValueEventListener() {
		@Override
		public void onDataChange(DataSnapshot dataSnapshot) {
			for(DataSnapshot huntsToLeadersSnapshot : dataSnapshot.getChildren()) {
				String leader = huntsToLeadersSnapshot.getKey();
				int score = huntsToLeadersSnapshot.getValue(Integer.class);
				leaders.put(leader, score);
			}
			RecyclerView mLeadersRecyclerView = (RecyclerView) findViewById(R.id.leaders_recycler);
			if(mLeadersRecyclerView != null) {
				LeadersRecyclerViewAdapter leadersRecyclerViewAdapter = (LeadersRecyclerViewAdapter) mLeadersRecyclerView.getAdapter();
				leadersRecyclerViewAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onCancelled(DatabaseError databaseError) {
			// Getting Post failed, log a message

		}
	};

	private ValueEventListener huntListener = new ValueEventListener() {
		@Override
		public void onDataChange(DataSnapshot dataSnapshot) {
			for (DataSnapshot huntSnapshot : dataSnapshot.getChildren()) {
				hunt = huntSnapshot.getValue(Hunt.class);
			}
		}

		@Override
		public void onCancelled(DatabaseError databaseError) {

		}
	};
}
