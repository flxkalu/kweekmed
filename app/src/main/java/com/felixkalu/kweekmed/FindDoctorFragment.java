package com.felixkalu.kweekmed;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindDoctorFragment extends Fragment implements SearchView.OnQueryTextListener {

    private ListView listView;
    private ProgressBar progressBar;
    private DoctorsAdapter doctorsAdapter;


    public FindDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_doctors_list, container, false);

        progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        listView = (ListView)v.findViewById(R.id.doctorsListView);
        final ArrayList<DoctorsModel> doctorsList = new ArrayList<>();

        SearchView searchView = (SearchView) v.findViewById(R.id.searchView1);
        searchView.setOnQueryTextListener(this);

        //setting up the query we need to search the parse server
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Doctors");
        query.orderByAscending("Title");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> doctors, ParseException e) {

                //if the exception is null...
                if (e == null) {
                    Log.i("findInBackground", "Retrieved " + doctors.size() + " doctors");
                    if (doctors.size() > 0) {
                        for (ParseObject doctor : doctors) {
                            Log.i("Doctor: ", doctor.get("photoLink").toString());
                            Log.i("name: ", doctor.get("name").toString());
                            Log.i("specialty: ", doctor.get("specialty").toString());
                            Log.i("location: ", doctor.get("location").toString());

                            String photoLink = doctor.get("photoLink").toString();
                            String name = doctor.get("name").toString();
                            String specialty = doctor.get("specialty").toString();
                            String location = doctor.get("location").toString();
                            //to get the book Id we use getObjectId. refer to parse documentation for android
                            String doctorId = doctor.getObjectId().toString();

                            Log.i("===BOOKID==== ", doctorId);
                            doctorsList.add(new DoctorsModel(photoLink, name, specialty, location));
                        }
                    }
                }
                try {
                    progressBar.setVisibility(View.GONE);
                    doctorsAdapter = new DoctorsAdapter(getActivity(), doctorsList);
                    listView.setAdapter(doctorsAdapter);

                    listView.setTextFilterEnabled(true);
                }
                catch (Exception ex){
                    Log.i("EXCEPTION ERROR", ex.getMessage());
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DoctorsDetailsFragment doctorsDetailsFragment = new DoctorsDetailsFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, doctorsDetailsFragment);
                Bundle args = new Bundle();

                //for sending the content of the clicked listview to the next Fragment where it is displayed in detail.
                args.putString("name", doctorsList.get(position).getName());
                args.putString("surname", doctorsList.get(position).getSurname());
                args.putString("specialty", doctorsList.get(position).getSpecialty());
                args.putString("coverPictureLink", doctorsList.get(position).getPhotoLink());
                //this line sends the book id that we would use to find all comments about  a particular book on the book details fragment
                args.putString("doctorId", doctorsList.get(position).getId());

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                doctorsDetailsFragment.setArguments(args);
                fragmentTransaction.replace(R.id.main_frame, doctorsDetailsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //this is for the searchView
    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("SEARCHVIEW: ", newText.toString());
        if (TextUtils.isEmpty(newText)) {
            doctorsAdapter.filter("");
            listView.clearTextFilter();
        } else {
            doctorsAdapter.filter(newText);
        }
        return true;
    }

}
