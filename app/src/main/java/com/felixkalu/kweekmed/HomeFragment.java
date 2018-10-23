package com.felixkalu.kweekmed;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.felixkalu.heart_rate_monitor.HeartRateMonitor;
import com.felixkalu.simplealarms.ui.MainFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Encoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements LocationListener {


    HttpURLConnection myURLConnection;
    String computedHashString;

    private LocationManager locationManager;
    private Location deviceCurrentLocation;

    String userName = "flxkalu@hotmail.co.uk";
    String password = "s3ZYp2q9B4Dac8CSe";
    String authUrl = "https://sandbox-authservice.priaid.ch/login";
    String healthUrl = "https://sandbox-healthservice.priaid.ch";
    String language = "en-gb";

    public static String token;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle("Home");


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            //if the permission is not already granted, request for permission else just get the last known location of the device
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
                deviceCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

        //to get the token by calling the setToken method.
        try {
            setToken();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //initializing elements
        ImageView heartRateMonitorImageView = (ImageView)v.findViewById(R.id.heartRateMonitorImageView);
        ImageView symptomsCheckerImageView = (ImageView)v.findViewById(R.id.symptomsCheckerImageView);
        ImageView possibleIssuesImageView = (ImageView)v.findViewById(R.id.possibleIssuesImageView);
        ImageView findaDoctorImageView = (ImageView) v.findViewById(R.id.findaDoctorImageView);
        ImageView medicationReminderImageView =(ImageView)v.findViewById(R.id.medicationReminderImageView);
        ImageView drugsAndMedsImageView = (ImageView)v.findViewById(R.id.drugsAndMedsImageView);
        ImageView pharmaciesImageView = (ImageView)v.findViewById(R.id.pharmaciesImageView);

        SearchView homeSearchView = (SearchView)v.findViewById(R.id.homeSearchView);

        //symptomsChecker Link
        symptomsCheckerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Note", "symptoms checker clicked");
                EnterSymptomInfoFragment enterSymptomInfoFragment = new EnterSymptomInfoFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(R.id.main_frame, enterSymptomInfoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        possibleIssuesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Note", "Possible Issues Clicked");
                IssuesListFragment possibleIssuesFragment = new IssuesListFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(R.id.main_frame, possibleIssuesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        drugsAndMedsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("drugsAndMedsImageView", "Clicked!");
                DrugsAndMedicationsFragment drugsAndMedicationsFragment = new DrugsAndMedicationsFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(R.id.main_frame, drugsAndMedicationsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        findaDoctorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorsListFragment findDoctorFragment = new DoctorsListFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(R.id.main_frame, findDoctorFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        medicationReminderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragment alarmFragment = new MainFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(R.id.main_frame, alarmFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        heartRateMonitorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HeartRateMonitor.class);
                startActivity(intent);
            }
        });

        //this displays all pharmacies close to the android device.
        pharmaciesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String location = Double.toString(deviceCurrentLocation.getLatitude()) + "," + Double.toString(deviceCurrentLocation.getLongitude());
                    String url = "https://www.google.com/maps/search/pharmacy/@" + location + "z/data=!3m1!4b1";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                } catch (NullPointerException e) {
                    Toast.makeText(getActivity(), "We could not get your location. Please Try again! ", Toast.LENGTH_LONG).show();
                    getFragmentManager().popBackStackImmediate();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error "+e.getMessage(), Toast.LENGTH_LONG).show();
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        homeSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked!", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    public void setToken() throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), "HmacMD5");

        computedHashString = "";
        try {
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(keySpec);
            byte[] result = mac.doFinal(authUrl.getBytes());

            BASE64Encoder encoder = new BASE64Encoder();
            computedHashString = encoder.encode(result);

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Can not create token (NoSuchAlgorithmException)");
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Can not create token (InvalidKeyException)");
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.i("Exception: ", e.getMessage());
        } finally {
            DownloadTask task = new DownloadTask();
            task.execute();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        deviceCurrentLocation = location;
        Log.i("Current Location", deviceCurrentLocation.toString());
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onProviderDisabled(String provider) { }

    class DownloadTask extends AsyncTask<String, Void, String> {

        URL url;
        String result;
        @Override
        protected String doInBackground(String... params) {

            try {

                url = new URL(authUrl);

                myURLConnection = (HttpURLConnection) url.openConnection();
                myURLConnection.setRequestMethod("POST");
                myURLConnection.setRequestProperty("Content-Type", "application/json");
                myURLConnection.setRequestProperty ("Authorization", "Bearer " + userName + ":" + computedHashString);

                InputStream in = myURLConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);
                //to be used to read the contents of our reader
                int data = reader.read();

                int a=0;
                while (data != -1) {
                    a++;
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                Log.i("MalformedURLException ", e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.i("IOException ", e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.i("IOException ", e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        //this method function is called when the doingInBackground method is completed and it would pass whatever we return from the doInBackground method, in this case, the result variable.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                String newstring = result.replace("null", "");
                JSONObject jsonObject = new JSONObject(newstring);
                token = jsonObject.getString("Token");
                Log.i("Token ", token);
            } catch (JSONException e) {
                Log.i("Exception ", e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.i("Exception ", e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    //main activity gets all permissions but this is here just incase the permission is not granted from the main activity avoiding a crash.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
                    deviceCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
    }
}
