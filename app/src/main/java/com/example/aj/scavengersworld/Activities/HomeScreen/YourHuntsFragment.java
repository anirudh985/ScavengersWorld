package com.example.aj.scavengersworld.Activities.HomeScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.aj.scavengersworld.Activities.HuntsFeed.HuntFeedActivity;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;
import com.example.aj.scavengersworld.UserSessionManager;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class YourHuntsFragment extends Fragment {

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private UserSessionManager session = UserSessionManager.INSTANCE;
    private List<Hunt> yourHuntsList = session.getParticipatingHuntsList();
    public final String LOG_TAG = getClass().getSimpleName();

    public YourHuntsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yourhunts_list, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.hunt_join_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent huntsFeedActivity = new Intent(getActivity(), HuntFeedActivity.class);
                getActivity().startActivity(huntsFeedActivity);
            }
        });

        // Set the adapter
        if (view instanceof CoordinatorLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.yourHuntsListRecyclerView);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyYourHuntsRecyclerViewAdapter(yourHuntsList, mListener));
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
        void onListYourHuntsFragmentInteraction(Hunt hunt);
    }

//    private List<Hunt> getHuntsList(){
//        List<Hunt> huntsList = new ArrayList<>();
//
//        Hunt hunt1 = new Hunt();
//        hunt1.setHuntId(1);
//        hunt1.setHuntName("Hunt1");
//        hunt1.setCreatedByUserId("anirudh985");
//        huntsList.add(hunt1);
//
//        Hunt hunt2 = new Hunt();
//        hunt2.setHuntId(2);
//        hunt2.setHuntName("Hunt2");
//        hunt2.setCreatedByUserId("kavskalyan");
//        huntsList.add(hunt2);
//
//        Hunt hunt3 = new Hunt();
//        hunt3.setHuntId(3);
//        hunt3.setHuntName("Hunt3");
//        hunt3.setCreatedByUserId("anirudh985");
//        huntsList.add(hunt3);
//
//        Hunt hunt4 = new Hunt();
//        hunt4.setHuntId(4);
//        hunt4.setHuntName("Hunt4");
//        hunt4.setCreatedByUserId("kavskalyan");
//        huntsList.add(hunt4);
//
//
//
//        return huntsList;
//    }

    public void updateUI(){
        Log.d(LOG_TAG, "updateUI Called");
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.yourHuntsListRecyclerView);
        if(recyclerView != null){
            MyYourHuntsRecyclerViewAdapter yourHuntsRecyclerViewAdapter = (MyYourHuntsRecyclerViewAdapter) recyclerView.getAdapter();
            yourHuntsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
