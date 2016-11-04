package com.example.aj.scavengersworld.Activities.HuntsFeed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.aj.scavengersworld.DatabaseModels.SearchableHunt;
import com.example.aj.scavengersworld.R;

import static com.example.aj.scavengersworld.Constants.POPULARITY_THRESHOLD;

import java.util.List;

public class MyPopularHuntsRecyclerViewAdapter extends RecyclerView.Adapter<MyPopularHuntsRecyclerViewAdapter.ViewHolder> {

    private final List<SearchableHunt> mValues;
    private final PopularHuntsFeedFragment.OnListFragmentInteractionListener mListener;
    private final ColorGenerator mGenerator = ColorGenerator.MATERIAL;

    public MyPopularHuntsRecyclerViewAdapter(List<SearchableHunt> items, PopularHuntsFeedFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_popularhuntsfeed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //TODO: need to change this later
        final SearchableHunt popularHunt = mValues.get(position);
//        holder.mProgressView.setText(progressMessage + String.valueOf(hunt.getProgress()));
        holder.mContentView.setText(popularHunt.getHuntName());
//        holder.mCreatedUserView.setText(hunt.getCreatedByUserId());
        holder.mRatingBar.setRating(getRating(popularHunt));

        String letter = String.valueOf(popularHunt.getHuntName().charAt(0));
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, mGenerator.getRandomColor());
        holder.mImageLetter.setImageDrawable(drawable);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.);
                    mListener.onListPopularHuntsFragmentInteraction(popularHunt);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private float getRating(SearchableHunt popularHunt){
        return popularHunt.getNumberOfPlayers()/POPULARITY_THRESHOLD >= 1 ? 5.0f : (popularHunt.getNumberOfPlayers()/POPULARITY_THRESHOLD)*5.0f;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mImageLetter;
//        public final TextView mCreatedUserView;
        public final RatingBar mRatingBar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.popularHuntsName);
            mImageLetter = (ImageView) view.findViewById(R.id.popularHuntsImageLetter);
//            mCreatedUserView = (TextView) view.findViewById(R.id.popularHuntsCreatedUser);
            mRatingBar = (RatingBar) view.findViewById(R.id.popularityRatingBar);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
