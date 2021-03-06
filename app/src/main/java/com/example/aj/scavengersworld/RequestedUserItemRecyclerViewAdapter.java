package com.example.aj.scavengersworld;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aj.scavengersworld.Model.Hunt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

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
    private FirebaseDatabase mDatabase;
    private final String LOG_TAG = getClass().getSimpleName();

    public RequestedUserItemRecyclerViewAdapter(Hunt currentHunt){
        this.currentHunt = currentHunt;
        mDatabase = FirebaseDatabase.getInstance();
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
        String userID = (String)keys.get(position);
        holder.mUserName.setText(pendingRequests.get(userID));
        holder.userId = userID;
    }

    @Override
    public int getItemCount() {
        if(currentHunt.getPendingRequests() != null)
            return currentHunt.getPendingRequests().size();
        else return 0;
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
            DatabaseReference mUserHuntsDatabaseRef = mDatabase.getReference(v.getContext().getString(R.string.userToHunts) + "/" + userId+"/"+ currentHunt.getHuntName() +"/"+v.getContext().getString(R.string.hunt_state));
            boolean isAccepted = false;
            switch (v.getId()) {
                case R.id.acceptButton:
                    mUserHuntsDatabaseRef.setValue(INPROGRESS);
                    isAccepted = true;
                    break;
                case R.id.rejectButton:
                    mUserHuntsDatabaseRef.setValue(REJECTED);
                    isAccepted = false;
                    break;
                default:
                    break;
            }
            sendNotificationToAdmin(v.getContext(),userId,isAccepted);
            clearPendingRequests(v.getContext());
            notifyDataSetChanged();
            incrementNumberOfPlayersInHunt(v.getContext());
        }
        private void incrementNumberOfPlayersInHunt(final Context context) {
            mDatabase.getReference(context.getString(R.string.searchableHuntsTable))
                    .orderByChild(context.getString(R.string.orderByHuntName))
                    .equalTo(currentHunt.getHuntName()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot searchableHuntSnapshot : dataSnapshot.getChildren()) {
                                String key = searchableHuntSnapshot.getKey();
                                if (key != null) {
                                    DatabaseReference dbRef = mDatabase.getReference(context.getString(R.string.searchableHuntsTable) +
                                            context.getString(R.string.pathSeparator) +
                                            key +
                                            context.getString(R.string.pathSeparator) +
                                            context.getString(R.string.orderByNumPlayers));
                                    dbRef.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long numberOfPlayers = 0;
                                            if (mutableData.getValue() != null) {
                                                numberOfPlayers = (long) mutableData.getValue();
                                                numberOfPlayers++;
                                            }
                                            mutableData.setValue(numberOfPlayers);
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
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        private void clearPendingRequests(Context context){
            currentHunt.getPendingRequests().remove(userId);
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mPendingRequestsReference = mDatabase.getReference(context.getString(R.string.hunts)+"/"+currentHunt.getHuntName()+"/"+context.getString(R.string.pendingRequests)+"/"+userId);
            mPendingRequestsReference.setValue(null);
        }
        private void sendNotificationToAdmin(Context context, String userIdToSend,Boolean isRequestAccepted){
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseUserIdDeviceId = mDatabase.getReference(context.getString(R.string.userToDeviceId))
                    .child(userIdToSend);
            mDatabaseUserIdDeviceId.addListenerForSingleValueEvent(new CustomValueEventListener(context,isRequestAccepted));
        }

        private class CustomValueEventListener implements ValueEventListener {
            private Context context;
            private Boolean isRequestAccepted;

            private CustomValueEventListener(Context context, Boolean isRequestAccepted) {
                this.context = context;
                this.isRequestAccepted = isRequestAccepted;
            }
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String deviceIdString = dataSnapshot.getValue(String.class);
                if(deviceIdString != null){
                    UserSessionManager session = UserSessionManager.INSTANCE;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(session.getUserName());
                    String verb = isRequestAccepted? "aproved" : "declined";
                    stringBuilder.append(" has ");
                    stringBuilder.append(verb);
                    stringBuilder.append(" your request to join ");
                    stringBuilder.append(currentHunt.getHuntName());
                    String notificationBody = stringBuilder.toString();
                    String titleString = isRequestAccepted? context.getString(R.string.notificationApprovalTitle) : context.getString(R.string.notificationDeclineTitle);
                    session.sendNotification(deviceIdString, titleString, notificationBody);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}
