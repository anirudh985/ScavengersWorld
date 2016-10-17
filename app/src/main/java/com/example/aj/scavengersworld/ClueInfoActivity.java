package com.example.aj.scavengersworld;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ClueInfoActivity extends Activity implements View.OnClickListener {

    private EditText mClueNameLabel;
    private TextView mLatitudeLabel;
    private TextView mLongitudelabel;
    private Button mLocationEditButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue_info);
        mLatitudeLabel = (TextView) findViewById(R.id.latitudeLabel);
        mLongitudelabel = (TextView) findViewById(R.id.longitudeLabel);
        mLocationEditButton = (Button) findViewById(R.id.editLocationButton);
        mLocationEditButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.editLocationButton) {
            Intent editLocationIntent = new Intent(ClueInfoActivity.this,ModifyClueLocationActivity.class);
            //Intent editLocationIntent = new Intent(MainActivity.this,GamePlayActivity.class);
            //Bundle initLocation = new Bundle();
            Double mCurrentLatitude = 0.0;//Double.parseDouble(mLatitudeLabel.getText().toString());
            Double mCurrentLongitude = 0.0;//Double.parseDouble(mLongitudelabel.getText().toString());
            if(mCurrentLatitude == 0) mCurrentLatitude = 23.5;
            if(mCurrentLongitude == 0) mCurrentLongitude = 83.5;
            editLocationIntent.putExtra("LATITUDE",mCurrentLatitude);
            editLocationIntent.putExtra("LONGITUDE",mCurrentLongitude);
            startActivityForResult(editLocationIntent,2);
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
}
