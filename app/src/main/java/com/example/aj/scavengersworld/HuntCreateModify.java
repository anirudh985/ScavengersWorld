package com.example.aj.scavengersworld;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.CluesRelated.UpdateClueRecyclerViewAdapter;
import com.example.aj.scavengersworld.DatabaseModels.HuntsData;
import com.example.aj.scavengersworld.DatabaseModels.SearchableHunt;
import com.example.aj.scavengersworld.DatabaseModels.UserToHunts;
import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.aj.scavengersworld.Constants.ADMIN;
import static com.example.aj.scavengersworld.Constants.METERS_PER_MILE;

public class HuntCreateModify extends BaseActivity implements View.OnClickListener, android.text.TextWatcher, DatePickerDialog.OnDateSetListener {
    private String mHuntName;
    private String mHuntDescription;
    private Hunt hunt;

	private int mPosition;

	private boolean changed = false;
	private boolean newHunt = false;

	private UserSessionManager session;

	private RecyclerView mCluesRecyclerView;

	private EditText editName;

	private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
	private DatabaseReference mDatabaseRefHuntClues;
	private DatabaseReference mDatabaseRefHuntsData;
	private DatabaseReference mDatabaseRefClues;
	private DatabaseReference mDatabaseRefUserHunts;
	private DatabaseReference mDatabaseSearchableHunts;

	private int numberOfPlayers;
	private String searchableHuntKey;

	private Button start_date;
	private Button end_date;

	private final int startID = Integer.MAX_VALUE-1;
	private final int endID = Integer.MAX_VALUE-2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent createdIntent = getIntent();
        Bundle extrasBundle = createdIntent.getExtras();

		start_date = (Button) findViewById(R.id.start_date);
		end_date = (Button) findViewById(R.id.end_date);

		mDatabaseRefHuntClues = mDatabase.getReference("hunts-clues");

        session = UserSessionManager.INSTANCE;
        if(savedInstanceState == null) {
            if(extrasBundle != null && extrasBundle.get("Name") != null){
                mHuntName = extrasBundle.getString("Name");
            }
            else {
                mHuntName = getString(R.string.newHuntName);
				newHunt = true;
            }

			if(!newHunt) {
				hunt = session.getAdminHuntByName(mHuntName);
				if(hunt != null) {
					mHuntDescription = hunt.getDescription();
				}
				getNumberOfPlayers(mHuntName);
			} else {
				mHuntDescription = getString(R.string.hunt_description);
				hunt = new Hunt();
				hunt.setHuntName(mHuntName);
				hunt.setCreatedByUserId(session.getUniqueUserId());
				numberOfPlayers = 0;
			}
        }
		else{
			restoreHuntInstance(savedInstanceState);
		}

        editName = (EditText) findViewById(R.id.editHuntName);
		editName.setText(mHuntName);
		if(newHunt) {
			editName.addTextChangedListener(this);
		}
		else{
			editName.setFocusable(false);
		}

		EditText editDescription = (EditText) findViewById(R.id.editHuntDescription);
		editDescription.setText(mHuntDescription);
		editDescription.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				hunt.setDescription(s.toString());
				changed = true;
			}

			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
			}
		});

		if(!newHunt) {
			Calendar startDate = hunt.getStartDate();
			String sDate = Integer.toString(startDate.get(Calendar.MONTH)+1) + "/" + Integer.toString(startDate.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(startDate.get(Calendar.YEAR));
			start_date.setText(sDate);
			Calendar endDate = hunt.getEndDate();
			String eDate = Integer.toString(endDate.get(Calendar.MONTH)+1) + "/" + Integer.toString(endDate.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(endDate.get(Calendar.YEAR));
			end_date.setText(eDate);
		}

		ToggleButton toggle = (ToggleButton) findViewById(R.id.public_private_toggle);
		toggle.setChecked(hunt.isPrivateHunt());
		toggle.setOnClickListener(this);

		//get all clues for this hunt
		if(!newHunt) {
			DatabaseReference mDataBaseRef = mDatabase.getReference(getString(R.string.huntsToClues) + "/" + mHuntName);
			mDataBaseRef.addListenerForSingleValueEvent(huntsToCluesListener);
		}

		//set up "clues" recycler
		mCluesRecyclerView = (RecyclerView) findViewById(R.id.clues_recycler);
		mCluesRecyclerView.setHasFixedSize(true);

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		mCluesRecyclerView.setLayoutManager(mLayoutManager);

		RecyclerView.Adapter mAdapter = new UpdateClueRecyclerViewAdapter(hunt, this);
		mCluesRecyclerView.setAdapter(mAdapter);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_clue_fab);
		fab.setOnClickListener(this);

		Button save_button = (Button) findViewById(R.id.save_button);
		save_button.setOnClickListener(this);

    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveHuntInstance(outState);
	}

	private void saveHuntInstance(Bundle outState){
		if(outState != null){
			outState.putString(getString(R.string.huntNameKey), hunt.getHuntName());
			outState.putString(getString(R.string.huntDescKey), hunt.getDescription());
			outState.putBoolean(getString(R.string.huntPrivateKey), hunt.isPrivateHunt());
			outState.putBoolean(getString(R.string.newHunt), newHunt);
			outState.putBoolean(getString(R.string.changed), changed);
			outState.putInt(getString(R.string.numPlayers), numberOfPlayers);
			outState.putString(getString(R.string.searchableHuntKey), searchableHuntKey);
			if(hunt.getClueList() != null && hunt.getClueList().size() != 0){
				outState.putParcelableArrayList(getString(R.string.clueListKey), (ArrayList<Clue>) hunt.getClueList());
			}
		}
	}

//	@Override
//	public void onRestoreInstanceState(Bundle inState){
//		super.onRestoreInstanceState(inState);
//		restoreHuntInstance(inState);
//	}

	private void restoreHuntInstance(Bundle inState){
		if(inState != null){
			hunt = new Hunt();
			hunt.setHuntName(inState.getString(getString(R.string.huntNameKey)));
			hunt.setDescription(inState.getString(getString(R.string.huntDescKey)));
			hunt.setPrivateHunt(inState.getBoolean(getString(R.string.huntPrivateKey)));
			hunt.setClueList(inState.<Clue>getParcelableArrayList(getString(R.string.clueListKey)));
			newHunt = inState.getBoolean(getString(R.string.newHunt));
			changed = inState.getBoolean(getString(R.string.changed));
			numberOfPlayers = inState.getInt(getString(R.string.numPlayers));
			searchableHuntKey = inState.getString(getString(R.string.searchableHuntKey));
		}
	}

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_hunt_create_modify;
    }

    @Override
    protected String getScreenName() {
        return mHuntName;
    }

    @Override
    public void onClick(View v) {
		switch (v.getId()) {
			case R.id.add_clue_fab:
				if(newHunt && session.getAdminHuntByName(mHuntName) == null) {
					session.addHunt(ADMIN, hunt);
				}
				Intent addClue = new Intent(this, ClueInfoActivity.class);
				addClue.putExtra("HUNTNAME", hunt.getHuntName());
				startActivityForResult(addClue,2);
				break;
			case R.id.public_private_toggle:
				ToggleButton toggle = (ToggleButton) findViewById(R.id.public_private_toggle);
				boolean isPrivate = toggle.isChecked();
				if(isPrivate != hunt.isPrivateHunt()) {
					changed = true;
					hunt.setPrivateHunt(isPrivate);
				}
				break;
			case R.id.save_button:
				if(changed) {
					if(newHunt) {
						editName.removeTextChangedListener(this);
						checkWhetherAHuntIsAlreadyPresentAndInsert(hunt);
						newHunt = false;
					}
					else{
						UpdateHuntDataInDatabase(hunt);
						UpdateClueListInDatabase(hunt);
						UpdateUserHuntsTableInDatabase(hunt);
						updateSearchableHuntsTableInDatabase(hunt, numberOfPlayers, false);
						finish();
					}

				} else if(!newHunt) {
					finish();
				}
				break;
		}
	}

	public void onClick(View v, int position) {
		RecyclerView mCluesRecyclerView = (RecyclerView) findViewById(R.id.clues_recycler);
		int itemPosition = mCluesRecyclerView.getChildLayoutPosition(v);
		Clue clue = hunt.getClueList().get(itemPosition);
		Intent editClue = new Intent(this, ClueInfoActivity.class);
		editClue.putExtra("HUNTNAME", hunt.getHuntName());
		editClue.putExtra("CLUE", clue);
		mPosition = position;
		startActivityForResult(editClue, 3);
	}

	@Override
	public void afterTextChanged(Editable s) {
		hunt.setHuntName(s.toString());
		changed = true;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start,
								  int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start,
							  int before, int count) {
	}

	ValueEventListener huntsToCluesListener = new ValueEventListener() {
		@Override
		public void onDataChange(DataSnapshot dataSnapshot) {
			for(DataSnapshot userToHuntsSnapshot : dataSnapshot.getChildren()){
				Hunt currentHunt = session.getAdminHuntByName(dataSnapshot.getKey());
				Clue newClue = userToHuntsSnapshot.getValue(Clue.class);
				if(currentHunt != null && newClue != null) {
					newClue.setHuntName(currentHunt.getHuntName());
					currentHunt.addClueToClueList(newClue);
				}
			}
			RecyclerView mCluesRecyclerView = (RecyclerView) findViewById(R.id.clues_recycler);
			if(mCluesRecyclerView != null) {
				UpdateClueRecyclerViewAdapter clueRecyclerViewAdapter = (UpdateClueRecyclerViewAdapter) mCluesRecyclerView.getAdapter();
				clueRecyclerViewAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onCancelled(DatabaseError databaseError) {
			// Getting Post failed, log a message

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		// check if the request code is same as what is passed. Here, it is 3
		if(requestCode==2 || requestCode==3)
		{
			Clue clue = data.getParcelableExtra("CLUE");
			if(requestCode == 3) {
				List<Clue> huntList = hunt.getClueList();
				huntList.remove(mPosition);
				huntList.add(mPosition, clue);
				hunt.setClueList(huntList);
			} else {
				hunt.addClueToClueList(clue);
			}
			changed = true;
		}
		UpdateClueRecyclerViewAdapter clueRecyclerViewAdapter = (UpdateClueRecyclerViewAdapter) mCluesRecyclerView.getAdapter();
		clueRecyclerViewAdapter.notifyDataSetChanged();
	}
	private void UpdateHuntDataInDatabase(Hunt currentHunt){
		HuntsData huntsData = new HuntsData();
		huntsData.setHuntName(currentHunt.getHuntName());
		huntsData.setCreatedByUserId(currentHunt.getCreatedByUserId());
		huntsData.setDescription(currentHunt.getDescription());
		huntsData.setEndTime(currentHunt.getEndTime());
		huntsData.setStartTime(currentHunt.getStartTime());
		huntsData.setPrivateHunt(currentHunt.isPrivateHunt());
		mDatabaseRefHuntsData = mDatabase.getReference(getString(R.string.hunts) + "/" + currentHunt.getHuntName());
		mDatabaseRefHuntsData.setValue(huntsData);
	}

	private void UpdateClueListInDatabase(Hunt currentHunt){
		mDatabaseRefClues = mDatabase.getReference(getString(R.string.huntsToClues) + "/" + currentHunt.getHuntName());
		mDatabaseRefClues.setValue(currentHunt.getClueList());
	}
	private void UpdateUserHuntsTableInDatabase(Hunt current){
		mDatabaseRefUserHunts = mDatabase.getReference(getString(R.string.userToHunts) + "/" + session.getUniqueUserId()+ "/" + current.getHuntName());
		UserToHunts userToHunts = new UserToHunts();
		userToHunts.setHuntName(current.getHuntName());
		userToHunts.setCurrentClueSequence(-1);
		userToHunts.setProgress(0);
		userToHunts.setScore(0);
		userToHunts.setState(ADMIN);
		mDatabaseRefUserHunts.setValue(userToHunts);
	}

	private void updateSearchableHuntsTableInDatabase(Hunt currentHunt, int numberOfPlayers, boolean newHunt){
		mDatabaseSearchableHunts = mDatabase.getReference(getString(R.string.searchableHuntsTable));
		SearchableHunt searchableHunt = new SearchableHunt();
		searchableHunt.setPrivateHunt(currentHunt.isPrivateHunt());
		searchableHunt.setStartTime(currentHunt.getStartTime());
		searchableHunt.setEndTime(currentHunt.getEndTime());
		searchableHunt.setNumberOfPlayers(numberOfPlayers);
		searchableHunt.setHuntName(currentHunt.getHuntName());
		searchableHunt.setLocationOfFirstClue(currentHunt.getClueAtSequence(1).getLocation());
		searchableHunt.setMaxRadius(getMaxRadius(currentHunt));
		if(newHunt){
			mDatabaseSearchableHunts.push().setValue(searchableHunt);
		}
		else{
			mDatabaseSearchableHunts.child(searchableHuntKey).setValue(searchableHunt);
		}
	}

	private double getMaxRadius(Hunt currentHunt){
		if(currentHunt == null){
			return -1;
		}
		double maxDistance = -1;
		if(currentHunt.getClueList() != null && currentHunt.getClueList().size() != 0){
			List<Clue> clueList = currentHunt.getClueList();
			double distanceBetweenClues;
			for(int i = 0; i < clueList.size(); i++){
				for(int j = i+1; j < clueList.size(); j++){
					distanceBetweenClues = getDistanceBetweenClues(clueList.get(i), clueList.get(j));
					if(maxDistance < distanceBetweenClues){
						maxDistance = distanceBetweenClues;
					}
				}
			}

		}
		return maxDistance/METERS_PER_MILE;
	}

	private double getDistanceBetweenClues(Clue clue1, Clue clue2){
		double distanceBetweenClues = 0;
		if(clue1 != null && clue1.getLocation() != null && clue2 != null && clue2.getLocation() != null){
			distanceBetweenClues = clue1.getLocation().distanceFromLocationInMeters(clue2.getLocation());
		}
		return distanceBetweenClues;
	}

	private void checkWhetherAHuntIsAlreadyPresentAndInsert(Hunt currentHunt){
		mDatabaseRefHuntsData = mDatabase.getReference(getString(R.string.hunts));
		mDatabaseRefHuntsData.orderByChild("huntName")
				.equalTo(currentHunt.getHuntName())
				.addListenerForSingleValueEvent(checkForHuntAndInsertListener);
	}

	ValueEventListener checkForHuntAndInsertListener = new ValueEventListener() {
		@Override
		public void onDataChange(DataSnapshot dataSnapshot) {
			boolean retrieved = false;
			for(DataSnapshot huntSnapshot : dataSnapshot.getChildren()){
				HuntsData huntsData = huntSnapshot.getValue(HuntsData.class);
				if(huntsData != null){
					retrieved = true;
				}
			}
			if(!retrieved){
//				if(session.getAdminHuntByName(mHuntName) == null) {
//					session.addHunt(ADMIN, hunt);
//				}
				UpdateHuntDataInDatabase(hunt);
				UpdateClueListInDatabase(hunt);
				UpdateUserHuntsTableInDatabase(hunt);
				updateSearchableHuntsTableInDatabase(hunt, 0, true);
				finish();
			}
			else{
				AlertDialog.Builder builder = new AlertDialog.Builder(HuntCreateModify.this);
				builder.setMessage(getString(R.string.duplicateHunt))
						.setPositiveButton(android.R.string.ok, null);
				AlertDialog dialog = builder.create();
				dialog.show();
				newHunt = true;
				editName.addTextChangedListener(HuntCreateModify.this);
			}
		}

		@Override
		public void onCancelled(DatabaseError databaseError) {

		}
	};

	private void getNumberOfPlayers(String huntName){
		if(huntName != null){
			mDatabaseSearchableHunts = mDatabase.getReference(getString(R.string.searchableHuntsTable));
			mDatabaseSearchableHunts.orderByChild("huntName")
					.equalTo(huntName).addListenerForSingleValueEvent(retrieveNumberOfPlayersListener);
		}
	}

	ValueEventListener retrieveNumberOfPlayersListener = new ValueEventListener() {
		@Override
		public void onDataChange(DataSnapshot dataSnapshot) {
			for(DataSnapshot searchableHuntSnapshot : dataSnapshot.getChildren()){
				SearchableHunt searchableHunt = searchableHuntSnapshot.getValue(SearchableHunt.class);
				numberOfPlayers = searchableHunt.getNumberOfPlayers();
				searchableHuntKey = searchableHuntSnapshot.getKey();
				break;
			}
		}

		@Override
		public void onCancelled(DatabaseError databaseError) {

		}
	};

	@Override
	public void onBackPressed(){
		removeHuntFromSession(hunt);
		super.onBackPressed();
	}

	private void removeHuntFromSession(Hunt hunt){
		session.removeHunt(ADMIN, hunt);
	}

	@Override
	public void homeButtonClicked(){
		session.removeHunt(ADMIN, hunt);
	}

	public void showDatePickerDialogStart(View v) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog datePickerDialogStart;

		if(!newHunt) {
			// Create a new instance of DatePickerDialog
			Calendar startDate = hunt.getStartDate();
			datePickerDialogStart =  new DatePickerDialog(this, this, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
		} else {
			datePickerDialogStart = new DatePickerDialog(this, this, year, month, day);
		}

		datePickerDialogStart.show();

		datePickerDialogStart.getDatePicker().setId(startID);
	}

	public void showDatePickerDialogEnd(View v) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog datePickerDialogEnd;

		if(!newHunt) {
			// Create a new instance of DatePickerDialog
			Calendar endDate = hunt.getEndDate();
			datePickerDialogEnd =  new DatePickerDialog(this, this, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
		} else {
			datePickerDialogEnd = new DatePickerDialog(this, this, year, month, day);
		}

		datePickerDialogEnd.show();

		datePickerDialogEnd.getDatePicker().setId(endID);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		if(view.getId()==startID) {
			Calendar startDate = Calendar.getInstance();
			startDate.set(view.getYear(), view.getMonth(), view.getDayOfMonth(), 0, 0, 0);
			hunt.setStartDate(startDate.getTimeInMillis());
			String date = Integer.toString(startDate.get(Calendar.MONTH)+1) + "/" + Integer.toString(startDate.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(startDate.get(Calendar.YEAR));
			start_date.setText(date);
			changed=true;
		} else if(view.getId()==endID) {
			Calendar endDate = Calendar.getInstance();
			endDate.set(view.getYear(), view.getMonth(), view.getDayOfMonth(), 0, 0, 0);
			hunt.setEndDate(endDate.getTimeInMillis());
			end_date.setText(endDate.toString());
			String date = Integer.toString(endDate.get(Calendar.MONTH)+1) + "/" + Integer.toString(endDate.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(endDate.get(Calendar.YEAR));
			end_date.setText(date);
			changed=true;
		}
	}

}
