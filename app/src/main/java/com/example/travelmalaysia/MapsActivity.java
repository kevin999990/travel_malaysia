package com.example.travelmalaysia;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelmalaysia.model.MyPlace;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMyLocationChangeListener {

    private static final String TAG = "MapsActivity";
    //Permission request code
    //can be any number
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    //Permission Strings
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private static final String MALAYSIA_CODE = "MY";
    private static final float DEFAULT_ZOOM = 15;
    //Default Location point to center of EM and WM
    private final LatLng mDefaultLocation = new LatLng(3.867, 109.463);


    ArrayList<MyPlace> myPlaceArrayList;

    //variables
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleApiClient mGoogleApiClient;
    /*private GeoDataClient mGeoDataClient;*/
    Marker mMarker;
    /*private LocationRequest mLocationRequest;*/
    private Location mCurrentLocation;
    private PlaceAutocompleteFragment placeAutocompleteFragment;

    //connect to mysql
    private String url;
    private RequestQueue requestQueue;
    private JsonObjectRequest request;

    //widget
    /*private AutoCompleteTextView mSearchText;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        url = "http://kvintech.esy.es/travelmalaysia/location_control.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        myPlaceArrayList = new ArrayList<>();

        //Check is all permission are granted
        //if true, then initialize the map
        getLocationPermission();

        getLocationListFromServer();
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

        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        // Add a marker in KLCC and move the camera
        //  mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title("Marker in KLCC Park"));
        moveCamera(mDefaultLocation, 4.6f);
    }

    private void init() {
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .build();
        mGoogleApiClient.connect();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mCurrentLocation = mFusedLocationProviderClient.getLastLocation().getResult();
        if (mCurrentLocation != null) {
            final double latitude = mCurrentLocation.getLatitude();
            final double longitude = mCurrentLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            //show marker
            mMap.addMarker(new MarkerOptions().position(latLng));
            moveCamera(latLng, DEFAULT_ZOOM);
        }
    }

    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
            return false;
        } else {
            return true;
        }
    }


    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location Permission");
        String[] permission = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        //check permission for FINE_LOCATION
        if (ContextCompat.checkSelfPermission(
                this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //check permission fo COARSE_LOCATION
            if ((ContextCompat.checkSelfPermission(
                    this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                Log.d(TAG, "getLocationPermission: Grants All Permission Needed");

                //set the global variable to true if all permission is granted
                mLocationPermissionsGranted = true;

                //initialize the map is all permission granted
                initMap();

            } else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap: Initializing Map");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Method Called");

        //set the variable to false
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    //if one of the permission are not granted, return.
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission Denied!");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                    mLocationPermissionsGranted = true;
                    //initialize Map
                    initMap();
                }
            }
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving camera to lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        /*//Direct Blink
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));*/
        //Animated
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving camera to lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));*/
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(markerOptions);
    }

/*    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }*/

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        init();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        init();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getLocationListFromServer() {
        requestQueue = Volley.newRequestQueue(MapsActivity.this);

        JSONObject jsonObject = new JSONObject();

        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Log.d(TAG, "onResponse: " +response);
                    JSONArray array = response.getJSONArray("allPlace");

                    for (int i = 0; i < array.length(); i++) {
                        JSONArray array2 = array.getJSONArray(i);
                        MyPlace myPlace = new MyPlace(
                                array2.getInt(0),
                                array2.getString(1),
                                array2.getString(2),
                                array2.getDouble(3),
                                array2.getDouble(4),
                                array2.getInt(5));

                        myPlaceArrayList.add(myPlace);

                        MarkerOptions options = new MarkerOptions().position(myPlace.getLatLng());
                        mMap.addMarker(options).setTitle(myPlace.getName());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, "Something Error at getAllLocation()", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                Log.e(TAG, "onErrorResponse: " + error);
                error.printStackTrace();
            }
        };
        request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, jsonObjectListener, errorListener);
        requestQueue.add(request);
    }

    @Override
    public void onMyLocationChange(Location location) {
        Location target = new Location("Target");
        for (int i = 0; i < myPlaceArrayList.size(); i++) {

            target.setLatitude(myPlaceArrayList.get(i).getLat());
            target.setLongitude(myPlaceArrayList.get(i).getLng());
            Log.d(TAG, "onMyLocationChange: " + myPlaceArrayList.get(i).getName() + "  " + location.distanceTo(target));
            if (location.distanceTo(target) < 100) {
                Log.d(TAG, "onMyLocationChange: " + myPlaceArrayList.get(i).getName() + "  " + location.distanceTo(target));
                Toast.makeText(this, "Your are near target " + myPlaceArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
