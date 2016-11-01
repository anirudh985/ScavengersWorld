package com.example.aj.scavengersworld.Activities.HuntsFeed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aj.scavengersworld.Activities.HomeScreen.MyCreatedHuntsRecyclerViewAdapter;
import com.example.aj.scavengersworld.HuntCreateModify;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PopularHuntsFeedFragment extends Fragment {

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Hunt> createdHuntsList = new ArrayList<>();
    private final String LOG_TAG = getClass().getSimpleName();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;
    private UserSessionManager session = UserSessionManager.INSTANCE;

    ValueEventListener createdHuntsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot huntSnapshot : dataSnapshot.getChildren()){
                Hunt hunt = huntSnapshot.getValue(Hunt.class);
                if(!createdHuntsList.contains(hunt)){
                    createdHuntsList.add(hunt);
                }
            }
            updateUI();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(LOG_TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PopularHuntsFeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate Called");
        mDatabaseRef = mDatabase.getReference(getString(R.string.userCreatedHuntsTable));
        getCreatedHuntsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView Called");
        View view = inflater.inflate(R.layout.fragment_createdhunts_list, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createHunt = new Intent(getActivity(), HuntCreateModify.class);
                startActivity(createHunt);
//                addNewHunt();
            }
        });

        // Set the adapter
        if (view instanceof CoordinatorLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.createdHuntListRecyclerView);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyCreatedHuntsRecyclerViewAdapter(createdHuntsList, mListener));
        }
        return view;
    }

    private void updateUI(){
        Log.d(LOG_TAG, "updateUI Called");
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.createdHuntListRecyclerView);
        if(recyclerView != null){
            MyCreatedHuntsRecyclerViewAdapter createdHuntsRecyclerViewAdapter = (MyCreatedHuntsRecyclerViewAdapter) recyclerView.getAdapter();
            createdHuntsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_TAG, "onAttach Called");
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(LOG_TAG, "onDetach Called");
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListCreatedHuntsFragmentInteraction(Hunt hunt);
    }

    private void getCreatedHuntsList(){
        Log.d(LOG_TAG, "getCreatedHuntsList Called");
        mDatabaseRef.child(session.getUniqueUserId())
                    .orderByChild(getString(R.string.orderByHuntId))
                    .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "onChildAdded Called");
                Hunt hunt = dataSnapshot.getValue(Hunt.class);
                if(!createdHuntsList.contains(hunt)){
                    createdHuntsList.add(hunt);
                    updateUI();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "onChildChanged Called");
                Hunt hunt = dataSnapshot.getValue(Hunt.class);
                if(!createdHuntsList.contains(hunt)){
                    createdHuntsList.add(hunt);
                    updateUI();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Hunt createNewHunt(@NonNull int huntId, @NonNull String huntName, @NonNull String createdByUser){
        Log.d(LOG_TAG, "createNewHunt Called");
        Hunt hunt = new Hunt();
        hunt.setHuntId(huntId);
        hunt.setHuntName(huntName);
        hunt.setCreatedByUserId(createdByUser);
        return hunt;
    }

    private void addNewHunt(){
        Log.d(LOG_TAG, "addNewHunt Called");
        int newHuntId = createdHuntsList.size() + 1;
        Hunt newHunt = createNewHunt(newHuntId, "CreatedHunt"+newHuntId, "anirudh985");
        mDatabaseRef.child(session.getUniqueUserId()).push().setValue(newHunt);
    }
}
