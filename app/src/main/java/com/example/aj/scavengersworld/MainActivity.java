package com.example.aj.scavengersworld;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aj.scavengersworld.ModifyClueLocationActivity;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button mButton;
    private Button mGamePlayButton;
    private TextView mTextView;
    private static double DEFAULT_LATITUDE = 23.5;
    private static double DEFAULT_LONGITUDE = 83;
    private double mCurrentLatitude;
    private double mCurrentLongitude;

    public MainActivity() {
        mCurrentLatitude = DEFAULT_LATITUDE;
        mCurrentLongitude = DEFAULT_LONGITUDE;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button)findViewById(R.id.button2);
        mGamePlayButton = (Button)findViewById(R.id.button3);
        mButton.setOnClickListener(this);
        mGamePlayButton.setOnClickListener(this);
        mTextView = (TextView)findViewById(R.id.textView2);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button2){
            Intent editLocationIntent = new Intent(MainActivity.this,ClueInfoActivity.class);
            //Intent editLocationIntent = new Intent(MainActivity.this,GamePlayActivity.class);
            //Bundle initLocation = new Bundle();
            editLocationIntent.putExtra("LATITUDE",mCurrentLatitude);
            editLocationIntent.putExtra("LONGITUDE",mCurrentLongitude);
            startActivity(editLocationIntent);
        }
        else if(v.getId() == R.id.button3){
            //Intent editLocationIntent = new Intent(MainActivity.this,ModifyClueLocationActivity.class);
            Intent editLocationIntent = new Intent(MainActivity.this,GamePlayActivity.class);
            //Bundle initLocation = new Bundle();
            editLocationIntent.putExtra("LATITUDE",mCurrentLatitude);
            editLocationIntent.putExtra("LONGITUDE",mCurrentLongitude);
            startActivityForResult(editLocationIntent,2);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            double lLatitude=data.getDoubleExtra("LATITUDE",-1);
            double lLongitude=data.getDoubleExtra("LONGITUDE",-1);
            String message = Double.toString(lLatitude) +","+ Double.toString(lLongitude);
            mTextView.setText(message);
        }
    }
}
