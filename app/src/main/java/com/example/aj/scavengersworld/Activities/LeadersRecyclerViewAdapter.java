package com.example.aj.scavengersworld.Activities;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aj.scavengersworld.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jennifer on 11/28/2016.
 */

public class LeadersRecyclerViewAdapter extends RecyclerView.Adapter<LeadersRecyclerViewAdapter.ViewHolder> {

	private final String LOG_TAG = getClass().getSimpleName();
	private Map<String, Integer> leaders;

	public LeadersRecyclerViewAdapter(Map<String, Integer> huntLeaders) {
		leaders = huntLeaders;
	}

	@Override
	public LeadersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_leader, parent, false);
		return new LeadersRecyclerViewAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(LeadersRecyclerViewAdapter.ViewHolder holder, int position) {
		//Cast Map to List
		List<Map.Entry<String, Integer>> indexedList = new ArrayList<>(leaders.entrySet());
		// Get the pair at position
		Map.Entry<String,Integer> leader = indexedList.get(position);
		String username = leader.getKey();
		int score = leader.getValue();

		holder.mUserNameView.setText(username);
		holder.mScoreView.setText(Integer.toString(score));
	}

	@Override
	public int getItemCount() {
		int count = 0;
		if(leaders != null) {
			count = leaders.size();
		}
		Log.w(LOG_TAG, "*****GET COUNT"+ count);
		return count;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View mView;
		public final TextView mUserNameView;
		public final TextView mScoreView;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			mUserNameView = (TextView) view.findViewById(R.id.username);
			mScoreView = (TextView) view.findViewById(R.id.user_score);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + mUserNameView.getText() + "'";
		}
	}
}
