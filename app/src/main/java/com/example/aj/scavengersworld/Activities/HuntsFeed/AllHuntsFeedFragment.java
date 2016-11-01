package com.example.aj.scavengersworld.Activities.HuntsFeed;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aj.scavengersworld.Activities.HomeScreen.MyCreatedHuntsRecyclerViewAdapter;
import com.example.aj.scavengersworld.Activities.HomeScreen.MyYourHuntsRecyclerViewAdapter;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
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
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AllHuntsFeedFragment extends Fragment {

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Hunt> allHuntsList;
    public static final String LOG_TAG = "AllHuntsFeed";

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
    public AllHuntsFeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseRef = mDatabase.getReference(getString(R.string.allHuntsTableRef));
        loadInitially();
//        allHuntsList = getHuntsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allhuntsfeed_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyAllHuntsFeedRecyclerViewAdapter(allHuntsList, mListener));

            // Pagination
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if(dy > 0) //check for scroll down
                    {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                        //TODO: need a way to find out whether we reached last page or not
                        if (!mIsLoading && !mIsLastPage)
                        {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount - mSizeOfItemsAlreadyLoadedAfterLastVisibleItem)
                            {
                                mIsLoading = true;
                                Log.d(LOG_TAG, "... Last Item Wow !");
                                //Do pagination.. i.e. fetch new data
                                loadMoreHunts();
                            }
                        }
                    }
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
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
        super.onDetach();
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
        void onListAllHuntsFeedFragmentInteraction(Hunt hunt);
    }

    private List<Hunt> getHuntsList(){
        List<Hunt> huntsList = new ArrayList<>();

        Hunt hunt1 = new Hunt();
        hunt1.setHuntId(1);
        hunt1.setHuntName("Hunt1");
        hunt1.setCreatedByUserId("anirudh985");
        huntsList.add(hunt1);

        Hunt hunt2 = new Hunt();
        hunt2.setHuntId(2);
        hunt2.setHuntName("Hunt2");
        hunt2.setCreatedByUserId("kavskalyan");
        huntsList.add(hunt2);

        Hunt hunt3 = new Hunt();
        hunt3.setHuntId(3);
        hunt3.setHuntName("Hunt3");
        hunt3.setCreatedByUserId("anirudh985");
        huntsList.add(hunt3);

        Hunt hunt4 = new Hunt();
        hunt4.setHuntId(4);
        hunt4.setHuntName("Hunt4");
        hunt4.setCreatedByUserId("kavskalyan");
        huntsList.add(hunt4);

        return huntsList;
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

    private void loadMoreHunts(){
        if(mLastKeyRetrieved != null){
            mDatabaseRef.orderByKey()
                        .startAt(mLastKeyRetrieved)
                        .limitToFirst(sizeOfEachRetrieval)
                        .addListenerForSingleValueEvent(paginatedHuntLoadListener);
        }

    }

    private void loadInitially(){
        mDatabaseRef.orderByKey()
                    .limitToFirst(sizeOfEachRetrieval)
                    .addListenerForSingleValueEvent(initialLoadHuntListener);
    }

    private void updateUI(){
        Log.d(LOG_TAG, "updateUI Called");
        mDatabaseRef.removeEventListener(initialLoadHuntListener);
        mDatabaseRef.removeEventListener(paginatedHuntLoadListener);
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
            for(DataSnapshot huntSnapshot : dataSnapshot.getChildren()){
                Hunt hunt = huntSnapshot.getValue(Hunt.class);
                if(!allHuntsList.contains(hunt)){
                    allHuntsList.add(hunt);
                }
            }
            updateUI();
            mIsLoading = false;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    private ValueEventListener paginatedHuntLoadListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d(LOG_TAG, "paginatedLoadHuntListener: onDataChange() called");
            for(DataSnapshot huntSnapshot : dataSnapshot.getChildren()) {
                if (!huntSnapshot.getKey().equals(mLastKeyRetrieved)) {
                    Hunt hunt = huntSnapshot.getValue(Hunt.class);
                    if (!allHuntsList.contains(hunt)) {
                        allHuntsList.add(hunt);
                        mLastKeyRetrieved = huntSnapshot.getKey();
                        updateUI();
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
