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

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.CluesRelated.UpdateClueRecyclerViewAdapter;
import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HuntCreateModify extends BaseActivity implements View.OnClickListener {
    private String mHuntName;
    private String mHuntDescription;
    private Hunt hunt;

	private UserSessionManager session;

	private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
	private DatabaseReference mDatabaseRefHuntClues;

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
            }

			if(!(mHuntName == null) && !mHuntName.equals("New Hunt")) {
				hunt = session.getAdminHuntByName(mHuntName);
				if(hunt != null) {
					mHuntDescription = hunt.getDescription();
				}
			} else {
				mHuntDescription = getString(R.string.hunt_description);
				hunt = new Hunt();
				//session.addHunt(ADMIN, hunt); TODO
			}
        }

        EditText editName = (EditText) findViewById(R.id.editHuntName);
		editName.setText(mHuntName);
        editName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {hunt.setHuntName(s.toString());}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

		EditText editDescription = (EditText) findViewById(R.id.editHuntDescription);
		editDescription.setText(mHuntDescription);
		editDescription.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {hunt.setDescription(s.toString());}

			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
			}
		});

		//get all clues for this hunt
		DatabaseReference mDataBaseRef = mDatabase.getReference(getString(R.string.huntsToClues) + "/" + mHuntName);
		mDataBaseRef.addListenerForSingleValueEvent(huntsToCluesListener);

		//set up "clues" recycler
		RecyclerView mCluesRecyclerView = (RecyclerView) findViewById(R.id.clues_recycler);
		mCluesRecyclerView.setHasFixedSize(true);

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		mCluesRecyclerView.setLayoutManager(mLayoutManager);

		RecyclerView.Adapter mAdapter = new UpdateClueRecyclerViewAdapter(hunt.getHuntName(), this);
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
				Intent addClue = new Intent(this, ClueInfoActivity.class);
				startActivity(addClue);
				break;
			case R.id.save_button:
				//TODO write updates to db
				break;
		}
	}

	public void onClick(View v, int position) {
		RecyclerView mCluesRecyclerView = (RecyclerView) findViewById(R.id.clues_recycler);
		int itemPosition = mCluesRecyclerView.getChildLayoutPosition(v);
		Clue clue = hunt.getClueList().get(itemPosition);
		Intent editClue = new Intent(this, ClueInfoActivity.class);
		editClue.putExtra("HUNTNAME", clue.getHuntName());
		editClue.putExtra("CLUEID", clue.getClueId());
		startActivity(editClue);
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
}
