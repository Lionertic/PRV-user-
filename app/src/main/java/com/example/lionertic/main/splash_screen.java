package com.example.lionertic.main;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lionertic.main.Fragments.LogIn;
import com.example.lionertic.main.MainActivity;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class splash_screen extends Fragment {


    public splash_screen() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isNetworkConnected()){

                    getActivity().setTitle("Sign In");
                    LogIn m = new LogIn();
                    FragmentManager fm = getFragmentManager();
                    if (fm != null) {
                        fm.beginTransaction().replace(R.id.fragment, m).commit();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "NO network..Enable and RESTART APP", Toast.LENGTH_LONG).show();
                }
            }
        },3500);

    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

}
