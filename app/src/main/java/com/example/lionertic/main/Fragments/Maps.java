package com.example.lionertic.main.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.arsy.maps_library.MapRipple;
import com.example.lionertic.main.AsyncTask.SaveLoca;
import com.example.lionertic.main.BuildConfig;
import com.example.lionertic.main.CONSTANTS;
import com.example.lionertic.main.MainActivity;
import com.example.lionertic.main.R;
import com.example.lionertic.main.RequestHandler;
import com.example.lionertic.main.Service.DriverRouting;
import com.example.lionertic.main.Service.LocationService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Maps extends Fragment implements OnMapReadyCallback {


    View v;

    SupportMapFragment supportMapFragment;
    public static MapRipple mapRipple;
    public static GoogleMap mMap;
    public static final String TAG = "E";
    // Creating JSON Parser object
    private static MarkerOptions markerOptions;
    public static Marker marker;
    private static String RO;
    public static Marker a = null;
    public static Polyline dri[] = new Polyline[2];
    public static String key;
    public static Marker drv[]=new Marker[2];

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    public static Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private Location onChange;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private String mLastUpdateTime;
    int i = 0;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private FloatingActionButton fab,fab1;
    private Button button;

    public static Bitmap dr[] = new Bitmap[2];
    public Maps() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity.progressDialog.dismiss();


        v = inflater.inflate(R.layout.fragment_maps, container, false);



        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Objects.requireNonNull(getActivity()).setTitle("Home");
                        Home_page m = new Home_page();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.fragment, m).commit();
                    }
                }
                return true;
            }
        });

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);

        init();
        if (supportMapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map1, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);
        fab=v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLocation();
                startLocationService();
            }
        });
        button=v.findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getContext().getSharedPreferences("KEY", getContext().MODE_PRIVATE);
                preferences.edit().putString("KEY", "").apply();
                MainActivity.KEY = preferences.getString("KEY", "");
                getActivity().setTitle("Sign In");
                LogIn m = new LogIn();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment, m).commit();
            }
        });
        fab1=v.findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.progressDialog.show();
                MainActivity.progressDialog.setMessage("Finding...");
                MainActivity.progressDialog.setCancelable(false);
                nearDrive();
            }
        });

        return v;
    }

    private void startLocationService(){

        if(!isLocationServiceRunning()){
            Intent serviceIntent = new Intent(getActivity(), LocationService.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                getActivity().startForegroundService(serviceIntent);
            }else{
                getActivity().startService(serviceIntent);
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.example.lionertic.main.Service.LocationService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void nearDrive(){
        mapRipple = new MapRipple(mMap,new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()),MainActivity.context);
        mapRipple.withNumberOfRipples(1);
        mapRipple.withDistance(500);
        mapRipple.withRippleDuration(1000);
        mapRipple.withTransparency(0.5f);

        if(!isDriverRountingRunning()){

            mapRipple.startRippleMapAnimation();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    CONSTANTS.NEAR,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                if(jsonObject.getInt("success")==1) {
                                    key=jsonObject.getString("id");
                                    startDriverRounting();
                                }
                                else{
                                    mapRipple.stopRippleMapAnimation();
                                    Toast.makeText(getContext(),"No Driver Found",Toast.LENGTH_LONG).show();
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.getMessage()+"asdfghjkl", Toast.LENGTH_LONG).show();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    SharedPreferences sd = MainActivity.context.getSharedPreferences("Req", Context.MODE_PRIVATE);
                    params.put("lat", Double.toString(onChange.getLatitude()));
                    params.put("lon", Double.toString(onChange.getLongitude()));
                    params.put("key",MainActivity.KEY);
                    params.put("req",sd.getString("Req",""));
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

        }
        else{
            mapRipple.stopRippleMapAnimation();
        }
    }

    private void startDriverRounting(){

        if(!isDriverRountingRunning()){
            Intent serviceIntent = new Intent(getActivity(), DriverRouting.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                getActivity().startForegroundService(serviceIntent);
            }else{
                getActivity().startService(serviceIntent);
            }
        }
    }
    private boolean isDriverRountingRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.example.lionertic.main.Service.DriverRouting".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                if ((onChange == null) || (onChange.getLatitude() != mCurrentLocation.getLatitude() && onChange.getLongitude() != mCurrentLocation.getLongitude())) {
                    onChange = mCurrentLocation;
                    updateLocationUI(mCurrentLocation,i++);
                }
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void updateLocationUI(Location mCurrent,int check) {
        if (mCurrent != null) {
            LatLng l =new LatLng(mCurrent.getLatitude(),mCurrent.getLongitude());
            marker.setPosition(l);
            if(check==0) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 16));
                nearDrive();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        marker=mMap.addMarker(new MarkerOptions().position(new LatLng(13.065057, 80.2263933)).title("TEST"));

        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(13.065057, 80.2263933)));
        startLocationUpdates();


    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");


                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }


    public void  startLocation() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        updateLocationUI(mCurrentLocation,0);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
