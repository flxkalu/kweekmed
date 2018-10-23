package com.felixkalu.kweekmed;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

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

        final ParseUser user = ParseUser.getCurrentUser();

        if(user!=null) {

            //to get all the views
            TextView verifyTextView = (TextView) v.findViewById(R.id.verifyTextView);
            TextView user_profile_name = (TextView) v.findViewById(R.id.user_profile_name);

            final EditText profileNameEditText = (EditText) v.findViewById(R.id.profileNameEditText);
            final EditText profileSurnameEditText = (EditText) v.findViewById(R.id.profileSurnameEditText);
            final EditText profileUsernameEditText = (EditText) v.findViewById(R.id.profileUsernameEditText);
            final EditText profileEmailEditText = (EditText) v.findViewById(R.id.profileEmailEditText);
            final EditText profilePhoneNumberEditText = (EditText) v.findViewById(R.id.profilePhoneNumberEditText);
            final EditText profilePasswordEditText = (EditText) v.findViewById(R.id.profilePasswordEditText);
            final EditText profileConfirmPasswordEditText = (EditText) v.findViewById(R.id.profileConfirmPasswordEditText);

            ImageView user_profile_photo = (ImageView) v.findViewById(R.id.user_profile_photo);
            ImageView uploadLogoImageView = (ImageView) v.findViewById(R.id.uploadLogoImageView);

            RelativeLayout profile_layout = (RelativeLayout) v.findViewById(R.id.profile_layout);

            //change the second banner color to blue or red depending if the user is a patient or doctor
            if(user.get("userType").toString().matches("patient")) {
                profile_layout.setBackgroundColor(Color.parseColor("#2b3392"));
            } else if(user.get("userType").toString().matches("doctor")){
                profile_layout.setBackgroundColor(Color.parseColor("#950110"));
            }


            //change the verify textview pending if the user is verified or not
            if(user.getBoolean("verify")==true) {
                verifyTextView.setText("Verified");
            } else if(user.getBoolean("verify")==false) {
                verifyTextView.setText("Not verified");
            } else {
                verifyTextView.setText("N/A");
            }


            //setting the different views witht the right information from parse server
            user_profile_name.setText(user.get("name").toString()+" "+user.get("surname").toString());
            profileNameEditText.setText(user.get("name").toString());
            profileSurnameEditText.setText(user.get("surname").toString());
            profileUsernameEditText.setText(user.getUsername().toString());
            profileEmailEditText.setText(user.getEmail());
            profilePhoneNumberEditText.setText(user.get("primaryMobileNumber").toString());


            Picasso.get().load(user.get("photoLink").toString()).resize(120, 120).into(user_profile_photo);

            //For updating profile info
            uploadLogoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newName = profileNameEditText.getText().toString();
                    String newSurname = profileSurnameEditText.getText().toString();
                    String newUserName = profileUsernameEditText.getText().toString();
                    String newMobileNumber = profilePhoneNumberEditText.getText().toString();
                    String newPassword = profilePasswordEditText.getText().toString();
                    String newPasswordConfirm = profileConfirmPasswordEditText.getText().toString();
                    String newEmailAddress = profileEmailEditText.getText().toString();

                    user.put("name", newName);
                    user.put("surname", newSurname);
                    user.setUsername(newUserName);
                    user.put("primaryMobileNumber", newMobileNumber);
                    user.setEmail(newEmailAddress);

                    //for setting password
                    if (!(newPassword.matches("") || newPasswordConfirm.matches(""))) {
                        if (newPassword.matches(newPasswordConfirm)) {
                            user.setPassword(newPassword);
                            //to update user profile
                            try {
                                user.save();
                            } catch (ParseException e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            } finally {
                                //refresh the fragment
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(MyProfileFragment.this).attach(MyProfileFragment.this).commit();
                                //clear the password edittexts
                                profilePasswordEditText.getText().clear();
                                profileConfirmPasswordEditText.getText().clear();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Input Password", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(getActivity(), "Log In First", Toast.LENGTH_SHORT).show();
        }
        return v;
    }
}
