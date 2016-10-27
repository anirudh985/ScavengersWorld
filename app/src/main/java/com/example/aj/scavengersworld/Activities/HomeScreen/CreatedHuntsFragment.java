package com.example.aj.scavengersworld.Activities.HomeScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aj.scavengersworld.HuntCreateModify;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.Activities.HomeScreen.dummy.DummyContent;
import com.example.aj.scavengersworld.Activities.HomeScreen.dummy.DummyContent.DummyItem;
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
public class CreatedHuntsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    //TODO: Need to get huntslist from another class. Don't make a call to retrieve them here.
    private List<Hunt> createdHuntsList = new ArrayList<>();
    private final String LOG_TAG = getClass().getSimpleName();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference("hunts");

    ValueEventListener createdHuntsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot huntSnapshot : dataSnapshot.getChildren()){
                Hunt hunt = huntSnapshot.getValue(Hunt.class);
                if(!createdHuntsList.contains(hunt)){
                    createdHuntsList.add(hunt);
                }
            }
            updateUI(createdHuntsList);
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
    public CreatedHuntsFragment() {
    }

    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static CreatedHuntsFragment newInstance(int columnCount) {
//        CreatedHuntsFragment fragment = new CreatedHuntsFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate Called");
//        mDatabaseRef.addValueEventListener(createdHuntsListener);
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
//                Intent createHunt = new Intent(getActivity(), HuntCreateModify.class);
//                startActivity(createHunt);
                addNewHunt();
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
//            createdHuntsList = getCreatedHuntsList();
            recyclerView.setAdapter(new MyCreatedHuntsRecyclerViewAdapter(createdHuntsList, mListener));
        }
        return view;
    }

    private void updateUI(List<Hunt> createdHuntsList){
        Log.d(LOG_TAG, "updateUI Called");
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.createdHuntListRecyclerView);
        if(recyclerView != null){
            MyCreatedHuntsRecyclerViewAdapter createdHuntsRecyclerViewAdapter = (MyCreatedHuntsRecyclerViewAdapter) recyclerView.getAdapter();
//            createdHuntsRecyclerViewAdapter.swap(createdHuntsList);
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

//    private List<Hunt> getCreatedHuntsList(){
//        List<Hunt> huntsList = new ArrayList<>();
//
//        Hunt hunt1 = new Hunt();
//        hunt1.setHuntId(1);
//        hunt1.setHuntName("CreatedHunt1");
//        huntsList.add(hunt1);
//
//        Hunt hunt2 = new Hunt();
//        hunt2.setHuntId(2);
//        hunt2.setHuntName("CreatedHunt2");
//        huntsList.add(hunt2);
//
//        Hunt hunt3 = new Hunt();
//        hunt3.setHuntId(3);
//        hunt3.setHuntName("CreatedHunt3");
//        huntsList.add(hunt3);
//
//        Hunt hunt4 = new Hunt();
//        hunt4.setHuntId(4);
//        hunt4.setHuntName("CreatedHunt4");
//        huntsList.add(hunt4);
//
//
//
//        return huntsList;
//    }

    private void getCreatedHuntsList(){
        Log.d(LOG_TAG, "getCreatedHuntsList Called");
//        final List<Hunt> createdHuntsList = new ArrayList<>();
        mDatabaseRef.orderByChild("huntId").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "onChildAdded Called");
                Hunt hunt = dataSnapshot.getValue(Hunt.class);
                if(!createdHuntsList.contains(hunt)){
                    createdHuntsList.add(hunt);
                    updateUI(createdHuntsList);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "onChildChanged Called");
                Hunt hunt = dataSnapshot.getValue(Hunt.class);
                if(!createdHuntsList.contains(hunt)){
                    createdHuntsList.add(hunt);
                    updateUI(createdHuntsList);
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
//        createdHuntsList.add(newHunt);
//        mDatabaseRef.child(""+newHuntId).setValue(newHunt);
        mDatabaseRef.push().setValue(newHunt);
    }
}
