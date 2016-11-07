package com.example.aj.scavengersworld;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;

public class ClueInfoActivity extends BaseActivity implements View.OnClickListener {

    private EditText mClueNameEditText;
    private TextView mLatitudeLabel;
    private TextView mLongitudelabel;
    private final String mDefaultLocationText = "--";
    private Button mLocationEditButton;
    private TextView mAddModifyClueLabel;
    private UserSessionManager session = UserSessionManager.INSTANCE;
    private EditText mClueDesctiptionEditText;
    private EditText mClueLandmarkEditText;
    private Button mBackToHuntButton;
    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_clue_info);
        mAddModifyClueLabel = (TextView) findViewById(R.id.add_modify_title);
        mLatitudeLabel = (TextView) findViewById(R.id.latitudeLabel);
        mLongitudelabel = (TextView) findViewById(R.id.longitudeLabel);
        mLocationEditButton = (Button) findViewById(R.id.editLocationButton);
        mClueNameEditText = (EditText)  findViewById(R.id.edit_clue_name);
        mClueDesctiptionEditText = (EditText)  findViewById(R.id.edit_clue_description);
        mClueLandmarkEditText = (EditText)  findViewById(R.id.edit_clue_landmark);
        mLocationEditButton.setOnClickListener(this);
        mBackToHuntButton = (Button) findViewById(R.id.back_to_hunts_button);
        mBackToHuntButton.setOnClickListener(this);
        UpdateValuesFromBundleOrIntent(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_clue_info;
    }

    @Override
    protected String getScreenName() {
        return "Clue";
    }

    private void UpdateValuesFromBundleOrIntent(Bundle savedbundle){
        if(savedbundle == null){
            Intent createdIntent = getIntent();
            savedbundle = createdIntent.getExtras();
           // String huntName = savedbundle.getString("HUNTNAME");
            String huntName = "Hunt3";
            Hunt currentHunt = session.getParticipatingHuntByName(huntName);
            if(savedbundle == null || savedbundle.get("CLUEID") == null) {
                mAddModifyClueLabel.setText(getString(R.string.add_clue));
                mLatitudeLabel.setText(mDefaultLocationText);
                mLongitudelabel.setText(mDefaultLocationText);
                mClueDesctiptionEditText.setText("");
                mClueNameEditText.setText("");
                mClueLandmarkEditText.setText("");
            }
            else{
                Clue currentClue = currentHunt.getClueWithId(savedbundle.getInt("CLUEID"));
                mAddModifyClueLabel.setText(getString(R.string.modify_clue));
                mLatitudeLabel.setText(Double.toString(currentClue.getLocation().getLatitude()));
                mLongitudelabel.setText(Double.toString(currentClue.getLocation().getLongitude()));
                mClueDesctiptionEditText.setText(currentClue.getClueDescription());
                mClueNameEditText.setText(currentClue.getClueTitle());
                mClueLandmarkEditText.setText(currentClue.getLandmarkDescription());
            }


        }
        else{
            mLatitudeLabel.setText(savedbundle.getString("LATITUDE"));
            mLongitudelabel.setText(savedbundle.getString("LONGITUDE"));
            mClueDesctiptionEditText.setText(savedbundle.getString(("CLUEDESCRITION")));
            mClueNameEditText.setText(savedbundle.getString("CLUENAME"));
            mClueLandmarkEditText.setText(savedbundle.getString("CLUELANDMARK"));
            mAddModifyClueLabel.setText(savedbundle.getString("ADDMODIFYCLUE"));
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.editLocationButton) {
            Intent editLocationIntent = new Intent(ClueInfoActivity.this,ModifyClueLocationActivity.class);
            //Intent editLocationIntent = new Intent(MainActivity.this,GamePlayActivity.class);
            //Bundle initLocation = new Bundle();
            Double mCurrentLatitude = Double.parseDouble(mLatitudeLabel.getText().toString().equals(mDefaultLocationText)? "0.0":mLatitudeLabel.getText().toString());
            Double mCurrentLongitude = Double.parseDouble(mLongitudelabel.getText().toString().equals(mDefaultLocationText)? "0.0": mLongitudelabel.getText().toString());
            if(mCurrentLatitude == 0) mCurrentLatitude = 23.5;
            if(mCurrentLongitude == 0) mCurrentLongitude = 83.5;
            editLocationIntent.putExtra("LATITUDE",mCurrentLatitude);
            editLocationIntent.putExtra("LONGITUDE",mCurrentLongitude);
            startActivityForResult(editLocationIntent,2);
        }
        else if( v.getId() == R.id.back_to_hunts_button){
            Bundle savedbundle = new Bundle();
            Intent backToHuntsIntent = new Intent(ClueInfoActivity.this, HuntCreateModify.class);
            Double mCurrentLatitude = Double.parseDouble(mLatitudeLabel.getText().toString().equals(mDefaultLocationText)? mLatitudeLabel.getText().toString() :"0.0");
            Double mCurrentLongitude = Double.parseDouble(mLongitudelabel.getText().toString().equals(mDefaultLocationText)? mLocationEditButton.getText().toString() :"0.0");
            savedbundle.putDouble("LATITUDE", mCurrentLatitude);
            savedbundle.putDouble("LONGITUDE",mCurrentLongitude);
            savedbundle.putString("CLUEDESCRITION",mClueDesctiptionEditText.getText().toString());
            savedbundle.putString("CLUENAME",mClueNameEditText.getText().toString());
            savedbundle.putString("CLUELANDMARK",mClueLandmarkEditText.getText().toString());
            backToHuntsIntent.putExtras(savedbundle);
            startActivity(backToHuntsIntent);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            double lLatitude=data.getDoubleExtra("LATITUDE",-1);
            double lLongitude=data.getDoubleExtra("LONGITUDE",-1);
            mLatitudeLabel.setText(Double.toString(lLatitude));
            mLongitudelabel.setText(Double.toString(lLongitude));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedbundle) {
        super.onSaveInstanceState(savedbundle);
        Log.d(LOG_TAG, "onSaveInstanceState()");
        savedbundle.putString("LATITUDE", (String) mLatitudeLabel.getText());
        savedbundle.putString("LONGITUDE",(String)mLongitudelabel.getText());
        savedbundle.putString("CLUEDESCRITION",mClueDesctiptionEditText.getText().toString());
        savedbundle.putString("CLUENAME",mClueNameEditText.getText().toString());
        savedbundle.putString("CLUELANDMARK",mClueLandmarkEditText.getText().toString());
        savedbundle.putString("ADDMODIFYCLUE",mAddModifyClueLabel.getText().toString());
    }
}
