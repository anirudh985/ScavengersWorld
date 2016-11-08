package com.example.aj.scavengersworld.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aj.scavengersworld.CluesRelated.CurrentClueActivity;
import com.example.aj.scavengersworld.DatabaseModels.SearchableHunt;
import com.example.aj.scavengersworld.DatabaseModels.UserToHunts;
import com.example.aj.scavengersworld.GamePlayActivity;
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
import static com.example.aj.scavengersworld.Constants.SEQUENCE_OF_FIRST_CLUE;


/**
 * Created by Jennifer on 10/17/2016.
 */
public class HuntActivity extends BaseActivity{

	private Intent intent;
	private String huntName;

	private Hunt hunt;
	private UserToHunts userToHunts;

	private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
	private DatabaseReference mDatabaseRefHunts;

	private UserSessionManager session = UserSessionManager.INSTANCE;

	private final String LOG_TAG = getClass().getSimpleName();

	private TextView description;

	private ValueEventListener huntListener = new ValueEventListener() {
		@Override
		public void onDataChange(DataSnapshot dataSnapshot) {
			for(DataSnapshot huntSnapshot : dataSnapshot.getChildren()){
				Hunt currentHunt = huntSnapshot.getValue(Hunt.class);
				updateHuntInSession(currentHunt);
				updateUI();
			}
		}

		@Override
		public void onCancelled(DatabaseError databaseError) {

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		intent = getIntent();
		huntName = intent.getStringExtra("NAME");

		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, getString(R.string.log_onCreate));

		mDatabaseRefHunts = mDatabase.getReference(getString(R.string.huntsTable));



		TextView huntNameView = (TextView) findViewById(R.id.hunt_name);
		huntNameView.setText(huntName);

		getHuntObjectFromDatabaseAndUpdateInSession(huntName);
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
			hunt = new Hunt();
		}

		description = (TextView) findViewById(R.id.hunt_description);
//		description.setText(hunt.getDescription());

		Button join = (Button) findViewById(R.id.hunt_join_button);
		if(userHuntStatus != null) {
			if(userHuntStatus.equals(INPROGRESS) || userHuntStatus.equals(COMPLETED)) {
				join.setText(R.string.view_clues);
				join.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent clueIntent = new Intent(view.getContext(), CurrentClueActivity.class);
						clueIntent.putExtra("MODE", "HUNTCLUES");
						clueIntent.putExtra("HUNTNAME", huntName);
						startActivity(clueIntent);
					}
				});
			} else if(userHuntStatus.equals(ADMIN)) {
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
			join.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO: add hunt to yourHuntsList and update in database and open gameplay screen
					addHuntToSession();
					updateHuntStatusToJoinInDB();
					startGamePlayScreen();
				}
			});
		}

//		Button leaders = (Button) findViewById(R.id.hunt_leaders_button);
//		leaders.setOnClickListener(this);
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

	private void getHuntObjectFromDatabaseAndUpdateInSession(String huntName){
		if(huntName != null){
			mDatabaseRefHunts.orderByChild(getString(R.string.orderByHuntName))
					         .equalTo(huntName)
					         .addListenerForSingleValueEvent(huntListener);
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please select a hunt to update")
					.setTitle("Error")
					.setPositiveButton(android.R.string.ok, null);
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	private void updateUI(){
		if(description != null){
			description.setText(hunt.getDescription());
		}
	}

	private void updateHuntInSession(Hunt currentHunt){
		if(hunt != null && currentHunt != null){
			hunt.setCreatedByUserId(currentHunt.getCreatedByUserId());
			hunt.setDescription(currentHunt.getDescription());
			hunt.setEndTime(currentHunt.getEndTime());
			hunt.setStartTime(currentHunt.getStartTime());
			hunt.setPrivateHunt(currentHunt.isPrivateHunt());
		}
	}

	private void addHuntToSession(){
		hunt.setCurrentClueSequence(SEQUENCE_OF_FIRST_CLUE);
		hunt.setState(INPROGRESS);
		hunt.setScore(0);
		hunt.setProgress(0);
		session.addHunt(INPROGRESS, hunt);
	}

	private void updateHuntStatusToJoinInDB(){
		userToHunts = new UserToHunts();
		userToHunts.setCurrentClueSequence(SEQUENCE_OF_FIRST_CLUE);
		userToHunts.setState(INPROGRESS);
		userToHunts.setScore(0);
		userToHunts.setProgress(0);

		DatabaseReference dbRef = mDatabase.getReference(getString(R.string.userToHunts));
		dbRef.child(session.getUniqueUserId()).child(huntName).setValue(userToHunts);
	}

	private void startGamePlayScreen(){
		Intent gamePlayScreenIntent = new Intent(this, GamePlayActivity.class);
		startActivity(gamePlayScreenIntent);
		finish();
	}
}
