package com.example.aj.scavengersworld.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.aj.scavengersworld.Activities.HomeScreen.MyYourHuntsRecyclerViewAdapter;
import com.example.aj.scavengersworld.Activities.HomeScreen.YourHuntsFragment;
import com.example.aj.scavengersworld.DatabaseModels.UserProfile;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;

import java.util.List;

/**
 * Created by aj on 10/16/16.
 */
public class ProfileActivity extends BaseActivity implements YourHuntsFragment.OnListFragmentInteractionListener {

    private final String LOG_TAG = getClass().getSimpleName();
    private UserSessionManager session = UserSessionManager.INSTANCE;

    private List<Hunt> yourHunts = session.getParticipatingHuntsList();
	private List<Hunt> completedHunts = session.getCompletedHuntsList();

	private RecyclerView mJoinedRecyclerView;
	private RecyclerView.Adapter mJoinedAdapter;
	private RecyclerView mCompletedRecyclerView;
	private RecyclerView.Adapter mCompletedAdapter;
	private RecyclerView.LayoutManager mJoinedLayoutManager;
	private RecyclerView.LayoutManager mCompletedLayoutManager;
	private YourHuntsFragment.OnListFragmentInteractionListener mJoinedListener; //TODO
	private YourHuntsFragment.OnListFragmentInteractionListener mCompletedListener; //TODO

    private UserProfile profile = session.getUserProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, getString(R.string.log_onCreate));

        TextView username = (TextView) findViewById(R.id.profile_username);
        username.setText(getScreenName());

        TextView score = (TextView) findViewById(R.id.profile_points);
        score.setText(String.valueOf(profile.getPointsEarned()));

		//set up "hunts joined" recycler
		mJoinedRecyclerView = (RecyclerView) findViewById(R.id.joined_hunts_recycler);
		mJoinedRecyclerView.setHasFixedSize(true);

		mJoinedLayoutManager = new LinearLayoutManager(this);
		mJoinedRecyclerView.setLayoutManager(mJoinedLayoutManager);

		mJoinedAdapter = new MyYourHuntsRecyclerViewAdapter(yourHunts, mJoinedListener);
		mJoinedRecyclerView.setAdapter(mJoinedAdapter);

		//set up "hunts completed" recycler
		mCompletedRecyclerView = (RecyclerView) findViewById(R.id.completed_hunts_recycler);
		mCompletedRecyclerView.setHasFixedSize(true);

		mCompletedLayoutManager = new LinearLayoutManager(this);
		mCompletedRecyclerView.setLayoutManager(mCompletedLayoutManager);

		mCompletedAdapter = new MyYourHuntsRecyclerViewAdapter(completedHunts, mCompletedListener);
		mCompletedRecyclerView.setAdapter(mCompletedAdapter);
    }

	@Override
	public void onListYourHuntsFragmentInteraction(Hunt hunt) {
		Log.d(LOG_TAG, "Your Hunt   "+hunt.toString());
		Intent openHuntActivity = new Intent(this, HuntActivity.class);
		openHuntActivity.putExtra("NAME", hunt.getHuntName());
		startActivity(openHuntActivity);
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
