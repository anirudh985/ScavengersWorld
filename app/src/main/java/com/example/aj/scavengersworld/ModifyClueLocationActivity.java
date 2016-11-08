package com.example.aj.scavengersworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.aj.scavengersworld.PermissionUtils;


public class ModifyClueLocationActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener,View.OnClickListener {

    private GoogleMap mMap;
    private final String TAG = ((Object) this).getClass().getSimpleName();
    private SharedPreferences mSettings;

    private UiSettings mUiSettings;

    private SupportMapFragment mMapFragment;
    private static final LatLng DEFAULT_MARKER_POSITION = new LatLng(48.858179, 2.294576);
    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_LAYER_PERMISSION_REQUEST_CODE = 2;
    private static final String MARKER_POSITION = "markerPosition";
    private LatLng mCurrentSelectedLocation;
    private Button mConfirmLocationButton;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mLocationPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_clue_location);
        mConfirmLocationButton = (Button) findViewById(R.id.button5);
        mConfirmLocationButton.setOnClickListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        if (savedInstanceState == null) {
//            // Activity created for the first time.
            Bundle dataFromIntent  = getIntent().getExtras();
            double lat = dataFromIntent.getDouble("LATITUDE",-1);
            double longi = dataFromIntent.getDouble("LONGITUDE",-1);
            LatLng oldLocation = new LatLng(lat,longi);
            mCurrentSelectedLocation = oldLocation;
        }else {
//            // Extract the state of the MapFragment:
//            // - Objects from the API (eg. LatLng, MarkerOptions, etc.) were stored directly in
//            //   the savedInsanceState Bundle.
//            // - Custom Parcelable objects were wrapped in another Bundle.
//
            mCurrentSelectedLocation = savedInstanceState.getParcelable(MARKER_POSITION);

        }
    }

    protected void onResume() {
        super.onResume();
        mSettings = getSharedPreferences(getString(R.string.prefs), 0);
        MapUtils.setUpEula(this,mSettings);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mMapFragment.onSaveInstanceState(outState);
        outState.putParcelable(MARKER_POSITION, mCurrentSelectedLocation);
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
        mMap.setOnMapClickListener(this);
        mUiSettings = mMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        enableCurrentLocation();
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);

        AddMarkerAtLocation(mCurrentSelectedLocation);


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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(true);
        } else {
            // Uncheck the box and request missing location permission.
           // mMyLocationLayerCheckbox.setChecked(false);
            PermissionUtils.requestPermission(this, LOCATION_LAYER_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == MY_LOCATION_PERMISSION_REQUEST_CODE) {
            // Enable the My Location button if the permission has been granted.
            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                mUiSettings.setMyLocationButtonEnabled(true);
            } else {
                mLocationPermissionDenied = true;
            }

        } else if (requestCode == LOCATION_LAYER_PERMISSION_REQUEST_CODE) {
            // Enable the My Location layer if the permission has been granted.
            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
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
                mMap.setMyLocationEnabled(true);
            } else {
                mLocationPermissionDenied = true;
            }
        }
    }

    public void AddMarkerAtLocation(LatLng latLng){
        // Creating a marker
        mCurrentSelectedLocation = latLng;
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

        // Clears the previously touched position
        mMap.clear();

        // Animating to the touched position
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        mMap.addMarker(markerOptions);
    }
    @Override
    public void onMapClick(LatLng latLng) {
        AddMarkerAtLocation(latLng);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button5)
        {
            Intent intent=new Intent();
            double latitude = mCurrentSelectedLocation.latitude;
            double longitude = mCurrentSelectedLocation.longitude;
            intent.putExtra("LATITUDE",latitude);
            intent.putExtra("LONGITUDE",longitude);
            setResult(RESULT_OK,intent);
            finish();//finishing activity
        }
    }
}
