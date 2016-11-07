package com.example.aj.scavengersworld.CluesRelated;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aj.scavengersworld.Activities.BaseActivity;
import com.example.aj.scavengersworld.GamePlayActivity;
import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        Clue nextClue = currentHunt.getClueAtSequence(currentClue.getSequenceNumberInHunt());
        currentHunt.setCurrentClueId(nextClue.getClueId());
        mDatabaseRef = mDatabase.getReference(getString(R.string.userToHunts) + "/" + session.getUniqueUserId()+"/"+ currentHunt.getHuntName() +"/"+R.string.clueId);
        mDatabaseRef.setValue(nextClue.getClueId());
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
        clueResult = outState.getBoolean("currentResult");
    }

    private void updateElementsFromIntent(Intent createdIntent){
        Bundle extrasBundle = createdIntent.getExtras();
        if(extrasBundle != null ) {
            String huntName = extrasBundle.getString("HUNTNAME");
            currentHunt = session.getParticipatingHuntByName(huntName);
            if (extrasBundle.getBoolean("RESULT")) {
                clueResult = true;
                Clue currentClue = currentHunt.getCurrentClue();
                updateUIElements(getString(R.string.clue_feedback_success),currentClue.getClueDescription(),currentClue.getHuntName() + currentClue.getClueTitle());
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
