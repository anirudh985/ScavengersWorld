package com.example.aj.scavengersworld.CluesRelated;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.Activities.HomeScreen.CreatedHuntsFragment;
import com.example.aj.scavengersworld.Activities.HomeScreen.MyCreatedHuntsRecyclerViewAdapter;
import com.example.aj.scavengersworld.DatabaseModels.HuntsToClues;
import com.example.aj.scavengersworld.DatabaseModels.UserToHunts;
import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.Model.Location;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CurrentClueActivity extends BaseActivity {
    private int mColumnCount = 1;
    private CreatedHuntsFragment.OnListFragmentInteractionListener mListener;
    private UserSessionManager session = UserSessionManager.INSTANCE;
    //private List<Hunt> yourHuntsList = session.getParticipatingHuntsList();
    private final String LOG_TAG = getClass().getSimpleName();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int numberOfEventListeners;
    private Location currentLocation;
    private Hunt currentHunt;
    public enum CluesDisplayMode{
        CURRENTCLUES ,
        HUNTCLUES;
    }
    private CluesDisplayMode cluesDisplayMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        numberOfEventListeners = 0;
        Intent createdIntent = getIntent();
        UpdateClassVariablesFromIntentInfo(createdIntent);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);
       // getCurrentCluesAndSaveInSession();
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //String [] myDataset = {};
        mAdapter = new ClueItemRecyclerViewAdapter(currentLocation,currentHunt,cluesDisplayMode);
        mRecyclerView.setAdapter(mAdapter);
    }
    ValueEventListener huntsToCluesListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot userToHuntsSnapshot : dataSnapshot.getChildren()){
                Hunt currentHunt = session.getParticipatingHuntByName(dataSnapshot.getKey());
                Clue newClue = userToHuntsSnapshot.getValue(Clue.class);
                newClue.setHuntName(currentHunt.getHuntName());
                currentHunt.addClueToClueList(newClue);
                int a  = 1;
            }
            numberOfEventListeners-=1;
            if(numberOfEventListeners == 0)
                updateUI();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(LOG_TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };
    private void UpdateClassVariablesFromIntentInfo(Intent createdIntent){
        Bundle extrasBundle = createdIntent.getExtras();
        if(extrasBundle != null ){
            if(extrasBundle.getString("MODE").equals("CURRENTCLUES"))
            {
                cluesDisplayMode = CluesDisplayMode.CURRENTCLUES;
                double latitude = extrasBundle.getDouble("LATITUDE");
                double longitude = extrasBundle.getDouble("LONGITUDE");
                currentLocation = new Location(latitude,longitude);
            }
            else if(extrasBundle.getString("MODE").equals("HUNTCLUES")) {
                cluesDisplayMode = CluesDisplayMode.HUNTCLUES;
                String huntName  = extrasBundle.getString("HUNTNAME");
                currentHunt = session.getParticipatingHuntByName(huntName);
            }
        }
        else{
            Log.w(LOG_TAG, "Location not received");
        }
    }
    public void updateUI(){
        Log.d(LOG_TAG, "updateUI Called");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if(recyclerView != null){
            ClueItemRecyclerViewAdapter createdHuntsRecyclerViewAdapter = (ClueItemRecyclerViewAdapter) recyclerView.getAdapter();
            createdHuntsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
    private void getCurrentCluesAndSaveInSession(){
        List<Hunt> participatingHuntsList = session.getParticipatingHuntsList();
        for(Hunt curHunt : participatingHuntsList){
            numberOfEventListeners+=1;
            mDatabaseRef = mDatabase.getReference(getString(R.string.huntsToClues) + "/" +curHunt.getHuntName());
            mDatabaseRef.addListenerForSingleValueEvent(huntsToCluesListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "******* onStart() **********");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "******* onResume() **********");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "******* onPause() **********");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "******* onStop() **********");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "******* onDestroy() **********");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "******* onRestart() **********");
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_current_clue;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.welcomeMessage) + session.getUserName();
    }
}
