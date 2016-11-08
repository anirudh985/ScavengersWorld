package com.example.aj.scavengersworld.CluesRelated;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;

import java.util.List;

/**
 * Created by Jennifer on 11/7/2016.
 */

public class UpdateClueRecyclerViewAdapter extends RecyclerView.Adapter<UpdateClueRecyclerViewAdapter.ViewHolder> {
	private String mhuntName;
	private Hunt mHunt;
	private List<Clue> huntClues;
	private UserSessionManager session = UserSessionManager.INSTANCE;

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View mView;
		public final TextView mClueNameView;
		public final TextView mHuntNameView;
		public final TextView mClueDescriptionView;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			mClueNameView = (TextView) view.findViewById(R.id.clueName);
			mHuntNameView = (TextView) view.findViewById(R.id.huntName);
			mClueDescriptionView = (TextView) view.findViewById(R.id.clueDescription);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + mClueNameView.getText() + "'";
		}
	}


	public UpdateClueRecyclerViewAdapter(String huntName) {
		mhuntName = huntName;
		mHunt = session.getAdminHuntByName(mhuntName);
		huntClues = mHunt.getClueList();
	}

	// Create new views (invoked by the layout manager)
	@Override
	public UpdateClueRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
															int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_clue_item, parent, false);
		return new UpdateClueRecyclerViewAdapter.ViewHolder(view);
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(UpdateClueRecyclerViewAdapter.ViewHolder holder, int position) {
		holder.mClueNameView.setText(huntClues.get(position).getClueTitle());
		holder.mClueDescriptionView.setText(huntClues.get(position).getClueDescription());
		holder.mHuntNameView.setText(huntClues.get(position).getHuntName());
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		huntClues = mHunt.getClueList();
		if(huntClues == null) {
			return 0;
		}
		return huntClues.size();
	}
}
