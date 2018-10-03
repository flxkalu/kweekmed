package com.felixkalu.kweekmed;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
public class SymptomsCheckerFragment extends Fragment implements SearchView.OnQueryTextListener{

    public SymptomsCheckerFragment() {
        // Required empty public constructor
    }

    ArrayList<String> symptomNames = new ArrayList<String>();
    ArrayList<String> symptomIds = new ArrayList<String>();

    private SymptomsAdapter adapter;

    private ListView listView;
    private ProgressBar progressBar;

    String token = HomeFragment.token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_symptoms_checker, container, false);
        getActivity().setTitle("Enter Symptoms?");

        SearchView symptomsSearchView = (SearchView)v.findViewById(R.id.symptomsSearchView);
        symptomsSearchView.setOnQueryTextListener(this);

        symptomIds.clear();
        symptomNames.clear();

        //continue button and what it should do when clicked
        Button continueButton = (Button)v.findViewById(R.id.buttonContinue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PossibleConditionsFragment possibleConditionsFragment = new PossibleConditionsFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();

                String age = getArguments().getString("age");
                String gender = getArguments().getString("gender");

                args.putString("age", age);
                args.putString("gender", gender);
                args.putStringArrayList("symptomIds", symptomIds);
                args.putStringArrayList("symptomNames", symptomNames);

                possibleConditionsFragment.setArguments(args);
                fragmentTransaction.replace(R.id.main_frame, possibleConditionsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //progressbar for loading when the app is retrieving symptoms from the api
        progressBar = (ProgressBar) v.findViewById(R.id.progress_loader);
        listView = (ListView) v.findViewById(R.id.listview);

        String url = "https://sandbox-healthservice.priaid.ch/symptoms?token=";
        String language = "&format=json&language=en-gb";

        Log.i("Token from SCF:", token);

        DownloadTask task = new DownloadTask();
        task.execute(url.concat(token).concat(language));
        //get symptoms from api and add them all into the the arraylist

        return v;
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("SEARCHVIEW: ", newText.toString());
        if (TextUtils.isEmpty(newText)) {
            adapter.filter("");
            listView.clearTextFilter();
        } else {
            adapter.filter(newText);
        }
        return true;
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

                adapter = new SymptomsAdapter(getActivity(), symptoms);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Log.i("index", Integer.toString(i));

                        SymptomModel model = symptoms.get(i);
                        try {
                            if (model.isSelected()) {
                                model.setSelected(false);
                                symptomIds.remove(model.getSymptomId());
                                symptomNames.remove(model.getName());
                                Log.i("you removed", model.getName());
                            } else {
                                model.setSelected(true);
                                symptomIds.add(model.getSymptomId());
                                symptomNames.add(model.getName());
                                Log.i("you Added", model.getName());
                            }
                        } catch (Exception e) {
                            Log.i("Exception ", e.getMessage());
                        } finally {
                            symptoms.set(i, model);
                            adapter.updateRecords(symptoms);
                        }
                    }
                });

            } catch (JSONException e) {
                Log.i("MESSAGE 3: ", e.toString());
            }
        }
    }

}
