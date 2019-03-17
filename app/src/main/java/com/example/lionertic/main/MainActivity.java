package com.example.lionertic.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.lionertic.main.Fragments.SplashScreen;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "E";
    public static String KEY, IID = "";
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
        IID = preferences.getString("ID", "");


        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragment, new SplashScreen()).commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}