package com.felixkalu.kweekmed;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnosisFragment extends Fragment {

    public DiagnosisFragment() {
        // Required empty public constructor
    }

    private ListView listView;
    private ProgressBar progressBar;

    ArrayList<String> symptomIds = new ArrayList<>();
    ArrayList<String> symptomNames = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_diagnosis, container, false);

        //hide the activity action bar on this fragment since this fragment has its own toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setTitle("Diagnosis");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        symptomIds = getArguments().getStringArrayList("symptomIds");
        symptomNames = getArguments().getStringArrayList("symptomNames");

        String url = "https://sandbox-healthservice.priaid.ch/diagnosis?symptoms=[";
        String ids = getSymptomIdsString(symptomIds);

        String token = HomeFragment.token;
        String language = "en-gb";

        String age = getArguments().getString("age");
        String gender = getArguments().getString("gender");

        //progressbar for loading when the app is retrieving symptoms from the api
        progressBar = (ProgressBar) v.findViewById(R.id.progress_loader);

        listView = (ListView) v.findViewById(R.id.listview);

        DownloadTask task = new DownloadTask();
        task.execute(url+ids+"]&gender="+gender+"&year_of_birth="+age+"&token="+token+"&format=json&language="+language);

        Log.i("Complete url String", url+ids+"]&gender="+gender+"&year_of_birth="+age+"&token="+token+"&format=json&language="+language);

        return v;
    }

    //this code block takes care of retrieving everything from the api
    class DownloadTask extends AsyncTask<String, Void, String> {

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

            } catch (RuntimeException e) {
               e.printStackTrace();
            } catch (Exception e) {
               e.printStackTrace();
            }
            return null;
        }

        //this method function is called when the doingInbackground method is completed and
        //it would pass whatever we return from the doInBackground method, in this case, the result variable.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            final ArrayList<DiagnosisModel> diagnosis = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(result);

                if (jsonArray.length() == 0) {
                    Toast.makeText(getActivity(), "No Possible Conditions Were Found, Please Visit a Real Doctor ", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //used for storing specialisations returned in the inner loop

                        String specialisationName ="You need:";
                        String issueName = jsonArray.getJSONObject(i).getJSONObject("Issue").getString("Name");
                        String accuracy = jsonArray.getJSONObject(i).getJSONObject("Issue").getString("Accuracy");
                        String issueId = jsonArray.getJSONObject(i).getJSONObject("Issue").getString("ID");

                        Log.i("IssueName: ", issueName);
                        Log.i("Accuracy: ", accuracy);

                        //for getting the specialization of the issue.
                        int arrayLength = jsonArray.getJSONObject(i).getJSONArray("Specialisation").length();
                        for (int j = 0; j < arrayLength; j++) {
                            //if it is only one specialization name...
                            if(arrayLength == 1) {
                                specialisationName +=" "+jsonArray.getJSONObject(i).getJSONArray("Specialisation").getJSONObject(j).getString("Name") + ".";
                                //if it is more than one specialization name, add ,
                            } else {
                                specialisationName +=" "+jsonArray.getJSONObject(i).getJSONArray("Specialisation").getJSONObject(j).getString("Name") + "|";
                            }
                            Log.i("Specialisation: ", specialisationName);
                        }

                        specialisationName = specialisationName.substring(0, specialisationName.length() - 1) + '.';
                        diagnosis.add(new DiagnosisModel(issueName, accuracy, specialisationName, issueId));
                    }

                    progressBar.setVisibility(View.GONE);

                    final DiagnosisAdapter adapter = new DiagnosisAdapter(getActivity(), diagnosis);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            DiagnosisModel model = diagnosis.get(position);
                            diagnosis.set(position, model);

                            adapter.updateRecords(diagnosis);

                            Log.i("diagnosis fragment: ", diagnosis.get(position).issueId);

                            IssuesDetailFragment possibleIssuesDetailFragment = new IssuesDetailFragment();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Bundle args = new Bundle();

                            //for sending the content of the clicked listview to the next Fragment where it is displayed in detail.
                            args.putString("issueId", diagnosis.get(position).issueId);

                            possibleIssuesDetailFragment.setArguments(args);
                            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                            fragmentTransaction.replace(R.id.main_frame, possibleIssuesDetailFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                }
            } catch(JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getSymptomIdsString(ArrayList<String> symptomIds) {

        String ids="";
        //to get all ids and make string
        for(int i = 0; i<symptomIds.size(); i++) {
            ids += symptomIds.get(i) + ",";
        }

        if(ids.endsWith(",")) {
            ids = ids.substring(0, ids.length() - 1) + "";
        }
        return ids;
    }
}
