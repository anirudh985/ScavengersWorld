package com.example.aj.scavengersworld.CluesRelated;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.GamePlayActivity;
import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.Calendar;

import static com.example.aj.scavengersworld.Constants.COMPLETED;
import static com.example.aj.scavengersworld.Constants.INPROGRESS;

public class ClueFeedbackActivity extends BaseActivity {
    private boolean clueResult;
    private Hunt currentHunt;
    private UserSessionManager session = UserSessionManager.INSTANCE;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;
    private TextView clueResultView;
    private TextView clueFeedbackView;
    private TextView clueDescriptionView;
    private Button gameplayButton;

    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clueResultView = (TextView) findViewById(R.id.clue_result);
        clueFeedbackView = (TextView) findViewById(R.id.clue_feedback);
        clueDescriptionView = (TextView) findViewById(R.id.clue_description);
        gameplayButton = (Button) findViewById(R.id.back_to_gameplay);
        //setContentView(R.layout.activity_clue_feedback);
        SetupGameplayButton();
        if(savedInstanceState != null)
            updateElementsFromBundle(savedInstanceState);
        else{
            Intent createdIntent = getIntent();
            updateElementsFromIntent(createdIntent);
        }
    }
    private void updateUIElements(String resultString, String feedbackString, String descriptionString){
        clueResultView.setText(resultString);
        clueFeedbackView.setText(feedbackString);
        clueDescriptionView.setText(descriptionString);
    }

    private void updateClueSuccess(){
        Clue currentClue = currentHunt.getCurrentClue();
        Clue nextClue = currentHunt.getClueAtSequence(currentClue.getSequenceNumberInHunt()+1);
        Double progress = 0.0;
        int totalCluesInCurrentHunt = currentHunt.getClueList().size();
        if(nextClue == null)
        {
            if(currentClue.getSequenceNumberInHunt() == totalCluesInCurrentHunt)
            {
                updateUIElements(getString(R.string.clue_feedback_hunt_complete),currentClue.getClueDescription(),currentClue.getHuntName() + currentClue.getClueTitle());
                progress = 100.0;
                mDatabaseRef = mDatabase.getReference(getString(R.string.userToHunts) + "/" + session.getUniqueUserId()+"/"+ currentHunt.getHuntName() +"/"+getString(R.string.hunt_state));
                mDatabaseRef.setValue(COMPLETED);
                currentHunt.setState(COMPLETED);
                session.removeHunt(INPROGRESS,currentHunt);
                session.addHunt(COMPLETED,currentHunt);
            }
            else{
                updateUIElements(getString(R.string.clue_feedback_success),currentClue.getClueDescription(),currentClue.getHuntName() + currentClue.getClueTitle());
            }
        }
        else{
            updateUIElements(getString(R.string.clue_feedback_success),currentClue.getClueDescription(),currentClue.getHuntName() + currentClue.getClueTitle());
            currentHunt.setCurrentClueSequence(nextClue.getSequenceNumberInHunt());
            progress = (double) currentClue.getSequenceNumberInHunt() * 100 / totalCluesInCurrentHunt;
            mDatabaseRef = mDatabase.getReference(getString(R.string.userToHunts) + "/" + session.getUniqueUserId()+"/"+ currentHunt.getHuntName() +"/"+getString(R.string.currentClueSequence));
            mDatabaseRef.setValue(nextClue.getSequenceNumberInHunt());
        }
        mDatabaseRef = mDatabase.getReference(getString(R.string.userToHunts) + "/" + session.getUniqueUserId()+"/"+ currentHunt.getHuntName() +"/"+getString(R.string.progress));
        mDatabaseRef.setValue(progress.intValue());
        mDatabaseRef = mDatabase.getReference(getString(R.string.userToHunts) + "/" + session.getUniqueUserId()+"/"+ currentHunt.getHuntName() +"/"+getString(R.string.score));
        mDatabaseRef.setValue(progress.intValue());
        mDatabaseRef = mDatabase.getReference(getString(R.string.huntsToLeaders) + "/" + currentHunt.getHuntName() +"/"+session.getUserName());
        mDatabaseRef.setValue(progress.intValue());
        currentHunt.setProgress(progress);
        double previousProgress = (double) (currentClue.getSequenceNumberInHunt() - 1) * 100 / totalCluesInCurrentHunt;
        double incrementScore = progress - previousProgress;
        incrementScoreInProfile(incrementScore);

    }


    private void incrementScoreInProfile(double scoreToIncrement){
        final long incrementScore = (long)scoreToIncrement;
        mDatabase.getReference(getString(R.string.userToProfile) + getString(R.string.pathSeparator) +
                                session.getUniqueUserId() + getString(R.string.pathSeparator) +
                                session.getUserProfileKey() + getString(R.string.pathSeparator) +
                                "pointsEarned").
                runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        double userScore = 0.0;
                        if (mutableData.getValue() != null) {
                            userScore = (long)mutableData.getValue();
                            userScore += incrementScore;
                            Double score = userScore;
                            session.getUserProfile().setPointsEarned(score.intValue());
                        }
                        mutableData.setValue(userScore);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        if (databaseError != null) {
                            Log.d(LOG_TAG, "postTransaction Error: Error in updating database \n" + databaseError);
                        }
                    }
                });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_clue_feedback;
    }

    @Override
    protected String getScreenName() {
            return getString(R.string.welcomeMessage) + session.getUserName();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("resultString", (String) clueResultView.getText());
        outState.putString("feedbackString", (String) clueFeedbackView.getText());
        outState.putString("descriptionString", (String) clueDescriptionView.getText());
        outState.putString("huntName",currentHunt.getHuntName());
        outState.putBoolean("currentResult",clueResult);
    }

    private void updateElementsFromBundle(Bundle outState){
        updateUIElements(outState.getString("resultString"),outState.getString("feedbackString"),outState.getString("descriptionString"));
        currentHunt = session.getParticipatingHuntByName(outState.getString("huntName"));
        if(currentHunt == null)
            currentHunt = session.getCompletedHuntByName(outState.getString("huntName"));
        clueResult = outState.getBoolean("currentResult");
    }

    private void updateElementsFromIntent(Intent createdIntent){
        Bundle extrasBundle = createdIntent.getExtras();
        if(extrasBundle != null ) {
            String huntName = extrasBundle.getString("HUNTNAME");
            currentHunt = session.getParticipatingHuntByName(huntName);
            if (extrasBundle.getBoolean("RESULT")) {
                clueResult = true;
                updateClueSuccess();
            }
            else{
                clueResult = false;
                Clue currentClue = currentHunt.getCurrentClue();
                updateUIElements(getString(R.string.clue_feedback_failure),currentClue.getClueDescription(),currentClue.getHuntName() + currentClue.getClueTitle());
            }
        }
    }

    private void SetupGameplayButton(){
        gameplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ClueFeedbackActivity.this,GamePlayActivity.class);
                startActivity(intent);
            }
        });
    }
}
