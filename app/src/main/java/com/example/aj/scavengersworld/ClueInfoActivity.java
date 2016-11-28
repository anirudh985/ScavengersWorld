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
import com.example.aj.scavengersworld.Model.Location;

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
    private Clue currentClue;
    private int RESULT_CODE;

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
           // RESULT_CODE = createdIntent.get
            savedbundle = createdIntent.getExtras();
            String huntName = savedbundle.getString("HUNTNAME");
            Hunt currentHunt = session.getAdminHuntByName(huntName);
            if(savedbundle == null || savedbundle.get("CLUE") == null) {
                currentClue = new Clue();
                currentClue.setHuntName(huntName);
                int numberOfCluesInhunt = currentHunt == null || currentHunt.getClueList() == null ? 0 :currentHunt.getClueList().size();
                currentClue.setSequenceNumberInHunt(numberOfCluesInhunt+1);
                currentClue.setClueId(numberOfCluesInhunt+1);
                currentClue.setLocation(new Location());
                if(numberOfCluesInhunt != 0)
                {
                    Clue previousClue = currentHunt.getClueAtSequence(numberOfCluesInhunt);
                    currentClue.setPreviousClueId(previousClue.getClueId());
                    previousClue.setNextClueId(currentClue.getClueId());
                }
                mAddModifyClueLabel.setText(getString(R.string.add_clue));
                mLatitudeLabel.setText(mDefaultLocationText);
                mLongitudelabel.setText(mDefaultLocationText);
                mClueDesctiptionEditText.setText("");
                mClueNameEditText.setText("");
                mClueLandmarkEditText.setText("");
            }
            else{
                Clue currentClue = savedbundle.getParcelable("CLUE");
                this.currentClue = currentClue;
                mAddModifyClueLabel.setText(getString(R.string.modify_clue));
                mLatitudeLabel.setText(Double.toString(currentClue.getLocation().getLatitude()));
                mLongitudelabel.setText(Double.toString(currentClue.getLocation().getLongitude()));
                mClueDesctiptionEditText.setText(currentClue.getClueDescription());
                mClueNameEditText.setText(currentClue.getClueTitle());
                mClueLandmarkEditText.setText(currentClue.getLandmarkDescription());
            }


        }
        else{
            this.currentClue = savedbundle.getParcelable("CLUE");
            mLatitudeLabel.setText(Double.toString(currentClue.getLocation().getLatitude()));
            mLongitudelabel.setText(Double.toString(currentClue.getLocation().getLongitude()));
            mClueDesctiptionEditText.setText(currentClue.getClueDescription());
            mClueNameEditText.setText(currentClue.getClueTitle());
            mClueLandmarkEditText.setText(currentClue.getLandmarkDescription());
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
            Double mCurrentLatitude = Double.parseDouble(mLatitudeLabel.getText().toString().equals(mDefaultLocationText)? "0.0":mLatitudeLabel.getText().toString() );
            Double mCurrentLongitude = Double.parseDouble(mLongitudelabel.getText().toString().equals(mDefaultLocationText)? "0.0":mLongitudelabel.getText().toString());
            currentClue.setLocation(new Location(mCurrentLatitude, mCurrentLongitude));
            currentClue.setClueDescription(mClueDesctiptionEditText.getText().toString());
            currentClue.setClueTitle(mClueNameEditText.getText().toString());
            currentClue.setLandmarkDescription(mClueLandmarkEditText.getText().toString());
            //savedbundle.putDouble("LATITUDE", mCurrentLatitude);
            //savedbundle.putDouble("LONGITUDE",mCurrentLongitude);
            //savedbundle.putString("CLUEDESCRITION",mClueDesctiptionEditText.getText().toString());
            //savedbundle.putString("CLUENAME",mClueNameEditText.getText().toString());
            //savedbundle.putString("CLUELANDMARK",mClueLandmarkEditText.getText().toString());
            savedbundle.putParcelable("CLUE",currentClue);
            backToHuntsIntent.putExtras(savedbundle);
            setResult(RESULT_OK,backToHuntsIntent);
            finish();

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
        Intent backToHuntsIntent = new Intent(ClueInfoActivity.this, HuntCreateModify.class);
        Double mCurrentLatitude = Double.parseDouble(mLatitudeLabel.getText().toString().equals(mDefaultLocationText)? "0.0":mLatitudeLabel.getText().toString());
        Double mCurrentLongitude = Double.parseDouble(mLongitudelabel.getText().toString().equals(mDefaultLocationText)? "0.0":mLongitudelabel.getText().toString());
        currentClue.setLocation(new Location(mCurrentLatitude, mCurrentLongitude));
        currentClue.setClueDescription(mClueDesctiptionEditText.getText().toString());
        currentClue.setClueTitle(mClueNameEditText.getText().toString());
        currentClue.setLandmarkDescription(mClueLandmarkEditText.getText().toString());
        savedbundle.putParcelable("CLUE",currentClue);
        savedbundle.putString("ADDMODIFYCLUE",mAddModifyClueLabel.getText().toString());
    }
}
