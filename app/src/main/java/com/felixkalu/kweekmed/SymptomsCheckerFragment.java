package com.felixkalu.kweekmed;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SymptomsCheckerFragment extends Fragment {


    public SymptomsCheckerFragment() {
        // Required empty public constructor
    }


    private ListView listView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_symptoms_checker, container, false);
        getActivity().setTitle("Enter Symptoms?");

        //continue button and what it should do when clicked
        Button continueButton = (Button)v.findViewById(R.id.buttonContinue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PossibleConditionsFragment possibleConditionsFragment = new PossibleConditionsFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, possibleConditionsFragment);
                fragmentTransaction.commit();
            }
        });

        //progressbar for loading when the app is retrieving symptoms from the api
        progressBar = (ProgressBar) v.findViewById(R.id.progress_loader);

        listView = (ListView) v.findViewById(R.id.listview);

        DownloadTask task = new DownloadTask();
        task.execute("https://healthservice.priaid.ch/symptoms?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImZseGthbHVAaG90bWFpbC5jby51ayIsInJvbGUiOiJVc2VyIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvc2lkIjoiMTA4OCIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvdmVyc2lvbiI6IjEwOCIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbGltaXQiOiIxMDAiLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL21lbWJlcnNoaXAiOiJCYXNpYyIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbGFuZ3VhZ2UiOiJlbi1nYiIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvZXhwaXJhdGlvbiI6IjIwOTktMTItMzEiLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL21lbWJlcnNoaXBzdGFydCI6IjIwMTgtMDktMDUiLCJpc3MiOiJodHRwczovL2F1dGhzZXJ2aWNlLnByaWFpZC5jaCIsImF1ZCI6Imh0dHBzOi8vaGVhbHRoc2VydmljZS5wcmlhaWQuY2giLCJleHAiOjE1MzY1MDQzODIsIm5iZiI6MTUzNjQ5NzE4Mn0.AnPVy2jU4qHw-b1_0FgaGzA_5WO_qBV2cp3NgKnndMs&format=json&language=en-gb");

        //get symptoms from api and add them all into the the arraylist



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

            final List<SymptomModel> symptoms = new ArrayList<>();

            try {

                JSONArray jsonArray = new JSONArray(result);

                for(int i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String id = jsonObject.getString("ID");
                    String name = jsonObject.getString("Name");

                    symptoms.add(new SymptomModel(false, name, id));

                    Log.i("Line: ", id + " : " + name);
                }
                progressBar.setVisibility(View.GONE);

                final SymptomsAdapter adapter = new SymptomsAdapter(getActivity(), symptoms);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        SymptomModel model = symptoms.get(i);

                        if(model.isSelected()) {
                            model.setSelected(false);
                            Log.i("you removed", model.getName());
                        } else {
                            model.setSelected(true);
                            Log.i("you Added", model.getName());
                        }

                        symptoms.set(i, model);

                        adapter.updateRecords(symptoms);
                    }
                });

            } catch (JSONException e) {
                Log.i("MESSAGE 3: ", e.toString());
            }
        }
    }

}
