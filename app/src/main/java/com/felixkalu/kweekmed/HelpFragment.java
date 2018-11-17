package com.felixkalu.kweekmed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {


    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, container, false);

        //hide the activity action bar on this fragment since this fragment has its own toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setTitle("Enter Age and Gender");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

}
