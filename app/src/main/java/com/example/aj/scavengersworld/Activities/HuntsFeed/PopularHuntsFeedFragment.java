package com.example.aj.scavengersworld.Activities.HuntsFeed;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aj.scavengersworld.DatabaseModels.SearchableHunt;
import com.example.aj.scavengersworld.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<SearchableHunt> popularHuntsList = new ArrayList<>();
    private final String LOG_TAG = getClass().getSimpleName();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;

    private boolean mIsLoading = false;
    private boolean mIsLastPage = false;
    private int mSizeOfItemsAlreadyLoadedAfterLastVisibleItem = 5;

    private String mLastKeyRetrieved;
    private int sizeOfEachRetrieval = 20;

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
        mDatabaseRef = mDatabase.getReference(getString(R.string.searchableHuntsTable));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView Called");
        View view = inflater.inflate(R.layout.fragment_popularhuntsfeed_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyPopularHuntsRecyclerViewAdapter(popularHuntsList, mListener));

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
////                                loadMoreHunts();
//                            }
//                        }
//                    }
//                }
//            });
            loadInitially();
        }


        return view;
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
        void onListPopularHuntsFragmentInteraction(SearchableHunt popularHunt);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
    }

    public void loadInitially(){
        mDatabaseRef.orderByChild("numberOfPlayers")
                .addListenerForSingleValueEvent(initialLoadHuntListener);
    }

    private ValueEventListener initialLoadHuntListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d(LOG_TAG, "initialLoadHuntListener: onDataChange() called");
            int numberOfItemsRetrieved = 0;
            for(DataSnapshot searchableHuntSnapshot : dataSnapshot.getChildren()){
                SearchableHunt searchableHunt = searchableHuntSnapshot.getValue(SearchableHunt.class);
                if(!popularHuntsList.contains(searchableHunt)){
                    numberOfItemsRetrieved++;
                    popularHuntsList.add(searchableHunt);
                    mLastKeyRetrieved = searchableHuntSnapshot.getKey();
                }
            }
            Collections.reverse(popularHuntsList);
            updateUI(numberOfItemsRetrieved);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void updateUI(int numberOfItemsRetrieved){
        Log.d(LOG_TAG, "updateUI Called");
        mDatabaseRef.removeEventListener(initialLoadHuntListener);
        if(numberOfItemsRetrieved == 0){
            Snackbar.make(getView(), "No Popular Hunts currently", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
//        mDatabaseRef.removeEventListener(paginatedHuntLoadListener);
        mIsLoading = false;
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.popularHuntsFeedListRecyclerView);
        if(recyclerView != null){
            MyPopularHuntsRecyclerViewAdapter popularHuntsRecyclerViewAdapter = (MyPopularHuntsRecyclerViewAdapter) recyclerView.getAdapter();
            popularHuntsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

}
