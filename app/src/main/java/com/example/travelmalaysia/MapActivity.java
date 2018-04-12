package com.example.travelmalaysia;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String MALAYSIA_CODE = "MY";
    private static final float DEFAULT_ZOOM = 15;

    //Default Location point to center of EM and WM
    private final LatLng mDefaultLocation = new LatLng(3.867, 109.463);

    //variables
    private ArrayList<MyPlace> myPlaceArrayList;
    private Boolean isLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private Location mCurrentLocation;
    //Location API
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //connect to mysql
    private String url;
    private RequestQueue requestQueue;
    private JsonObjectRequest request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initializeVariables();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        checkLocationPermission();
        if (isLocationPermissionsGranted)
            mMap.setMyLocationEnabled(true);

        moveCamera(mDefaultLocation, DEFAULT_ZOOM);
    }

    private void initializeVariables() {
        //For MySqlServer
        url = getString(R.string.url_location_control);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        myPlaceArrayList = new ArrayList<>();

        checkLocationPermission();
        if (isLocationPermissionsGranted) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationListFromServer();
    }

    private void checkLocationPermission() {
        Log.d(TAG, "getLocationPermission: Check is Permission Granted");

        String[] permission = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        //check permission for FINE_LOCATION
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //check permission fo COARSE_LOCATION
            if ((ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                Log.d(TAG, "getLocationPermission: All Permission Granted");

                //set the global variable to true if all permission is granted
                isLocationPermissionsGranted = true;

                //initialize the map is all permission granted

            } else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Method Called");

        isLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    //if one of the permission are not granted, return.
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            isLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission Denied!");
                            //todo:: back to home page
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                    isLocationPermissionsGranted = true;
                    //initialize Map
                    //initMap();
                }
            }
        }
    }

    private void getLocationListFromServer() {
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

//                        MarkerOptions options = new MarkerOptions().position(myPlace.getLatLng());
//                        mMap.addMarker(options).setTitle(myPlace.getName());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something Error at getAllLocation()", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                Log.e(TAG, "onErrorResponse: " + error);
                error.printStackTrace();
            }
        };
        request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, jsonObjectListener, errorListener);
        requestQueue.add(request);
    }


    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving camera to lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        //Animated
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving camera to lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(markerOptions);
    }


    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
