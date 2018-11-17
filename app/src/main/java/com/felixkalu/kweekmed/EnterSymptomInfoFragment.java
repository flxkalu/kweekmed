package com.felixkalu.kweekmed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterSymptomInfoFragment extends Fragment {


    public EnterSymptomInfoFragment() {
        // Required empty public constructor
    }
    String gender = "male";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_enter_info, container, false);

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


        final EditText ageEditText = (EditText)v.findViewById(R.id.ageEditText);

        Button button = (Button)v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(ageEditText.getText().toString().matches(""))) {
                    SymptomsCheckerFragment symptomsCheckerFragment = new SymptomsCheckerFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                    //to send the age, gender, and sex
                    Bundle args = new Bundle();
                    args.putString("age", ageEditText.getText().toString());
                    args.putString("gender", gender);

                    symptomsCheckerFragment.setArguments(args);

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    fragmentTransaction.replace(R.id.main_frame, symptomsCheckerFragment);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getActivity(), "Please Enter Your Age", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Switch genderSwitch = (Switch) v.findViewById(R.id.genderSwitch);
        genderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (genderSwitch.isChecked()) {
                    gender = "female";
                } else {
                    gender = "male";
                }
            }
        });
        return v;
    }

}
