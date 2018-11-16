package com.felixkalu.kweekmed;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsListFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FusedLocationProviderClient client;

    private ListView listView;
    private ProgressBar progressBar;
    private DoctorsAdapter doctorsAdapter;
    private Location deviceCurrentLocation;
    private LocationManager locationManager;
    private ArrayList<Double> doctorsLatitudes = new ArrayList<Double>();
    private ArrayList<Double> doctorsLongitudes= new ArrayList<Double>();

    public DoctorsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_doctors_list, container, false);

        getActivity().setTitle("Doctors & their Locations");

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        //initializing all needed stuff
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        listView = (ListView) v.findViewById(R.id.doctorsListView);
        final ArrayList<DoctorsModel> doctorsList = new ArrayList<>();

        //searchView and the clickListener
        SearchView searchView = (SearchView) v.findViewById(R.id.symptomsSearchView);
        searchView.setOnQueryTextListener(this);

        //clear the arrayLists to be on a safer side
        doctorsLatitudes.clear();
        doctorsLongitudes.clear();

        if(ActivityCompat.checkSelfPermission(getActivity(),ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.i("Location Success: ", location.toString());
                        deviceCurrentLocation = location;
                        try {
                            //setting up the query we need to search the parse server
                            ParseQuery<ParseUser> query = new ParseUser().getQuery();
                            final ParseGeoPoint deviceLocation = new ParseGeoPoint(deviceCurrentLocation.getLatitude(), deviceCurrentLocation.getLongitude());

                            //this ensures that it retrieves only doctors from parse user
                            query.whereEqualTo("userType", "doctor");
                            //this line arranges the list according to the closest doctors around the device
                            query.whereNear("doctorsLocation", deviceLocation);
                            //you can use query.whereEqualTo() here to also add search constraints like specialty of doctor needed. Would be used in the future.

                            query.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> doctors, ParseException e) {
                                    //if the exception is null...
                                    if (e == null) {
                                        Log.i("findInBackground", "Retrieved " + doctors.size() + " doctors");
                                        if (doctors.size() > 0) {
                                            for (ParseObject doctor : doctors) {

                                                //for storing doctors location in parseGeoPoint type
                                                ParseGeoPoint doctorsLocation = (ParseGeoPoint) doctor.get("doctorsLocation");

                                                //this two lines of code are used to convert the parselocations to miles.
                                                Double distanceInMiles = deviceLocation.distanceInMilesTo((ParseGeoPoint) doctor.get("doctorsLocation"));
                                                Double distanceOneDecimalPlace = (double) Math.round(distanceInMiles * 10) / 10;

                                                String photoLink = doctor.get("photoLink").toString();
                                                String name = doctor.get("name").toString();
                                                String surname = doctor.get("surname").toString();
                                                String sex = doctor.get("sex").toString();
                                                String age = doctor.get("age").toString();
                                                String specialty = doctor.get("specialty").toString();
                                                String currentHospitalOfService = doctor.get("currentHospitalOfService").toString();
                                                String yearsOfExperience = doctor.get("yearsOfExperience").toString();
                                                String email = "defaultdoctor@mail.com"; //to be reviewed later.
                                                String primaryMobileNumber = doctor.get("primaryMobileNumber").toString();
                                                String medicalCertificate = doctor.get("medicalCertificateLink").toString();
                                                String description = doctor.get("description").toString();
                                                String location = distanceOneDecimalPlace.toString() + " Miles";
                                                //to get the book Id we use getObjectId. refer to parse documentation for android
                                                String doctorId = doctor.getObjectId().toString();

                                                //this block makes sure that doctorslocation has a value if not, it should be null so that the position number is maintained
                                                if (doctorsLocation != null) {
                                                    doctorsLatitudes.add(doctorsLocation.getLatitude());
                                                    doctorsLongitudes.add(doctorsLocation.getLongitude());
                                                } else {
                                                    doctorsLatitudes.add(null);
                                                    doctorsLatitudes.add(null);
                                                }
                                                Log.i("===DOCTORID==== ", doctorId);
                                                doctorsList.add(new DoctorsModel(name, surname, sex, age, specialty, currentHospitalOfService,
                                                        yearsOfExperience, email, primaryMobileNumber, medicalCertificate,
                                                        photoLink, description, location, doctorId));
                                            }
                                        }
                                    }
                                    try {
                                        progressBar.setVisibility(View.GONE);
                                        doctorsAdapter = new DoctorsAdapter(getActivity(), doctorsList);
                                        listView.setAdapter(doctorsAdapter);

                                        listView.setTextFilterEnabled(true);
                                    } catch (NullPointerException ex) {
                                        e.printStackTrace();
                                    } catch (Exception ex) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (NullPointerException ex) {
                            Toast.makeText(getActivity(), "Couldn't get your Location " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            ex.printStackTrace();
                            getFragmentManager().popBackStackImmediate();
                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), "Error " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            ex.printStackTrace();
                            getFragmentManager().popBackStackImmediate();
                        }
                    } else {
                        Log.i("Location ", "not available");
                    }
                }
            });
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DoctorsDetailsFragment doctorsDetailsFragment = new DoctorsDetailsFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();

                args.putDouble("doctorsLatitude", doctorsLatitudes.get(position));
                args.putDouble("doctorsLongitude", doctorsLongitudes.get(position));
                args.putDouble("deviceLocationLatitude", deviceCurrentLocation.getLatitude());
                args.putDouble("deviceLocationLongitude", deviceCurrentLocation.getLongitude());

                //for sending the content of the clicked listView to the next Fragment where it is displayed in detail.
                args.putString("photoLink", doctorsList.get(position).getPhotoLink());
                args.putString("name", doctorsList.get(position).getName());
                args.putString("surname", doctorsList.get(position).getSurname());
                args.putString("sex", doctorsList.get(position).getSex());
                args.putString("age", doctorsList.get(position).getAge());
                args.putString("specialty", doctorsList.get(position).getSpecialty());
                args.putString("currentHospitalOfService", doctorsList.get(position).getCurrentHospitalOfService());
                args.putString("yearsOfExperience", doctorsList.get(position).getYearsOfExperience());
                args.putString("email", doctorsList.get(position).getEmail());
                args.putString("primaryMobileNumber", doctorsList.get(position).getPrimaryMobileNumber());
                args.putString("medicalCertificate", doctorsList.get(position).getMedicalCertificateLink());
                args.putString("description", doctorsList.get(position).getDescription());
                args.putString("doctorsLocationLatitude", doctorsList.get(position).getLocation());
                //this line sends the book id that we would use to find all comments about  a particular book on the book details fragment
                args.putString("doctorId", doctorsList.get(position).getId());

                doctorsDetailsFragment.setArguments(args);
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
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

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
