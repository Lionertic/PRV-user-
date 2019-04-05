package com.example.lionertic.main.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lionertic.main.AsyncTask.KeyCheck;
import com.example.lionertic.main.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class SplashScreen extends Fragment {
    Handler handler;
    Runnable runnable;

    public SplashScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected()) {
                    SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("KEY", MODE_PRIVATE);
                    String KEY = preferences.getString("KEY", "");
                    String ID = preferences.getString("ID", "");
                    if (!TextUtils.isEmpty(KEY)) {
                        new KeyCheck(getContext(), getActivity()).execute(KEY, ID);
                    } else {
                        if (getFragmentManager() != null) {
                            getFragmentManager().beginTransaction().replace(R.id.fragment, new LogIn()).commit();
                        }
                    }
                } else
                    Toast.makeText(getContext(), "NO network..Enable and RESTART APP", Toast.LENGTH_LONG).show();
            }
        };
        handler.postDelayed(runnable, 1500);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }


}
