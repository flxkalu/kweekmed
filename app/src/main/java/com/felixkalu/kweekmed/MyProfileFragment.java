package com.felixkalu.kweekmed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.microedition.khronos.egl.EGLDisplay;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    Boolean verify = false;
    String userProfileName, name, surname, username,
            email, phoneNumber, password, confirmPassword;


    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        getActivity().setTitle("My Profile");

        //to get all the views
        TextView verifyTextView = (TextView)v.findViewById(R.id.verifyTextView);
        TextView user_profile_name =(TextView)v.findViewById(R.id.user_profile_name);

        EditText profileNameEditText = (EditText)v.findViewById(R.id.profileNameEditText);
        EditText profileSurnameEditText = (EditText)v.findViewById(R.id.profileSurnameEditText);
        EditText profileUsernameEditText = (EditText)v.findViewById(R.id.profileUsernameEditText);
        EditText profileEmailEditText = (EditText)v.findViewById(R.id.profileEmailEditText);
        EditText profilePhoneNumberEditText = (EditText)v.findViewById(R.id.profilePhoneNumberEditText);
        EditText profilePasswordEditText = (EditText)v.findViewById(R.id.profilePasswordEditText);
        EditText profileConfirmPasswordEditText = (EditText)v.findViewById(R.id.profileConfirmPasswordEditText);

        ImageView user_profile_photo = (ImageView)v.findViewById(R.id.user_profile_photo);
        ImageView uploadLogoImageView = (ImageView)v.findViewById(R.id.uploadLogoImageView);

        RelativeLayout profile_layout = (RelativeLayout)v.findViewById(R.id.profile_layout);

        return v;
    }

}
