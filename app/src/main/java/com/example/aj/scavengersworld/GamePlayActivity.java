package com.example.aj.scavengersworld;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.location.Location;

import com.example.aj.scavengersworld.CluesRelated.CurrentClueActivity;
import com.example.aj.scavengersworld.Model.Clue;
import com.example.aj.scavengersworld.Model.Hunt;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.aj.scavengersworld.PermissionUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class GamePlayActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {

    private GoogleMap mMap;
    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_LAYER_PERMISSION_REQUEST_CODE = 2;
    //private LatLng mCurrentLocation;
    private Location mCurrentLocation;
    private Button mConfirmLocationButton;
    private boolean mLocationPermissionDenied = false;
    private final String TAG = ((Object) this).getClass().getSimpleName();
    private SharedPreferences mSettings;
    private GoogleApiClient mGoogleApiClient;
    private Button mShowCluesButton;
    private Button mSolveClueButton;
    private UiSettings mUiSettings;
    private UserSessionManager session = UserSessionManager.INSTANCE;
    //private List<Hunt> yourHuntsList = session.getParticipatingHuntsList();
    private final String LOG_TAG = getClass().getSimpleName();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;
    private int numberOfEventListeners = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mSolveClueButton = (Button)findViewById(R.id.button6);
        mSolveClueButton.setOnClickListener(this);
        //mShowCluesButton = (Button)findViewById(R.id.button7);
        buildGoogleApiClient();
        getCurrentCluesAndSaveInSession();
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //createLocationRequest();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mUiSettings = mMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        enableCurrentLocation();
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
    }

    protected void onResume() {
        super.onResume();
        mSettings = getSharedPreferences(getString(R.string.prefs), 0);
        MapUtils.setUpEula(this,mSettings);
    }


    /**
     * Checks if the map is ready (which depends on whether the Google Play services APK is
     * available. This should be called prior to calling any methods on GoogleMap.
     */
    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void enableCurrentLocation() {
        if (!checkReady()) {
            return;
        }
        // Enables/disables the my location layer (i.e., the dot/chevron on the map). If enabled, it
        // will also cause the my location button to show (if it is enabled); if disabled, the my
        // location button will never show.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(true);
        } else {
            // Uncheck the box and request missing location permission.
            // mMyLocationLayerCheckbox.setChecked(false);
            PermissionUtils.requestPermission(this, LOCATION_LAYER_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == MY_LOCATION_PERMISSION_REQUEST_CODE) {
            // Enable the My Location button if the permission has been granted.
            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                mUiSettings.setMyLocationButtonEnabled(true);
            } else {
                mLocationPermissionDenied = true;
            }

        } else if (requestCode == LOCATION_LAYER_PERMISSION_REQUEST_CODE) {
            // Enable the My Location layer if the permission has been granted.
            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                mLocationPermissionDenied = true;
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()),4.0));
            //Location	myLocation	=	mMap.getMyLocation();
            LatLng myLatLng	=	new	LatLng(mCurrentLocation.getLatitude(),	mCurrentLocation.getLongitude());
            CameraPosition myPosition	=	new	CameraPosition.Builder()
                    .target(myLatLng).zoom(4).bearing(0).tilt(0).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));

            //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            // updateUI();
        }
    }

    private Location GetCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG,"No permission in get cur location");
            return mCurrentLocation;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return mCurrentLocation;
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }
    private void AddMarkersForLastModifiedClues(){

    }
    public void AddMarkerAtLocation(LatLng latLng, String markerTitle){
        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(markerTitle);

        // Clears the previously touched position
       // mMap.clear();

        // Animating to the touched position
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        mMap.addMarker(markerOptions);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button6:
                Location currentLocation = GetCurrentLocation();
                Intent intent=new Intent(GamePlayActivity.this,CurrentClueActivity.class);
                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();
                intent.putExtra("LATITUDE",latitude);
                intent.putExtra("LONGITUDE",longitude);
                String mode = "CURRENTCLUES";
                intent.putExtra("MODE",mode);
                startActivity(intent);
                //finish();
        }
    }
    private void getCurrentCluesAndSaveInSession(){
        List<Hunt> participatingHuntsList = session.getParticipatingHuntsList();
        for(Hunt curHunt : participatingHuntsList){
            numberOfEventListeners+=1;
            mDatabaseRef = mDatabase.getReference(getString(R.string.huntsToClues) + "/" +curHunt.getHuntName());
            mDatabaseRef.addListenerForSingleValueEvent(huntsToCluesListener);
        }
    }
    ValueEventListener huntsToCluesListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot userToHuntsSnapshot : dataSnapshot.getChildren()){
                Hunt currentHunt = session.getParticipatingHuntByName(dataSnapshot.getKey());
                Clue newClue = userToHuntsSnapshot.getValue(Clue.class);
                newClue.setHuntName(currentHunt.getHuntName());
                currentHunt.addClueToClueList(newClue);
                int a  = 1;
            }
            numberOfEventListeners-=1;
            if(numberOfEventListeners == 0)
                updateUI();
        }
        private void updateUI(){
            for(Hunt hunt: session.getParticipatingHuntsList()){
                if(hunt.getClueList() == null) continue;
                if(hunt.getClueList().size() == 0) continue;
                int currentClueSequence = hunt.getCurrentClue().getSequenceNumberInHunt();
                if(currentClueSequence == 1) continue;
                Clue previousClue = hunt.getClueAtSequence(currentClueSequence -1 );
                if(previousClue == null) continue;
                LatLng previousClueLocation	=	new	LatLng(previousClue.getLocation().getLatitude(),previousClue.getLocation().getLongitude());
                AddMarkerAtLocation(previousClueLocation, previousClue.getLandmarkDescription());
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(LOG_TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };
}
