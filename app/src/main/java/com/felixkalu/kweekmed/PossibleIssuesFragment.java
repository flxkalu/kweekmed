package com.felixkalu.kweekmed;


import android.app.FragmentManager;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class PossibleIssuesFragment extends Fragment {


    ListView possibleIssuesListView;
    ProgressBar progressBar;

    public PossibleIssuesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_possible_issues, container, false);

        possibleIssuesListView = (ListView)v.findViewById(R.id.possibleIssuesListView);
        progressBar = (ProgressBar) v.findViewById(R.id.possibleIssuesProgressBar);

        String url = "https://sandbox-healthservice.priaid.ch/issues?token=";
        String language = "&format=json&language=en-gb";
        String token = HomeFragment.token;

        DownloadTask task = new DownloadTask();

        task.execute(url+token+language);

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
            final ArrayList<String> issues = new ArrayList<>();
            final ArrayList<String> issueIds = new ArrayList<>();

            try {
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String name = jsonObject.getString("Name");
                        String id = jsonObject.getString("ID");

                        issues.add(name);
                        issueIds.add(id);

                        Log.i("Issue Name: ", name);
                }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, issues);

                    progressBar.setVisibility(View.GONE);
                    possibleIssuesListView.setAdapter(arrayAdapter);

                    possibleIssuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Log.i("you tapped: ", issues.get(position) + " The Id is: " + issueIds.get(position));// this line of code displays the name of the issue that was tapped.
                            PossibleIssuesDetailFragment possibleIssuesDetailFragment = new PossibleIssuesDetailFragment();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Bundle args = new Bundle();

                            //for sending the content of the clicked listview to the next Fragment where it is displayed in detail.
                            args.putString("issueId", issueIds.get(position));

                            possibleIssuesDetailFragment.setArguments(args);
                            fragmentTransaction.replace(R.id.main_frame, possibleIssuesDetailFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                    }
                });

            } catch (JSONException e) {
                Log.i("MESSAGE 3: ", e.toString());
            }
        }
    }

}
