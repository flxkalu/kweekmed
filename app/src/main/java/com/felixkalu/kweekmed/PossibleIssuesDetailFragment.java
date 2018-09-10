package com.felixkalu.kweekmed;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PossibleIssuesDetailFragment extends Fragment {

    String possibleIssueId;
    TextView descriptionTextView;
    TextView medicalConditiontextView;
    TextView nameTextview;
    TextView possibleSymptomsTextView;
    TextView profNametextView;
    TextView synonymsTextView;
    TextView treatmentDescriptionTextView;

    public PossibleIssuesDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_possible_issues_detail, container, false);

        descriptionTextView = (TextView)v.findViewById(R.id.descriptionTextView);
        medicalConditiontextView = (TextView)v.findViewById(R.id.medicalConditiontextView);
        nameTextview = (TextView)v.findViewById(R.id.nameTextview);
        possibleSymptomsTextView = (TextView)v.findViewById(R.id.possibleSymptomsTextView);
        profNametextView = (TextView)v.findViewById(R.id.profNametextView);
        synonymsTextView = (TextView)v.findViewById(R.id.synonymsTextView);
        treatmentDescriptionTextView = (TextView)v.findViewById(R.id.treatmentDescriptionTextView);


        possibleIssueId = getArguments().getString("issueId");
        Log.i("IssueId: ", possibleIssueId);

        DownloadTask task = new DownloadTask();
        task.execute("https://sandbox-healthservice.priaid.ch/issues/11/info?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImZseGthbHVAaG90bWFpbC5jby51ayIsInJvbGUiOiJVc2VyIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvc2lkIjoiMzc5NSIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvdmVyc2lvbiI6IjIwMCIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbGltaXQiOiI5OTk5OTk5OTkiLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL21lbWJlcnNoaXAiOiJQcmVtaXVtIiwiaHR0cDovL2V4YW1wbGUub3JnL2NsYWltcy9sYW5ndWFnZSI6ImVuLWdiIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9leHBpcmF0aW9uIjoiMjA5OS0xMi0zMSIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbWVtYmVyc2hpcHN0YXJ0IjoiMjAxOC0wOS0wNSIsImlzcyI6Imh0dHBzOi8vc2FuZGJveC1hdXRoc2VydmljZS5wcmlhaWQuY2giLCJhdWQiOiJodHRwczovL2hlYWx0aHNlcnZpY2UucHJpYWlkLmNoIiwiZXhwIjoxNTM2NjI0MjgyLCJuYmYiOjE1MzY2MTcwODJ9.pPGNBtsSoxPivG-pHKaGy4QefeiBfS2_3t_f6eODG4o&format=json&language=en-gb");

        return v;
    }

    //this code block takes care of retrieving everything from the api
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            //result string that stores the downloaded content
            String result = "";
            //the url
            URL url;
            //declare new http connection
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]); //passing the url to the urls array parameter in position 0

                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);
                //to be used to read the contents of our reader
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                Log.i("From doInBackground:", result);
                return result;
                //the catch phrase will display this toast if the city name does not exist on the openWeather api

            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Could Not Find anything", Toast.LENGTH_LONG);
            }

            return null;
        }

        //this method function is called when the doingInbackground method is completed and
        //it would pass whatever we return from the doInBackground method, in this case, the result variable.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //for storing issues and issueId
            try {
                JSONObject jsonObject = new JSONObject(result);

                String description = jsonObject.getString("Description");
                String medicalCondition = jsonObject.getString("MedicalCondition");
                String name = jsonObject.getString("Name");
                String possibleSymptoms = jsonObject.getString("PossibleSymptoms");
                String profName = jsonObject.getString("ProfName");
                String synonyms = jsonObject.getString("Synonyms");
                String treatmentDescription = jsonObject.getString("TreatmentDescription");

                descriptionTextView.setText(description);
                medicalConditiontextView.setText(medicalCondition);
                nameTextview.setText(name);
                possibleSymptomsTextView.setText(possibleSymptoms);
                profNametextView.setText(profName);
                synonymsTextView.setText(synonyms);
                treatmentDescriptionTextView.setText(treatmentDescription);

                Log.i("Description: ", description);

            } catch (JSONException e) {
                Log.i("MESSAGE 3: ", e.toString());
            }
        }
    }
}

