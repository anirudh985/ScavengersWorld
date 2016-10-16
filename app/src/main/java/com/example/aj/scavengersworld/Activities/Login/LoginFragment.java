package com.example.aj.scavengersworld.Activities.Login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.aj.scavengersworld.Activities.HomeScreen.HomeScreenActivity;
import com.example.aj.scavengersworld.Model.Hunt;
import com.example.aj.scavengersworld.Model.User;
import com.example.aj.scavengersworld.R;

import java.util.ArrayList;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link LoginFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link LoginFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class LoginFragment extends Fragment implements View.OnClickListener{
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;

    private final String LOG_TAG = getClass().getSimpleName();

    private Button mLoginButton;
    private EditText mUsername;
    private EditText mPassword;

    public LoginFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment LoginFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static LoginFragment newInstance(String param1, String param2) {
//        LoginFragment fragment = new LoginFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate()");


//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        bindUItoVariables(view);

        return view;
    }

    private void bindUItoVariables(View view){
        Log.d(LOG_TAG, "bindUItoVariables()");
        if(view != null) {
            mLoginButton = (Button) view.findViewById(R.id.btnLogin);
            mLoginButton.setOnClickListener(this);

            mUsername = (EditText) view.findViewById(R.id.loginUsername);
            mPassword = (EditText) view.findViewById(R.id.loginPassword);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated()");
        restoreUIVariables(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState()");
        saveUIVariables(outState);
    }

    private void restoreUIVariables(Bundle savedInstanceState){
        Log.d(LOG_TAG, "restoreUIVariables()");
        if(savedInstanceState != null){
            mUsername.setText(savedInstanceState.getString(getString(R.string.username), ""));
            mPassword.setText(savedInstanceState.getString(getString(R.string.password), ""));
        }
    }

    private Bundle saveUIVariables(Bundle outState){
        Log.d(LOG_TAG, "saveUIVariables()");
        if(outState != null){
            outState.putString(getString(R.string.username), mUsername.getText().toString());
            outState.putString(getString(R.string.password), mPassword.getText().toString());
        }
        return outState;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                performLogin();
                break;
            default:
                break;
        }

    }

    private void performLogin(){
        Log.d(LOG_TAG, "performLogin()");
        if(mUsername.getText() != null && mPassword.getText() != null &&
                mUsername.getText().toString() != null && mUsername.getText().toString() != "" &&
                mPassword.getText().toString() != null && mPassword.getText().toString() != ""){
            //TODO: validate login and call homescreen intent
            //if(validateLogin())
            Intent homeScreen = new Intent(getActivity(), HomeScreenActivity.class);
            homeScreen.putExtra(getString(R.string.USER), createUser());
            startActivity(homeScreen);
            //TODO: need to save session
        }
    }

    //    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    private User createUser(){


        List<Hunt> mCreatedHuntList = new ArrayList<>();
        List<Hunt> mRegisteredHuntList = new ArrayList<>();

        User user = new User();
        user.setUserId("abcd");
        user.setUserEmail("qwerty@xyz.com");
        user.setDisplayName("$$ABCD$$");

//        Hunt h = new Hunt();
//        h.
//
//        createdHuntList.add(new Hunt());

        return user;
    }
}
