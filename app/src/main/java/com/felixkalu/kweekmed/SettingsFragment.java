package com.felixkalu.kweekmed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);


        TextView signInTextView = (TextView)v.findViewById(R.id.signInTextView);
//        signInTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SignInFragment signInFragment = new SignInFragment();
//                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                Bundle args = new Bundle();
//                signInFragment.setArguments(args);
//                fragmentTransaction.replace(R.id.main_frame, signInFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.fragment_sign_in, null);
                final EditText usernameEditText = (EditText) mView.findViewById(R.id.loginUserNameEditText);
                final EditText passwordEditText = (EditText) mView.findViewById(R.id.loginUserPasswordEditText);
                Button singInButton = (Button)mView.findViewById(R.id.signInButton);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                singInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ParseUser.getCurrentUser() == null) {
                            String userName = usernameEditText.getText().toString();
                            String password = passwordEditText.getText().toString();
                            //check and be sure that username and password fields are not empty
                            if (!(userName.matches("") || password.matches(""))) {
                                try {
                                    ParseUser.logInInBackground(userName, password, new LogInCallback() {
                                        public void done(ParseUser user, ParseException e) {
                                            if (user != null) {
                                                Toast.makeText(getActivity(), "You have Signed in", Toast.LENGTH_LONG).show();
                                                //close the log in pop-up dialog
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(getActivity(), "Error Signing in", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Complete The Sign In Form", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Already Signed In. Sign Out First", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //to show the dialog
                dialog.show();
            }
        });
        return v;
    }

}
