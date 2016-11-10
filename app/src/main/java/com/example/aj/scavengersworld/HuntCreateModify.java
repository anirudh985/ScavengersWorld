package com.example.aj.scavengersworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.CluesRelated.UpdateClueRecyclerViewAdapter;
import com.example.aj.scavengersworld.DatabaseModels.HuntsData;
import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.example.aj.scavengersworld.Constants.ADMIN;

public class HuntCreateModify extends BaseActivity implements View.OnClickListener, android.text.TextWatcher {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent createdIntent = getIntent();
        Bundle extrasBundle = createdIntent.getExtras();

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
			} else {
				mHuntDescription = getString(R.string.hunt_description);
				hunt = new Hunt();
				hunt.setHuntName(mHuntName);
			}
        }

        editName = (EditText) findViewById(R.id.editHuntName);
		editName.setText(mHuntName);
		if(newHunt) {
			editName.addTextChangedListener(this);
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

		ToggleButton toggle = (ToggleButton) findViewById(R.id.public_private_toggle);
		toggle.setOnClickListener(this);

		//get all clues for this hunt
		DatabaseReference mDataBaseRef = mDatabase.getReference(getString(R.string.huntsToClues) + "/" + mHuntName);
		mDataBaseRef.addListenerForSingleValueEvent(huntsToCluesListener);

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
				boolean isPublic = toggle.isChecked(); //TODO ensure this isn't reversed
				if(isPublic == hunt.isPrivateHunt()) {
					changed = true;
					hunt.setPrivateHunt(isPublic);
				}
			case R.id.save_button:
				if(changed) {
					if(newHunt) {
						newHunt = false;
						if(session.getAdminHuntByName(mHuntName) == null) {
							session.addHunt(ADMIN, hunt);
						}
						editName.removeTextChangedListener(this);
					}
                    UpdateHuntDataInDatabase(hunt);
                    UpdateClueListInDatabase(hunt);
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
				newClue.setHuntName(currentHunt.getHuntName());
				currentHunt.addClueToClueList(newClue);
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
		huntsData.setPrivate(currentHunt.isPrivateHunt());
		mDatabaseRefHuntsData = mDatabase.getReference(getString(R.string.hunts) + "/" + currentHunt.getHuntName());
		mDatabaseRefHuntsData.setValue(huntsData);
	}

	private void UpdateClueListInDatabase(Hunt currentHunt){
		mDatabaseRefClues = mDatabase.getReference(getString(R.string.huntsToClues) + "/" + currentHunt.getHuntName());
		mDatabaseRefClues.setValue(currentHunt.getClueList());
	}

}
