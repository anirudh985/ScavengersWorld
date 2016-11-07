package com.example.aj.scavengersworld;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aj.scavengersworld.Activities.BaseActivity;
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
            Button clueEditButton = (Button)findViewById(R.id.clueEditButton);
            clueEditButton.setOnClickListener(this);
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
		//RecyclerView mCluesRecyclerView = (RecyclerView) findViewById(R.id.clues_recycler);
		//mCluesRecyclerView.setHasFixedSize(true);

		//LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		//mCluesRecyclerView.setLayoutManager(mLayoutManager);

		//RecyclerView.Adapter mAdapter = new RecyclerView.Adapter();
		//mCluesRecyclerView.setAdapter(mAdapter);
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
        switch (v.getId()){
            case R.id.clueEditButton:
                Intent modifyClue = new Intent(this,ClueInfoActivity.class);
                startActivity(modifyClue);
                break;
        }
    }
}
