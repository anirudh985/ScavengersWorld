package com.example.aj.scavengersworld.CluesRelated;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.aj.scavengersworld.Activities.HomeScreen.MyCreatedHuntsRecyclerViewAdapter;
import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.Model.Location;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;

import java.util.List;

/**
 * Created by kalyan on 11/3/16.
 */

public class ClueItemRecyclerViewAdapter extends RecyclerView.Adapter<ClueItemRecyclerViewAdapter.ViewHolder> {
    private final ColorGenerator mGenerator = ColorGenerator.MATERIAL;
    private String[] mDataset;
    private final String LOG_TAG = getClass().getSimpleName();
    private UserSessionManager session = UserSessionManager.INSTANCE;
    private Location currentLocation;
    private Hunt currentHunt;
    private CurrentClueActivity.CluesDisplayMode cluesDisplayMode;
    public ClueItemRecyclerViewAdapter(Location currentLocation, Hunt currentHunt, CurrentClueActivity.CluesDisplayMode cluesDisplayMode) {
        this.currentLocation = currentLocation;
        this.currentHunt = currentHunt;
        this.cluesDisplayMode = cluesDisplayMode;
    }
    @Override
    public ClueItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_clue_item, parent, false);
        return new ClueItemRecyclerViewAdapter.ViewHolder(view);
    }
    public class ClueClickListener implements View.OnClickListener{
        private Clue clue;
        private Location currentLocation;
        ClueClickListener(Clue clue, Location currentLocation){
            this.clue = clue;
        }
        @Override
        public void onClick(View v) {
            if(clue.getLocation().distanceFromLocationInMeters(currentLocation) < 10.0)
            {

            }
        }
    }

    @Override
    public void onBindViewHolder(ClueItemRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.w(LOG_TAG, "*****GET Position"+ position);
        if(cluesDisplayMode == CurrentClueActivity.CluesDisplayMode.CURRENTCLUES){
            int count = 0;
            for(Hunt hunt : session.getParticipatingHuntsList()){
                if(hunt.getClueList() != null) {
                    count += 1;
                    if (count-1 == position){
                        Clue currentClue =  hunt.getCurrentClue();
                        SetUIElementsFromClue(holder,currentClue);
                        holder.mView.setOnClickListener(new ClueClickListener(currentClue,currentLocation));
                    }
                }
            }
        }
        else{
            Clue currentClue =  currentHunt.getClueAtSequence(position+1);
            SetUIElementsFromClue(holder,currentClue);
        }


    }
    private void SetUIElementsFromClue(ClueItemRecyclerViewAdapter.ViewHolder holder,Clue clue){
        holder.mClueNameView.setText(clue.getClueTitle());

        String letter = String.valueOf("C");
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, mGenerator.getRandomColor());
        //holder.mImageLetter.setImageDrawable(drawable);
        holder.mHuntNameView.setText(clue.getHuntName());
        holder.mClueDescriptionView.setText(clue.getClueTitle());
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(cluesDisplayMode == CurrentClueActivity.CluesDisplayMode.HUNTCLUES){
            count  = currentHunt.getCurrentClue().getSequenceNumberInHunt();
        }
        else {
            for (Hunt hunt : session.getParticipatingHuntsList()) {
                if (hunt.getClueList() != null)
                    count += 1;
            }
        }
        Log.w(LOG_TAG, "*****GET COUNT"+ count);
        return count;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //        public final TextView mProgressView;
        public final TextView mClueNameView;
        public final TextView mHuntNameView;
        public final TextView mClueDescriptionView;
//        public Hunt mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mProgressView = (TextView) view.findViewById(R.id.progress);
            mClueNameView = (TextView) view.findViewById(R.id.clueName);
            mHuntNameView = (TextView) view.findViewById(R.id.huntName);
            mClueDescriptionView = (TextView) view.findViewById(R.id.clueDescription);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mClueNameView.getText() + "'";
        }
    }
}
