package com.felixkalu.kweekmed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
                    fragmentTransaction.replace(R.id.main_frame, symptomsCheckerFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getActivity(), "Please Enter Your Age", Toast.LENGTH_LONG).show();
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
