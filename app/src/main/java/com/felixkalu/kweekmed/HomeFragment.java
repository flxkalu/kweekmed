package com.felixkalu.kweekmed;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

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
public class HomeFragment extends Fragment {

    HttpURLConnection myURLConnection;
    String computedHashString;

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

        try {
            setToken();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //initializing elements
        ImageView conditionsImageView = (ImageView)v.findViewById(R.id.conditionsImageView);
        ImageView symptomsCheckerImageView = (ImageView)v.findViewById(R.id.symptomsCheckerImageView);
        ImageView possibleIssuesImageView = (ImageView)v.findViewById(R.id.possibleIssuesImageView);
        ImageView findaDoctorImageView = (ImageView) v.findViewById(R.id.findaDoctorImageView);
        ImageView medicationReminderImageView =(ImageView)v.findViewById(R.id.medicationReminderImageView);
        ImageView drugsAndMedsImageView = (ImageView)v.findViewById(R.id.drugsAndMedsImageView);

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

                PossibleIssuesFragment possibleIssuesFragment = new PossibleIssuesFragment();
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
                FindDoctorFragment findDoctorFragment = new FindDoctorFragment();
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
}
