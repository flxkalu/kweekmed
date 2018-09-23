package com.felixkalu.kweekmed;


import android.app.FragmentTransaction;
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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
        }

        //initializing elements
        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
        ImageView symptomsCheckerImageView = (ImageView)v.findViewById(R.id.symptomsCheckerImageView);
        ImageView possibleIssuesImageView = (ImageView)v.findViewById(R.id.possibleIssuesImageView);
        ImageView findaDoctorImageView = (ImageView) v.findViewById(R.id.findaDoctorImageView);

        SearchView searchView = (SearchView) v.findViewById(R.id.searchView1);

        //searchview icon click listener
       searchView.setOnSearchClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //open the next fragment as it is in webMD app
               Log.i("Note", "SearchView clicked");
           }
       });

       //symptomschecker link
        symptomsCheckerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Note", "symptoms checker clicked");

                EnterSymptomInfoFragment enterSymptomInfoFragment = new EnterSymptomInfoFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
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
                fragmentTransaction.replace(R.id.main_frame, possibleIssuesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        findaDoctorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindDoctorFragment findDoctorFragment = new FindDoctorFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, findDoctorFragment);
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

                url = new URL("https://sandbox-authservice.priaid.ch/login");

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
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        //this method function is called when the doingInbackground method is completed and it would pass whatever we return from the doInBackground method, in this case, the result variable.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String newstring = result.replace("null", "");
            try {
                JSONObject jsonObject = new JSONObject(newstring);
                token = jsonObject.getString("Token");
                Log.i("Token ", token);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public String getToken() {
        return token;
    }
}
