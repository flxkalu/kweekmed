package com.felixkalu.kweekmed;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.utils.ObjectUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    //get the current user from parse server
    ParseUser user = ParseUser.getCurrentUser();
    ProgressBar profilePictureProgressBar;
    ImageView user_profile_photo;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        getActivity().setTitle("My Profile");

        if(user!=null) {

            //to get all the views
            TextView verifyTextView = (TextView) v.findViewById(R.id.verifyTextView);
            final TextView user_profile_name = (TextView) v.findViewById(R.id.user_profile_name);

            profilePictureProgressBar = (ProgressBar)v.findViewById(R.id.profilePictureProgressBar);
            profilePictureProgressBar.setVisibility(View.INVISIBLE);
            //change color red if the version of the OS is > Lollipop
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                profilePictureProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                profilePictureProgressBar.setMax(100);
            }

            final EditText profileNameEditText = (EditText) v.findViewById(R.id.profileNameEditText);
            final EditText profileSurnameEditText = (EditText) v.findViewById(R.id.profileSurnameEditText);
            final EditText profileUsernameEditText = (EditText) v.findViewById(R.id.profileUsernameEditText);
            final EditText profileEmailEditText = (EditText) v.findViewById(R.id.profileEmailEditText);
            final EditText profilePhoneNumberEditText = (EditText) v.findViewById(R.id.profilePhoneNumberEditText);
            final EditText profilePasswordEditText = (EditText) v.findViewById(R.id.profilePasswordEditText);
            final EditText profileConfirmPasswordEditText = (EditText) v.findViewById(R.id.profileConfirmPasswordEditText);
            final EditText profileAgeEditText = (EditText)v.findViewById(R.id.ageEditText);

            user_profile_photo = (ImageView) v.findViewById(R.id.user_profile_photo);
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

            //setting the different views with the right information from parse server
            try {
                user_profile_name.setText(user.getUsername());
                profileNameEditText.setText(user.get("name").toString());
                profileSurnameEditText.setText(user.get("surname").toString());
                profileUsernameEditText.setText(user.getUsername());
                profileEmailEditText.setText(user.getEmail());
                profileAgeEditText.setText(user.get("age").toString());
                profilePhoneNumberEditText.setText(user.get("primaryMobileNumber").toString());
                Picasso.get().load(user.get("photoLink").toString()).resize(120, 120).into(user_profile_photo);
            } catch (NullPointerException e) {
                Toast.makeText(getActivity(),"Complete Your Registration",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }

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
                    String newAge = profileAgeEditText.getText().toString();

                    user.put("name", newName);
                    user.put("surname", newSurname);
                    user.put("age", newAge);
                    user.setUsername(newUserName);
                    user.put("primaryMobileNumber", newMobileNumber);
                    user.setEmail(newEmailAddress);

                    //for setting password
                    if (!(newPassword.matches("") || newPasswordConfirm.matches("") || newUserName.matches(""))) {
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
                        Toast.makeText(getActivity(), "Username and password fields are mandatory for security reasons", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            user_profile_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            getPhoto();
                        }
                        //if we are not in marshMellow?
                    } else {
                        getPhoto();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Log In First", Toast.LENGTH_SHORT).show();
            //redirect to home page
            HomeFragment fragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.replace(R.id.main_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        return v;
    }

    //this is called when user taps on the profile picture for uploading new profile picture.
    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //for startActivityForResult, the second parameter, requestCode is used to identify this particular intent
        startActivityForResult(intent, 1);
    }

    //when the user has given us permission,
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }

    //I believe this method is called anytime the startActivityForResult() method is called.
    //It uses the request code which is sent from startActivityForResult()
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {

            //this gets the link to our image and assigns it to selectedImage Uri
            Uri selectedImage = data.getData();
                try {
                    String requestId = MediaManager.get().upload(selectedImage)
                             .unsigned("vpzgkhsb")
                             .option("resource_type", "image")
                             .option("folder", "kweekmed/profilepictures/")
                             .callback(new UploadCallback() {
                                 @Override
                                 public void onStart(String requestId) {
                                     Log.i("Upload: ", "Started!");
                                     user_profile_photo.setVisibility(View.INVISIBLE);
                                     profilePictureProgressBar.setProgress(0);
                                     profilePictureProgressBar.setVisibility(View.VISIBLE);
                                 }

                                 @Override
                                 public void onProgress(String requestId, long bytes, long totalBytes) {
                                     //Double progress = (double) bytes/totalBytes;
                                     profilePictureProgressBar.setProgress((int) Math.round((bytes/totalBytes)*100));
                                 }

                                 @Override
                                 public void onSuccess(String requestId, Map resultData) {
                                    Log.i("Upload is: ", "Successful "+ resultData.toString());
                                    profilePictureProgressBar.setVisibility(View.INVISIBLE);
                                     //prepare image link to be saved in parse server database
                                     user.put("photoLink", resultData.get("url"));
                                     try {
                                         user.save();
                                     } catch (ParseException e) {
                                         Toast.makeText(getActivity(), "Error "+e.getMessage()+" Try again", Toast.LENGTH_SHORT ).show();
                                     } catch (Exception e) {
                                         Toast.makeText(getActivity(), "Error "+e.getMessage()+" Try again", Toast.LENGTH_SHORT ).show();
                                     } finally {
                                         //refresh the current fragment..
                                         FragmentTransaction ft = getFragmentManager().beginTransaction();
                                         ft.detach(MyProfileFragment.this).attach(MyProfileFragment.this).commit();

                                         Toast.makeText(getActivity(), "Profile Picture Changed!", Toast.LENGTH_SHORT).show();
                                     }
                                 }

                                 @Override
                                 public void onError(String requestId, ErrorInfo error) {
                                     //refresh the current fragment..
                                     FragmentTransaction ft = getFragmentManager().beginTransaction();
                                     ft.detach(MyProfileFragment.this).attach(MyProfileFragment.this).commit();

                                     Toast.makeText(getActivity(), "Error uploading Profile Pic, Try again", Toast.LENGTH_SHORT).show();
                                 }

                                 @Override
                                 public void onReschedule(String requestId, ErrorInfo error) {

                                 }
                             })
                            .dispatch();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
        }
    }
}
