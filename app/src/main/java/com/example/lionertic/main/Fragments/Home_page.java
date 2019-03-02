package com.example.lionertic.main.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.example.lionertic.main.R;
import com.example.lionertic.main.gridview.GridItemView;
import com.example.lionertic.main.gridview.GridViewAdapter;

import java.util.ArrayList;

import static com.example.lionertic.main.MainActivity.activity;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home_page extends Fragment {

    private GridView gridView;
    private View serviceReq;
    private ArrayList<String> selectedStrings;
    private static final String[] numbers = new String[]{
            "A", "B", "C", "D", "E", "F"};

    public Home_page() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridView = view.findViewById(R.id.gridview);
        selectedStrings = new ArrayList<>();
        serviceReq = view.findViewById(R.id.req);


        final GridViewAdapter adapter = new GridViewAdapter(numbers, getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int selectedIndex = adapter.selectedPositions.indexOf(position);
                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) v).display(false);
                    selectedStrings.remove((String) parent.getItemAtPosition(position));
                } else {
                    adapter.selectedPositions.add(position);
                    ((GridItemView) v).display(true);
                    selectedStrings.add((String) parent.getItemAtPosition(position));
                }
            }
        });
        serviceReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                t.setText( selectedStrings.toString());
                Maps m = new Maps();
                FragmentManager fm = ((FragmentActivity)activity).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment, m).commit();

            }
        });
    }
}
