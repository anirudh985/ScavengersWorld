package com.example.aj.scavengersworld.Activities.Login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aj.scavengersworld.Model.User;
import com.example.aj.scavengersworld.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link SignUpFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link SignUpFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class SignUpFragment extends Fragment implements View.OnClickListener{
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

    private TextView mTextView;

    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallbackResult = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayWelcomeMessage(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    private AccessTokenTracker mAccessTokenTracker;
    private ProfileTracker mProfileTracker;

    private void displayWelcomeMessage(Profile profile){
        if(profile != null){
            mTextView.setText("Welcome "+profile.getName());
        }
    }



    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mSignUpButton;

    private final String LOG_TAG = getClass().getSimpleName();


    public SignUpFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SignUpFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SignUpFragment newInstance(String param1, String param2) {
//        SignUpFragment fragment = new SignUpFragment();
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
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                displayWelcomeMessage(currentProfile);
            }
        };


//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }

    private void bindUItoVariables(View view){
        Log.d(LOG_TAG, "bindUItoVariables()");
        if(view != null) {
            mUsername = (EditText) view.findViewById(R.id.signupUsername);
            mEmail = (EditText) view.findViewById(R.id.signupEmail);
            mPassword = (EditText) view.findViewById(R.id.signupPassword);
            mConfirmPassword = (EditText) view.findViewById(R.id.signupConfirmPassword);
            mSignUpButton = (Button) view.findViewById(R.id.btnSignUp);
            mSignUpButton.setOnClickListener(this);
        }
    }

    private void restoreUIVariables(Bundle savedInstanceState){
        Log.d(LOG_TAG, "restoreUIVariables()");
        if(savedInstanceState != null){
            mUsername.setText(savedInstanceState.getString(getString(R.string.username), ""));
            mEmail.setText(savedInstanceState.getString(getString(R.string.email), ""));
            mPassword.setText(savedInstanceState.getString(getString(R.string.password), ""));
            mConfirmPassword.setText(savedInstanceState.getString(getString(R.string.confirmPassword), ""));
        }
    }

    private Bundle saveUIVariables(Bundle outState){
        Log.d(LOG_TAG, "saveUIVariables()");
        if(outState != null){
            outState.putString(getString(R.string.username), mUsername.getText().toString());
            outState.putString(getString(R.string.email), mEmail.getText().toString());
            outState.putString(getString(R.string.password), mPassword.getText().toString());
            outState.putString(getString(R.string.confirmPassword), mConfirmPassword.getText().toString());
        }

        return outState;
    }

    @Override
    public void onClick(View view) {
        Log.d(LOG_TAG, "onClick()");
        switch (view.getId()){
            case R.id.btnSignUp:
                User user = validateInput();
                registerUser(user);
                break;
            default:
                break;
        }
    }

    private User validateInput(){
        Log.d(LOG_TAG, "validateInput()");

        return null;
    }

    private void registerUser(@NonNull User user){
        Log.d(LOG_TAG, "registerUser()");
        // TODO: network call to register user in our database

        //TODO: validate login and call homescreen intent
//            Intent homeScreen = new Intent(this, HomeScreenAcitivity.class);
//            homeScreen.putExtra(R.string.USER, user);
//            startActivity(homeScreen);
        //TODO: need to save session
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(LOG_TAG, "onViewCreated()");
        bindUItoVariables(view);
        LoginButton fbLoginButton = (LoginButton) view.findViewById(R.id.facebook_login_button);
//        fbLoginButton.setReadPermissions("user_friends");
        fbLoginButton.setFragment(this);
        fbLoginButton.registerCallback(mCallbackManager, mCallbackResult);

        mTextView = (TextView) view.findViewById(R.id.welcomeText);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "onActivityResult()");
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
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
}
