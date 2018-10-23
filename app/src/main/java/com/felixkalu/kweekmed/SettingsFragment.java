package com.felixkalu.kweekmed;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements LocationListener {

    LocationManager locationManager;
    Location currentLocation;

    public SettingsFragment() {
        // Required empty public constructor
    }

    String textContentForPopUp = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        getActivity().setTitle("Settings");

        TextView signInTextView = (TextView)v.findViewById(R.id.signInTextView);
        TextView feedBackTextView = (TextView)v.findViewById(R.id.feedBackTextView);
        TextView termsOfUseTextView = (TextView)v.findViewById(R.id.contentTextView);
        TextView privatePolicyTextView = (TextView)v.findViewById(R.id.privatePolicyTextView);
        TextView aboutKweekMedTextView = (TextView)v.findViewById(R.id.aboutKweekMedTextView);
        TextView aboutTheDevelopersTextView = (TextView)v.findViewById(R.id.aboutTheDevelopersTextView);

        TextView signUpPatientTextView = (TextView)v.findViewById(R.id.signUpPatientTextView);
        TextView signUpDoctorTextView = (TextView)v.findViewById(R.id.signUpDoctorTextView);

        //when the signInTextView is clicked...
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

        feedBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParseUser.getCurrentUser()!=null) {
                    startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:to@gmail.com")));
                } else {
                    Toast.makeText(getActivity(), "Please Sign In Before sending Feedback!", Toast.LENGTH_LONG).show();
                }
            }
        });

        termsOfUseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.termsofusepopup, null);
                final TextView contentTextView = (TextView) mView.findViewById(R.id.contentTextView);

                textContentForPopUp = getActivity().getString(R.string.termsofuse);

                Button doneButton = (Button)mView.findViewById(R.id.doneButton);
                contentTextView.setText(textContentForPopUp);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //to show the terms of use pop-up dialog
                dialog.show();
            }
        });

        privatePolicyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.termsofusepopup, null);
                final TextView contentTextView = (TextView) mView.findViewById(R.id.contentTextView);

                textContentForPopUp = getActivity().getString(R.string.privatepolicy);

                Button doneButton = (Button)mView.findViewById(R.id.doneButton);
                contentTextView.setText(textContentForPopUp);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //to show the terms of use pop-up dialog
                dialog.show();
            }
        });

        aboutKweekMedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.termsofusepopup, null);
                final TextView contentTextView = (TextView) mView.findViewById(R.id.contentTextView);

                textContentForPopUp = getActivity().getString(R.string.aboutkweekmed);

                Button doneButton = (Button)mView.findViewById(R.id.doneButton);
                contentTextView.setText(textContentForPopUp);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //to show the terms of use pop-up dialog
                dialog.show();
            }
        });

        aboutTheDevelopersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.termsofusepopup, null);
                final TextView contentTextView = (TextView) mView.findViewById(R.id.contentTextView);

                textContentForPopUp = getActivity().getString(R.string.aboutthedevelopers);

                Button doneButton = (Button)mView.findViewById(R.id.doneButton);
                contentTextView.setText(textContentForPopUp);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //to show the terms of use pop-up dialog
                dialog.show();
            }
        });

        //for when sign up for patient is clicked...
        signUpPatientTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.fragment_sign_up, null);

                final Button signUpButton = (Button) mView.findViewById(R.id.signUpButton2);
                final EditText userUsernameTextView = (EditText)mView.findViewById(R.id.userUsernameTextView);
                final EditText userEmailTextView = (EditText)mView.findViewById(R.id.userEmailTextView);
                final EditText userPasswordTextView = (EditText)mView.findViewById(R.id.userPasswordTextView);
                final EditText userPasswordConfirmTextView = (EditText)mView.findViewById(R.id.userPasswordConfirmTextView);
                final EditText mobileNumberTextView = (EditText)mView.findViewById(R.id.mobileNumberTextView);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                signUpButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ParseUser.getCurrentUser() == null) {
                            //checks that all forms are properly filled before signUpButton action happens
                            if (!(userUsernameTextView.getText().toString().matches("")
                                    || mobileNumberTextView.getText().toString().matches("")
                                    || userEmailTextView.getText().toString().matches("")
                                    || userPasswordTextView.getText().toString().matches("")
                                    || userPasswordConfirmTextView.getText().toString().matches(""))) {
                                if (userPasswordTextView.getText().toString().matches(userPasswordConfirmTextView.getText().toString())) {
                                    savePatientsDetails(userUsernameTextView.getText().toString(), userEmailTextView.getText().toString(), userPasswordTextView.getText().toString(), mobileNumberTextView.getText().toString());
                                    //dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Passwords Do Not Match", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Complete Form", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Sign Out First", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //to show the terms of use pop-up dialog
                dialog.show();
            }
        });

        //signupfor doctors textview
        signUpDoctorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.fragment_sign_up, null);

                final Button signUpButton = (Button) mView.findViewById(R.id.signUpButton2);
                final EditText userUsernameTextView = (EditText)mView.findViewById(R.id.userUsernameTextView);
                final EditText userEmailTextView = (EditText)mView.findViewById(R.id.userEmailTextView);
                final EditText userPasswordTextView = (EditText)mView.findViewById(R.id.userPasswordTextView);
                final EditText userPasswordConfirmTextView = (EditText)mView.findViewById(R.id.userPasswordConfirmTextView);
                final EditText mobileNumberTextView = (EditText)mView.findViewById(R.id.mobileNumberTextView);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                signUpButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ParseUser.getCurrentUser() == null) {
                            //checks that all forms are properly filled before signUpButton action happens
                            if (!(userUsernameTextView.getText().toString().matches("")
                                    || mobileNumberTextView.getText().toString().matches("")
                                    || userEmailTextView.getText().toString().matches("")
                                    || userPasswordTextView.getText().toString().matches("")
                                    || userPasswordConfirmTextView.getText().toString().matches(""))) {
                                if (userPasswordTextView.getText().toString().matches(userPasswordConfirmTextView.getText().toString())) {
                                    saveDoctorsDetails(userUsernameTextView.getText().toString(), userEmailTextView.getText().toString(), userPasswordTextView.getText().toString(), mobileNumberTextView.getText().toString());
                                    //dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Passwords Do Not Match", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Complete Form", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Sign Out First", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //to show the terms of use pop-up dialog
                dialog.show();
            }
        });
        return v;
    }

    public void saveDoctorsDetails(String userName, String email, String password, String primaryMobileNumber ) {

        //does a final check to ensure that currentLocation has something before trying to save
        if (currentLocation == null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        try {
            ParseUser doctor = new ParseUser();
            ParseGeoPoint parseGeoPoint = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());

            doctor.setUsername(userName);
            doctor.setEmail(email);
            doctor.setPassword(password);
            doctor.put("primaryMobileNumber", primaryMobileNumber);
            doctor.put("doctorsLocation", parseGeoPoint);
            doctor.put("userType", "doctor");

            doctor.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "Doctor Account Created", Toast.LENGTH_LONG).show();
                        //redirect to the next fragment.
                    } else {
                        Toast.makeText(getActivity(), "Error Occured "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void savePatientsDetails(String usernmame, String email, String password, String primaryMobileNumber) {

        try {
            ParseUser patient = new ParseUser();

            patient.setUsername(usernmame);
            patient.setEmail(email);
            patient.setPassword(password);
            patient.put("primaryMobileNumber", primaryMobileNumber);
            patient.put("userType", "patient");

            patient.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "Patient Account Created", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Error Occured " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.i("Location", location.toString());
        currentLocation = location;
        Log.i("Current Location", currentLocation.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
