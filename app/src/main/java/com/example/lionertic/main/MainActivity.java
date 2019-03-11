package com.example.lionertic.main;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.lionertic.main.AsyncTask.KeyCheck;
import com.example.lionertic.main.Fragments.LogIn;
import com.example.lionertic.main.Fragments.SplashScreen;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "E";
    public static String KEY = "";
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static Boolean mRequestingLocationUpdates = false;
    public static Activity activity;
    public static Context context;
    public static ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        context = getApplicationContext();
        progressDialog = new ProgressDialog(this);
        Log.e("asdfghjkl", "123456789");

        SharedPreferences preferences = getSharedPreferences("KEY", MODE_PRIVATE);
        KEY = preferences.getString("KEY", "");

//        if (isNetworkConnected()) {
//            isLocationEnabled();
//            startLocation(0);
//            if (KEY.equals("")) {
                //            startLocation(0);
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().add(R.id.fragment, new SplashScreen()).commit();
//            } else {
//                Log.e("asfafsfasaas", "not granted");
//                startLocation(1);
//            }
//        } else
//            Toast.makeText(getApplicationContext(), "NO network..Enable and RESTART APP", Toast.LENGTH_LONG).show();
    }

//    private void openSettings() {
//        Intent intent = new Intent();
//        intent.setAction(
//                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package",
//                BuildConfig.APPLICATION_ID, null);
//        intent.setData(uri);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


//    public void startLocation(final int i) {
//        // Requesting ACCESS_FINE_LOCATION using Dexter library
//
//        Dexter.withActivity(activity)
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        if (i == 1) {
//                            progressDialog.show();
//                            progressDialog.setMessage("Logging In...");
//                            progressDialog.setCancelable(false);
//                            new KeyCheck(context, activity).execute();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        if (response.isPermanentlyDenied()) {
//                            // open device settings when the permission is
//                            // denied permanently
//
//                            openSettings();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
//
//                    }
//                }).check();
//    }

    @Override
    public void onResume() {
        super.onResume();
//        if (checkPermissions()) {
//            mRequestingLocationUpdates = true;
//        }
        // Resuming location updates depending on button state and
        // allowed permissions

    }


//    private boolean checkPermissions() {
//        int permissionState = ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        return permissionState == PackageManager.PERMISSION_GRANTED;
//    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

//    public void isLocationEnabled() {
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        boolean gps_enabled = false, network_enabled = false;
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch (Exception ex) {
//        }
//        try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch (Exception ex) {
//        }
//        if (!gps_enabled && !network_enabled) {
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setMessage("Enable Location");
//            dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(myIntent);
//                }
//            });
//            dialog.show();
//        }
//    }
}