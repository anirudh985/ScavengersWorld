package com.example.aj.scavengersworld.Activities.HomeScreen;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;

import java.util.List;


public class MyYourHuntsRecyclerViewAdapter extends RecyclerView.Adapter<MyYourHuntsRecyclerViewAdapter.ViewHolder> {

    private final List<Hunt> mValues;
    private final YourHuntsFragment.OnListFragmentInteractionListener mListener;
    private final ColorGenerator mGenerator = ColorGenerator.MATERIAL;

    public MyYourHuntsRecyclerViewAdapter(List<Hunt> items, YourHuntsFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.fragment_yourhunts, parent, false);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_yourhunts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Hunt hunt = mValues.get(position);
        holder.mIdView.setText(String.valueOf(hunt.getHuntId()));
        holder.mContentView.setText(hunt.getHuntName());
        holder.mCreatedUser.setText(hunt.getCreatedByUserId());

        String letter = String.valueOf(hunt.getHuntName().charAt(0));
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
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mCreatedUser;
        public final ImageView mImageLetter;
//        public Hunt mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.yourHuntId);
            mContentView = (TextView) view.findViewById(R.id.yourHuntName);
            mCreatedUser = (TextView) view.findViewById(R.id.yourHuntCreatedUser);
            mImageLetter = (ImageView) view.findViewById(R.id.yourHuntImageLetter);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + " " + mCreatedUser.getText()+"'";
        }
    }
}