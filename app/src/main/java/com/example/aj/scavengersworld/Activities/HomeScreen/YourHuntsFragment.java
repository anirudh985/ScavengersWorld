package com.example.aj.scavengersworld.Activities.HomeScreen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class YourHuntsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    //TODO: Need to get huntslist from another class. Don't make a call to retrieve them here.
    private List<Hunt> yourHuntsList;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public YourHuntsFragment() {
    }

//    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static YourHuntsFragment newInstance(int columnCount) {
//        YourHuntsFragment fragment = new YourHuntsFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yourhunts_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //TODO: Need to get huntslist from another class. Don't make a call to retrieve them here.
            yourHuntsList = getHuntsList();
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
        void onListFragmentInteraction(Hunt hunt);
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
}