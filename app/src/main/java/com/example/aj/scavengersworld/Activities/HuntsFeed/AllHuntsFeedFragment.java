package com.example.aj.scavengersworld.Activities.HuntsFeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aj.scavengersworld.DatabaseModels.SearchableHunt;
import com.example.aj.scavengersworld.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AllHuntsFeedFragment extends Fragment {

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    // Had to do arraylist because while saving state, bundle explicitly requires arraylist (not list)
    private ArrayList<SearchableHunt> allHuntsList = new ArrayList<>();
    public final String LOG_TAG = getClass().getSimpleName();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;

    private boolean mIsLoading = false;
    private boolean mIsLastPage = false;
    private int mSizeOfItemsAlreadyLoadedAfterLastVisibleItem = 5;

    private String mLastKeyRetrieved;
    private int sizeOfEachRetrieval = 20;

    private EditText searchQuery;

    // PREFERENCES //
    SharedPreferences sharedPrefs;
    private boolean showPrivateHunts;
    private double maxRadius;
    private double startRadius;
    private String searchString;

    private int numberOfItemsPreviouslyLoaded;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AllHuntsFeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate() called");
        mDatabaseRef = mDatabase.getReference(getString(R.string.searchableHuntsTable));
        loadPreferences();
//        loadInitially();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView() called");
        View view = inflater.inflate(R.layout.fragment_allhuntsfeed_list, container, false);

        // Set the adapter
        if (view instanceof LinearLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.allHuntsListRecyclerView);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyAllHuntsFeedRecyclerViewAdapter(allHuntsList, mListener));

            // Pagination
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                    if(dy > 0) //check for scroll down
//                    {
//                        int visibleItemCount = layoutManager.getChildCount();
//                        int totalItemCount = layoutManager.getItemCount();
//                        int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
//
//                        //TODO: need a way to find out whether we reached last page or not
//                        if (!mIsLoading && !mIsLastPage)
//                        {
//                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount - mSizeOfItemsAlreadyLoadedAfterLastVisibleItem)
//                            {
//                                mIsLoading = true;
//                                Log.d(LOG_TAG, "... Last Item Wow !");
//                                //Do pagination.. i.e. fetch new data
//                                loadMoreHunts(searchQuery.getText().toString());
//                            }
//                        }
//                    }
//                }
//            });

            searchQuery = (EditText) view.findViewById(R.id.searchText);
            searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener(){
               @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event){
                   if(actionId == EditorInfo.IME_ACTION_SEARCH){
                       searchString = view.getText().toString();
                       performSearch();
                       return true;
                   }
                   return false;
               }
            });

            // Filter Preference Listener
            ImageButton filterButton = (ImageButton) view.findViewById(R.id.filterButton);
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent searchPreferenceActivity = new Intent(getActivity(), SearchPreferenceActivity.class);
                    startActivity(searchPreferenceActivity);
                }
            });

//            if(savedInstanceState != null && savedInstanceState.get("All_hunts") != null){
//                allHuntsList = savedInstanceState.getParcelableArrayList("All_hunts");
//                updateUI(1);
//            }
//            else{
//                loadInitially();
//            }

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "onAttach() called");
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "onDetach() called");
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListAllHuntsFeedFragmentInteraction(SearchableHunt hunt);
    }


//    private void loadMoreHunts(){
//        if(mLastKeyRetrieved != null) {
//            int sizeOfCurrentRetrieval = sizeOfEachRetrieval;
//            if(numberOfItemsPreviouslyLoaded < sizeOfEachRetrieval){
//                sizeOfCurrentRetrieval = sizeOfEachRetrieval - numberOfItemsPreviouslyLoaded;
//            }
//                if (maxRadius != -1.0 && maxRadius > 0) {
//                    if (searchString != null && !searchString.equals("")) {
//                        mDatabaseRef.orderByChild("maxRadius")
//                                .startAt(startRadius)
//                                .endAt(maxRadius)
//                                .orderByChild("huntName")
//                                .startAt(searchString.charAt(0))
//                                .endAt(searchString.charAt(0) + getString(R.string.escapeChar))
//                                .orderByChild("isPrivate")
//                                .equalTo(showPrivateHunts)
//                                .startAt(mLastKeyRetrieved)
//                                .limitToFirst(sizeOfEachRetrieval)
//                                .addListenerForSingleValueEvent(paginatedHuntLoadListener);
//                    }
//                    else{
//                        mDatabaseRef.orderByChild("maxRadius")
//                                .startAt(startRadius)
//                                .endAt(maxRadius)
//                                .orderByChild("isPrivate")
//                                .equalTo(showPrivateHunts)
//                                .startAt(mLastKeyRetrieved)
//                                .limitToFirst(sizeOfEachRetrieval)
//                                .addListenerForSingleValueEvent(paginatedHuntLoadListener);
//                    }
//                }
//            else{
//                    if (searchString != null && !searchString.equals("")) {
//                        mDatabaseRef.orderByChild("huntName")
//                                .startAt(searchString.charAt(0))
//                                .endAt(searchString.charAt(0) + getString(R.string.escapeChar))
//                                .orderByChild("isPrivate")
//                                .equalTo(showPrivateHunts)
//                                .startAt(mLastKeyRetrieved)
//                                .limitToFirst(sizeOfEachRetrieval)
//                                .addListenerForSingleValueEvent(paginatedHuntLoadListener);
//                    }
//                    else{
//                        mDatabaseRef.orderByChild("isPrivate")
//                                .equalTo(showPrivateHunts)
//                                .startAt(mLastKeyRetrieved)
//                                .limitToFirst(sizeOfEachRetrieval)
//                                .addListenerForSingleValueEvent(paginatedHuntLoadListener);
//                    }
//                }
//            }
//    }

    private void loadInitially(){
        Log.d(LOG_TAG, "loadInitially() called");
//        if(mLastKeyRetrieved == null) {
//            if (maxRadius != -1.0 && maxRadius > 0) {
//                if (searchQuery != null && !searchQuery.equals("")) {
//                    mDatabaseRef.orderByChild("maxRadius")
//                            .startAt(startRadius)
//                            .endAt(maxRadius)
//                            .orderByChild("huntName")
//                            .startAt(searchQuery.charAt(0))
//                            .endAt(searchQuery.charAt(0) + getString(R.string.escapeChar))
//                            .orderByChild("isPrivate")
//                            .equalTo(showPrivateHunts)
//                            .limitToFirst(sizeOfEachRetrieval)
//                            .addListenerForSingleValueEvent(initialLoadHuntListener);
//
//                    mDatabaseRef.orderByChild("huntName")
//                                .startAt(searchQuery.charAt(0))
//                                .endAt(searchQuery.charAt(0)+getString(R.string.escapeChar))
//                                .addListenerForSingleValueEvent(initialLoadHuntListener);
//                }
//                else{
//                    mDatabaseRef.orderByChild("maxRadius")
//                            .startAt(startRadius)
//                            .endAt(maxRadius)
//                            .orderByChild("isPrivate")
//                            .equalTo(showPrivateHunts)
//                            .limitToFirst(sizeOfEachRetrieval)
//                            .addListenerForSingleValueEvent(initialLoadHuntListener);
//                }
//            }
//            else{
//                if (searchQuery != null && !searchQuery.equals("")) {
//                    mDatabaseRef.orderByChild("huntName")
//                            .startAt(searchQuery.charAt(0))
//                            .endAt(searchQuery.charAt(0) + getString(R.string.escapeChar))
//                            .orderByChild("isPrivate")
//                            .equalTo(showPrivateHunts)
//                            .limitToFirst(sizeOfEachRetrieval)
//                            .addListenerForSingleValueEvent(initialLoadHuntListener);
//                }
//                else{
//                    mDatabaseRef.orderByChild("isPrivate")
//                            .equalTo(showPrivateHunts)
//                            .limitToFirst(sizeOfEachRetrieval)
//                            .addListenerForSingleValueEvent(initialLoadHuntListener);
//                }
//            }
//        }

        if(mLastKeyRetrieved == null) {
            if (searchString != null && !searchString.equals("")) {
                mDatabaseRef.orderByChild("huntName")
                        .startAt(searchString.charAt(0))
                        .endAt(searchString.charAt(0)+getString(R.string.escapeChar))
                        .addListenerForSingleValueEvent(initialLoadHuntListener);

            }
            else{
                if (maxRadius > 0) {
                    mDatabaseRef.orderByChild("maxRadius")
                            .startAt(startRadius)
                            .endAt(maxRadius)
//                            .limitToFirst(sizeOfEachRetrieval)
                            .addListenerForSingleValueEvent(initialLoadHuntListener);
                }
                else{
                    mDatabaseRef
                            .orderByChild("privateHunt")
                            .equalTo(showPrivateHunts)
//                            .limitToFirst(sizeOfEachRetrieval)
                            .addListenerForSingleValueEvent(initialLoadHuntListener);
                }
            }
        }
    }

    private void updateUI(int numberOfItemsRetrieved){
        Log.d(LOG_TAG, "updateUI Called");
        mDatabaseRef.removeEventListener(initialLoadHuntListener);
        if(numberOfItemsRetrieved == 0){
            Snackbar.make(getView(), "No items with the current query criteria", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
//        mDatabaseRef.removeEventListener(paginatedHuntLoadListener);
        mIsLoading = false;
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.allHuntsListRecyclerView);
        if(recyclerView != null){
            MyAllHuntsFeedRecyclerViewAdapter allHuntsRecyclerViewAdapter = (MyAllHuntsFeedRecyclerViewAdapter) recyclerView.getAdapter();
            allHuntsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private ValueEventListener initialLoadHuntListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d(LOG_TAG, "initialLoadHuntListener: onDataChange() called");
            int numberOfItemsRetrieved = 0;
            for(DataSnapshot searchableHuntSnapshot : dataSnapshot.getChildren()){
                SearchableHunt searchableHunt = searchableHuntSnapshot.getValue(SearchableHunt.class);
                if(searchString != null && !searchString.equals("")){
                    if(!searchableHunt.getHuntName().contains(searchString)){
                        continue;
                    }
                }
                if(maxRadius > 0 && searchableHunt.getMaxRadius() > maxRadius){
                    continue;
                }
                if((showPrivateHunts && !searchableHunt.isPrivateHunt()) ||
                        (!showPrivateHunts && searchableHunt.isPrivateHunt())){
                    continue;
                }
                if(!allHuntsList.contains(searchableHunt)){
                    numberOfItemsRetrieved++;
                    allHuntsList.add(searchableHunt);
                    mLastKeyRetrieved = searchableHuntSnapshot.getKey();
                }

            }
//            if(numberOfItemsRetrieved == 0){
//                Snackbar.make(getView(), "No items with the current query criteria", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//            else if(numberOfItemsRetrieved < sizeOfEachRetrieval){
//                numberOfItemsPreviouslyLoaded = numberOfItemsRetrieved;
//                loadMoreHunts();
//            }
//            else{
//                numberOfItemsPreviouslyLoaded = sizeOfEachRetrieval;
//            }
            updateUI(numberOfItemsRetrieved);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


//    private ValueEventListener paginatedHuntLoadListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            Log.d(LOG_TAG, "paginatedLoadHuntListener: onDataChange() called");
//            for(DataSnapshot searchableHuntSnapshot : dataSnapshot.getChildren()) {
//                if (!searchableHuntSnapshot.getKey().equals(mLastKeyRetrieved)) {
//                    SearchableHunt searchableHunt= searchableHuntSnapshot.getValue(SearchableHunt.class);
//                    if (!allHuntsList.contains(searchableHunt)) {
//                        allHuntsList.add(searchableHunt);
//                        mLastKeyRetrieved = searchableHuntSnapshot.getKey();
//                    }
//                }
//            }
//            updateUI();
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };

    private void performSearch() {
        Log.d(LOG_TAG, "performSearch() called");
        allHuntsList.clear();
        mLastKeyRetrieved = null;
        loadPreferences();
        loadInitially();
    }

    private void loadPreferences(){
        Log.d(LOG_TAG, "loadPreferences() called");
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        showPrivateHunts = sharedPrefs.getBoolean(getString(R.string.showPrivateHunts), false);
        String maxRadiusString = sharedPrefs.getString(getString(R.string.maxRadius), "-1.0");
        maxRadius = Double.parseDouble(maxRadiusString != "" ? maxRadiusString : "-1.0");
        startRadius = 0.0;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState()");
        outState.putParcelableArrayList("all_Hunts", allHuntsList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated()");
        if(savedInstanceState != null && savedInstanceState.get("all_Hunts") != null){
            ArrayList<SearchableHunt> list = savedInstanceState.getParcelableArrayList("all_Hunts");
            allHuntsList.addAll(list);
            updateUI(1);
        }
        else{
            loadInitially();
        }
    }

}
