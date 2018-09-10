package com.felixkalu.kweekmed;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //initializing elements
        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
        ImageView symptomsCheckerImageView = (ImageView)v.findViewById(R.id.symptomsCheckerImageView);
        ImageView possibleIssuesImageView = (ImageView)v.findViewById(R.id.possibleIssuesImageView);

        SearchView searchView = (SearchView) v.findViewById(R.id.searchView1);

        //searchview icon click listener
       searchView.setOnSearchClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //open the next fragment as it is in webMD app
               Log.i("Note", "SearchView clicked");

           }
       });

       //symptomschecker link
        symptomsCheckerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Note", "symptoms checker clicked");

                SymptomsCheckerFragment symptomsCheckerFragment = new SymptomsCheckerFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, symptomsCheckerFragment);
                fragmentTransaction.commit();
            }
        });

        possibleIssuesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Note", "Possible Issues Clicked");

                PossibleIssuesFragment possibleIssuesFragment = new PossibleIssuesFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, possibleIssuesFragment);
                fragmentTransaction.commit();
            }
        });
        return v;
    }
}
