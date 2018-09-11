package com.felixkalu.kweekmed;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
public class PossibleConditionsFragment extends Fragment {

    public PossibleConditionsFragment() {
        // Required empty public constructor
    }

    private ListView listView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_possible_conditions, container, false);

        //progressbar for loading when the app is retrieving symptoms from the api
        progressBar = (ProgressBar) v.findViewById(R.id.progress_loader);

        listView = (ListView) v.findViewById(R.id.listview);

        DownloadTask task = new DownloadTask();

        task.execute("https://sandbox-healthservice.priaid.ch/diagnosis?symptoms=[11,12,13]&gender=male&year_of_birth=32&token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImZseGthbHVAaG90bWFpbC5jby51ayIsInJvbGUiOiJVc2VyIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvc2lkIjoiMzc5NSIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvdmVyc2lvbiI6IjIwMCIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbGltaXQiOiI5OTk5OTk5OTkiLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL21lbWJlcnNoaXAiOiJQcmVtaXVtIiwiaHR0cDovL2V4YW1wbGUub3JnL2NsYWltcy9sYW5ndWFnZSI6ImVuLWdiIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9leHBpcmF0aW9uIjoiMjA5OS0xMi0zMSIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbWVtYmVyc2hpcHN0YXJ0IjoiMjAxOC0wOS0wNSIsImlzcyI6Imh0dHBzOi8vc2FuZGJveC1hdXRoc2VydmljZS5wcmlhaWQuY2giLCJhdWQiOiJodHRwczovL2hlYWx0aHNlcnZpY2UucHJpYWlkLmNoIiwiZXhwIjoxNTM2NjY5NTk1LCJuYmYiOjE1MzY2NjIzOTV9.owpCSlTyAG1ti1ug-mcKR_LzFL0PygLIH4dF2bHlPoE&format=json&language=en-gb");
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

            final ArrayList <DiagnosisModel> diagnosis = new ArrayList<>();

            try {

                JSONArray jsonArray = new JSONArray(result);
                for(int i=0; i<jsonArray.length(); i++) {
                    //used for storing specialisations returned in the inner loop

                    //JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //id += jsonArray.getJSONObject(i).getJSONObject("Issue").toString();
                    String specialisationName = "";
                    String issueName = jsonArray.getJSONObject(i).getJSONObject("Issue").getString("Name");
                    String accuracy = jsonArray.getJSONObject(i).getJSONObject("Issue").getString("Accuracy");

                    Log.i("IssueName: ", issueName);
                    Log.i("Accuracy: ", accuracy);

                    //diagnosis.add(new DiagnosisModel("Headache", "009" , "General"));

                    //for getting the specialization of the issue.
                    int arrayLength = jsonArray.getJSONObject(i).getJSONArray("Specialisation").length();
                    for (int j = 0; j < arrayLength; j++) {
                        specialisationName += jsonArray.getJSONObject(i).getJSONArray("Specialisation").getJSONObject(j).getString("Name")+" ";
                        Log.i("Specialisation: ", specialisationName);
                    }
                    diagnosis.add(new DiagnosisModel(issueName,accuracy,specialisationName));
                }

//                Log.i("ID: ", id);
//                    //get the conditions
//                    for(int i=0; i<jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);

//                                //get the specializations inside every condition
//                                JSONArray internalArray = new JSONArray(id);
//                                for(int a=0; a<internalArray.length(); a++) {
//                                    JSONObject jsonObjectInternal = internalArray.getJSONObject(a);
//                                    String name = jsonObjectInternal.getString("Name");
//
//                                    //display the specializations
//                                    Log.i("Specialization", name);
//
//
//                                }
//                       // String name = jsonObject.getString("Name");
//
//                        //possibleConditions.add(id);
//                        Log.i("Line: ", " : " + i);
//                }
                progressBar.setVisibility(View.GONE);

                final DiagnosisAdapter adapter = new DiagnosisAdapter(getActivity(), diagnosis);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        DiagnosisModel model = diagnosis.get(i);

//                        if(model.isSelected()) {
//                            model.setSelected(false);
//                            Log.i("you removed", model.getName());
//                        } else {
//                            model.setSelected(true);
//                            Log.i("you Added", model.getName());
//                        }

                        diagnosis.set(i, model);

                        adapter.updateRecords(diagnosis);
                    }
                });

            } catch (JSONException e) {
                Log.i("MESSAGE 3: ", e.toString());
            }
        }
    }

}
