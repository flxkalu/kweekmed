package com.felixkalu.kweekmed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felixkalu.kweekmed.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnosisDetailFragment extends Fragment {


    public DiagnosisDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_diagnosis_detail, container, false);




        return v;
    }

}
