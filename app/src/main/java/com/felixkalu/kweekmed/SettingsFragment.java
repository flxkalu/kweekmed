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
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private FusedLocationProviderClient client;

    Location currentLocation;
    AlertDialog dialog;

    public SettingsFragment() {
        // Required empty public constructor
    }

    String textContentForPopUp = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        //show the activity action bar on this fragment since this fragment does not have it's own toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        getActivity().setTitle("Settings");

        client = LocationServices.getFusedLocationProviderClient(getActivity());

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
                if (ParseUser.getCurrentUser() == null) {

                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    View mView = getLayoutInflater().inflate(R.layout.fragment_sign_in, null);
                    final EditText usernameEditText = (EditText) mView.findViewById(R.id.loginUserNameEditText);
                    final EditText passwordEditText = (EditText) mView.findViewById(R.id.loginUserPasswordEditText);
                    Button singInButton = (Button) mView.findViewById(R.id.signInButton);

                    mBuilder.setView(mView);
                    dialog = mBuilder.create();

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
                                                    Toast.makeText(getActivity(), "You have Signed in", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                    //Redirect to MyProfile fragment.
                                                    MyProfileFragment fragment = new MyProfileFragment();
                                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                                    fragmentTransaction.replace(R.id.main_frame, fragment);
                                                    fragmentTransaction.addToBackStack(null);
                                                    fragmentTransaction.commit();
                                                } else {
                                                    Toast.makeText(getActivity(), "Error Signing in", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Complete Form", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Sign Out First", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //to show the dialog
                    dialog.show();
                } else {
                    Toast.makeText(getActivity(), "Already Signed In as " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        feedBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParseUser.getCurrentUser()!=null) {
                    startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:to@gmail.com")));
                } else {
                    Toast.makeText(getActivity(), "Sign In First!", Toast.LENGTH_LONG).show();
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
                dialog = mBuilder.create();

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
                dialog = mBuilder.create();

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
                dialog = mBuilder.create();

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
                dialog = mBuilder.create();

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
                if (ParseUser.getCurrentUser() == null) {
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    View mView = getLayoutInflater().inflate(R.layout.fragment_sign_up, null);

                    final Button signUpButton = (Button) mView.findViewById(R.id.signUpButton2);
                    final EditText userUsernameTextView = (EditText) mView.findViewById(R.id.userUsernameTextView);
                    final EditText userEmailTextView = (EditText) mView.findViewById(R.id.userEmailTextView);
                    final EditText userPasswordTextView = (EditText) mView.findViewById(R.id.userPasswordTextView);
                    final EditText userPasswordConfirmTextView = (EditText) mView.findViewById(R.id.userPasswordConfirmTextView);
                    final EditText mobileNumberTextView = (EditText) mView.findViewById(R.id.mobileNumberTextView);

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
                                        savePatientsDetails(userUsernameTextView.getText().toString(), userEmailTextView.getText().toString(), userPasswordTextView.getText().toString(), mobileNumberTextView.getText().toString());
                                        //dialog.dismiss();
                                    } else {
                                        Toast.makeText(getActivity(), "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please Complete Form", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Sign Out First", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //to show the terms of use pop-up dialog
                    dialog.show();
                } else {
                    Toast.makeText(getActivity(), "Already Signed In as " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //SignUp doctors textView
        signUpDoctorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ParseUser.getCurrentUser() == null) {
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    View mView = getLayoutInflater().inflate(R.layout.fragment_sign_up, null);

                    final Button signUpButton = (Button) mView.findViewById(R.id.signUpButton2);
                    final EditText userUsernameTextView = (EditText) mView.findViewById(R.id.userUsernameTextView);
                    final EditText userEmailTextView = (EditText) mView.findViewById(R.id.userEmailTextView);
                    final EditText userPasswordTextView = (EditText) mView.findViewById(R.id.userPasswordTextView);
                    final EditText userPasswordConfirmTextView = (EditText) mView.findViewById(R.id.userPasswordConfirmTextView);
                    final EditText mobileNumberTextView = (EditText) mView.findViewById(R.id.mobileNumberTextView);

                    mBuilder.setView(mView);
                    dialog = mBuilder.create();

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
                                        Toast.makeText(getActivity(), "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please Complete Form", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Sign Out First", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //to show the terms of use pop-up dialog
                    dialog.show();
                } else {
                    Toast.makeText(getActivity(), "Already Signed In as " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    public void saveDoctorsDetails(final String userName, final String email, final String password, final String primaryMobileNumber ) {

        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.i("Location Success: ", location.toString());
                    currentLocation = location;
                    try {
                        ParseUser doctor = new ParseUser();
                        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());

                        doctor.setUsername(userName);
                        doctor.setEmail(email);
                        doctor.setPassword(password);
                        doctor.put("primaryMobileNumber", primaryMobileNumber);
                        doctor.put("doctorsLocation", parseGeoPoint);
                        doctor.put("userType", "doctor");
                        doctor.put("photoLink", "https://res.cloudinary.com/the-software-gurus-place/image/upload/v1540382277/kweekmed/profilepictures/noprofilepicture.png");

                        doctor.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getActivity(), "Sign Up complete", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    MyProfileFragment fragment = new MyProfileFragment();
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                    fragmentTransaction.replace(R.id.main_frame, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                } else {
                                    Toast.makeText(getActivity(), "Error Occured " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Log.i("Location ", "not available");
                    Toast.makeText(getActivity(), "Please ensure that Location Services is turned ON", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void savePatientsDetails(String usernmame, String email, String password, String primaryMobileNumber) {

        try {
            ParseUser patient = new ParseUser();

            //this three lines of codes gives gives user flxkalu write access and gives everybody else read access
            //that way, flxkalu can change user properties when he is logged in on the back end.
            //ParseACL groupACL = new ParseACL();
            //groupACL.setPublicReadAccess(true);
            //groupACL.setPublicWriteAccess(true);
            //groupACL.setWriteAccess("US46UMvUNb", true);

            //patient.setACL(groupACL);

            patient.setUsername(usernmame);
            patient.setEmail(email);
            patient.setPassword(password);
            patient.put("primaryMobileNumber", primaryMobileNumber);
            patient.put("userType", "patient");
            patient.put("photoLink", "https://res.cloudinary.com/the-software-gurus-place/image/upload/v1540382277/kweekmed/profilepictures/noprofilepicture.png");

            patient.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "Sign Up Complete", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        MyProfileFragment fragment = new MyProfileFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                        fragmentTransaction.replace(R.id.main_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        Toast.makeText(getActivity(), "Error Occured " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
