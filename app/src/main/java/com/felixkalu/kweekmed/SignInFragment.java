package com.felixkalu.kweekmed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        final EditText loginUserNameEditText = (EditText)v.findViewById(R.id.loginUserNameEditText);
        final EditText loginUserPasswordEditText = (EditText)v.findViewById(R.id.loginUserPasswordEditText);
        Button signInButton = (Button)v.findViewById(R.id.signInButton);
        TextView forgotPasswordTextView = (TextView)v.findViewById(R.id.forgotPasswordTextView);
        TextView signUpTextView = (TextView)v.findViewById(R.id.signUpTextView);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = loginUserNameEditText.getText().toString();
                String password = loginUserPasswordEditText.getText().toString();
                //check and be sure that username and password fields are not empty
                if (!(userName.matches("") || password.matches(""))) {
                    try {
                        ParseUser.logInInBackground(userName, password, new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    Toast.makeText(getActivity(), "You have Signed in", Toast.LENGTH_LONG).show();
                                    //redirect to the next fragment after.
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
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment signUpFragment = new SignUpFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                signUpFragment.setArguments(args);
                fragmentTransaction.replace(R.id.main_frame, signUpFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }

}
