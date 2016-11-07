package com.example.aj.scavengersworld;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aj.scavengersworld.Activities.BaseActivity;

public class HuntCreateModify extends BaseActivity implements View.OnClickListener {
    private String mHuntName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            Intent createdIntent = getIntent();
            Bundle extrasBundle = createdIntent.getExtras();
            if(extrasBundle != null && extrasBundle.get("Name") != null){
                mHuntName = extrasBundle.getString("Name");
            }
            else {
                mHuntName = getString(R.string.newHuntName);
            }
            Button clueEditButton = (Button)findViewById(R.id.clueEditButton);
            clueEditButton.setOnClickListener(this);
        }
        TextView huntName = (TextView) findViewById(R.id.hunt_name);
        huntName.setText(mHuntName);

        EditText editText = (EditText) findViewById(R.id.editHuntName);
        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
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
