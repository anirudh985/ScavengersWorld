package com.example.aj.scavengersworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.CluesRelated.ClueItemRecyclerViewAdapter;
import com.example.aj.scavengersworld.Model.Hunt;

public class HuntCreateModify extends BaseActivity implements View.OnClickListener {
    private String mHuntName;
    private String mHuntDescription;
    private Hunt hunt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent createdIntent = getIntent();
        Bundle extrasBundle = createdIntent.getExtras();

        UserSessionManager session = UserSessionManager.INSTANCE;
        if(savedInstanceState == null) {
            if(extrasBundle != null && extrasBundle.get("Name") != null){
                mHuntName = extrasBundle.getString("Name");
            }
            else {
                mHuntName = getString(R.string.newHuntName);
            }
        }

        hunt = session.getAdminHuntByName(mHuntName);
        if(hunt != null) {
            mHuntDescription = hunt.getDescription();
        } else {
            mHuntDescription = getString(R.string.hunt_description);
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

		//set up "clues" recycler
		RecyclerView mCluesRecyclerView = (RecyclerView) findViewById(R.id.clues_recycler);
		mCluesRecyclerView.setHasFixedSize(true);

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		mCluesRecyclerView.setLayoutManager(mLayoutManager);

		RecyclerView.Adapter mAdapter = new ClueItemRecyclerViewAdapter(null, hunt, null);
		mCluesRecyclerView.setAdapter(mAdapter);

		//TODO set listener for clicking on specific item

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_clue_fab);
		fab.setOnClickListener(this);
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
    public void onClick(View v) { //TODO change to onGragmentInteraction or whatvver
        switch (v.getId()){
            /*case R.id.clueEditButton:
                Intent modifyClue = new Intent(this,ClueInfoActivity.class);
                startActivity(modifyClue);
                break;*/
			case R.id.add_clue_fab:
				Intent addClue = new Intent(this,ClueInfoActivity.class);
				startActivity(addClue);
				break;
        }
    }
}
