package com.felixkalu.kweekmed;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsDetailsFragment extends Fragment {

    public DoctorsDetailsFragment() {
        // Required empty public constructor
    }

    //simple animation for the callnowbutton and locationbutton.
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctors_details, container, false);

        ImageView doctorPicture = (ImageView)v.findViewById(R.id.doctorPicture);
        ImageButton locationImageButton = (ImageButton)v.findViewById(R.id.locationImageButton);
        ImageButton callNowImageButton = (ImageButton)v.findViewById(R.id.callNowImageButton);
        TextView nameTextView = (TextView)v.findViewById(R.id.doctorsDetailsNameTextView);
        TextView specialtyTextView = (TextView)v.findViewById(R.id.doctorsDetailsSpecialtyTextView);
        TextView experienceTextView = (TextView)v.findViewById(R.id.doctorsDetailsExperienceTextView);
        TextView descriptionTextView = (TextView)v.findViewById(R.id.doctorsDetailsDescriptionTextView);


        String name = getArguments().getString("name");
        String surname = getArguments().getString("surname");
        String sex = getArguments().getString("sex");
        String age = getArguments().getString("age");
        String specialty = getArguments().getString("specialty");
        String currentHospitalOfService = getArguments().getString("currentHospitalOfService");
        String yearsOfExperience = getArguments().getString("yearsOfExperience");
        String email = getArguments().getString("email");
        String primaryMobileNumber = getArguments().getString("primaryMobileNumber");
        String medicalCertificate = getArguments().getString("medicalCertificate");
        String photoLink = getArguments().getString("photoLink");
        String description = getArguments().getString("description");
        String location = getArguments().getString("location");
        String doctorId = getArguments().getString("doctorId");

        Log.i("LINK: ", location);

        Picasso.get().load(photoLink).resize(154, 154).into(doctorPicture);
        nameTextView.setText(Html.fromHtml("<b>Full Name:</b><i> "+name + " " + surname));
        specialtyTextView.setText(Html.fromHtml("<b>Specialty:</b><i> " + specialty));
        experienceTextView.setText(Html.fromHtml("<b>Years Of Experience:</b><i> " + yearsOfExperience));
        descriptionTextView.setText(Html.fromHtml("<b>Admin's Comment:</b><i> " + description));

        callNowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
            }
        });

        locationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                Intent intent = new Intent(getActivity(), PatientMapsActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

}