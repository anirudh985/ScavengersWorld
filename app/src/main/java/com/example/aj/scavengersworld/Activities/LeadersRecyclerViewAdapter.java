package com.example.aj.scavengersworld.Activities;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aj.scavengersworld.R;

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

		public ViewHolder(View view) {
			super(view);
			mView = view;
			mUserNameView = (TextView) view.findViewById(R.id.username);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + mUserNameView.getText() + "'";
		}
	}
}
