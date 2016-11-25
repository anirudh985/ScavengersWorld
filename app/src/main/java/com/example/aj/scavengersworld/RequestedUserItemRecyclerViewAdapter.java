package com.example.aj.scavengersworld;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aj.scavengersworld.Model.Hunt;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.example.aj.scavengersworld.Constants.INPROGRESS;
import static com.example.aj.scavengersworld.Constants.REJECTED;
/**
 * Created by kalyan on 11/25/16.
 */

public class RequestedUserItemRecyclerViewAdapter extends RecyclerView.Adapter<RequestedUserItemRecyclerViewAdapter.ViewHolder> {
    private Hunt currentHunt;

    public RequestedUserItemRecyclerViewAdapter(Hunt currentHunt){
        this.currentHunt = currentHunt;
    }

    @Override
    public RequestedUserItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_requested_user_item, parent, false);
        return new RequestedUserItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestedUserItemRecyclerViewAdapter.ViewHolder holder, int position) {
        HashMap<String,String> pendingRequests = currentHunt.getPendingRequests();
        List keys = new ArrayList(pendingRequests.keySet());
        String userName = (String)keys.get(position);
        holder.mUserName.setText(userName);
        holder.userId = pendingRequests.get(userName);
    }

    @Override
    public int getItemCount() {
        return currentHunt.getPendingRequests().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        //        public final TextView mProgressView;
        public final TextView mUserName;
        public final Button acceptButton;
        public final Button rejectButton;
        public String userId;
//        public Hunt mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUserName = (TextView) view.findViewById(R.id.requestedUsername);
            acceptButton = (Button) view.findViewById(R.id.acceptButton);
            acceptButton.setOnClickListener(this);
            rejectButton = (Button) view.findViewById(R.id.rejectButton);
            rejectButton.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" +   "'";
        }

        @Override
        public void onClick(View v) {
            //UserSessionManager session = UserSessionManager.INSTANCE;
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mUserHuntsDatabaseRef = mDatabase.getReference(v.getContext().getString(R.string.userToHunts) + "/" + userId+"/"+ currentHunt.getHuntName() +"/"+v.getContext().getString(R.string.hunt_state));
            switch (v.getId()){
                case R.id.acceptButton:
                    mUserHuntsDatabaseRef.setValue(INPROGRESS);
                    break;
                case R.id.rejectButton:
                    mUserHuntsDatabaseRef.setValue(REJECTED);
                    break;
                default:
                    break;
            }
        }
    }
}
