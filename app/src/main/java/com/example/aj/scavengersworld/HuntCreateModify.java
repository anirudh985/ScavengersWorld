package com.example.aj.scavengersworld;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
            else{
                mHuntName = getString(R.string.newHuntName);
            }
            Button clueEditButton = (Button)findViewById(R.id.clueEditButton);
            clueEditButton.setOnClickListener(this);
        }
        //setContentView(R.layout.activity_hunt_create_modify);
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
