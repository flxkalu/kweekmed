package com.felixkalu.kweekmed;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import static android.os.Build.VERSION.SDK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements LocationListener {

    LocationManager locationManager;
    LocationListener locationListener;
    Location currentLocation;

    String patientsMessage = "Sign Up as a Patient to recieve the latest news and updates on health magazines.";
    String doctorsMessage = "If you are a Doctor, do not Click the Sign-up Button until you " +
            "are in your Place of work. The save button saves your location which is used by " +
            "patients to get directions to your Office! To activate your account, please send you medical certificate," +
            "passport photograph and pay the subscription fee. All emails should be sent to kweekmed@blabla.com";


    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        final Button signUpButton = (Button) v.findViewById(R.id.signUpButton);

        final EditText userUsernameTextView = (EditText)v.findViewById(R.id.userUsernameTextView);
        final EditText userEmailTextView = (EditText)v.findViewById(R.id.userEmailTextView);
        final EditText userPasswordTextView = (EditText)v.findViewById(R.id.userPasswordTextView);
        final EditText userPasswordConfirmTextView = (EditText)v.findViewById(R.id.userPasswordConfirmTextView);
        final EditText mobileNumberTextView = (EditText)v.findViewById(R.id.mobileNumberTextView);


        final TextView warningTextView = (TextView)v.findViewById(R.id.warningTextView);

        final Switch switch2 = (Switch)v.findViewById(R.id.switch2);
        warningTextView.setText(patientsMessage);

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch2.isChecked()) {
                    signUpButton.setText("Sign Up as a Doctor");
                    warningTextView.setTextColor(Color.parseColor("#ff6666"));
                    warningTextView.setText(doctorsMessage);
                    signUpButton.setBackgroundColor(Color.parseColor("#ff6666"));
                } else {
                    signUpButton.setText("Sign Up as a patient");
                    warningTextView.setTextColor(Color.parseColor("#a9b4f9"));
                    warningTextView.setText(patientsMessage);
                    signUpButton.setBackgroundColor(Color.parseColor("#a9b4f9"));
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checks that all forms are properly filled before signUpButton action happens
                if (!(userUsernameTextView.getText().toString().matches("")
                        || mobileNumberTextView.getText().toString().matches("")
                        || userEmailTextView.getText().toString().matches("")
                        || userPasswordTextView.getText().toString().matches("")
                        || userPasswordConfirmTextView.getText().toString().matches(""))) {
                    if (userPasswordTextView.getText().toString().matches(userPasswordConfirmTextView.getText().toString())) {
                        if (switch2.isChecked()) {
                            saveDoctorsDetails(userUsernameTextView.getText().toString(), userEmailTextView.getText().toString(), userPasswordTextView.getText().toString(), mobileNumberTextView.getText().toString());
                        } else {
                            savePatientsDetails(userUsernameTextView.getText().toString(), userEmailTextView.getText().toString(), userPasswordTextView.getText().toString(), mobileNumberTextView.getText().toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Passwords Do Not Match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Complete Form", Toast.LENGTH_LONG).show();
                }
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
        }
        return v;
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
}
