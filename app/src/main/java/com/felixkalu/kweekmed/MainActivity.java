package com.felixkalu.kweekmed;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    //private LocationManager locationManager;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    AlertDialog dialog;

    private FusedLocationProviderClient client;
    public Location currentLocation;

    ParseUser user = ParseUser.getCurrentUser();

    private HomeFragment homeFragment;
    private HelpFragment helpFragment;
    private NewsFragment newsFragment;
    private SettingsFragment settingsFragment;
    private MyProfileFragment myProfileFragment;

    //array for saving all the required user permissions.
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        //locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
        trustEveryone();

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
        }

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        homeFragment = new HomeFragment();
        newsFragment = new NewsFragment();
        helpFragment = new HelpFragment();
        myProfileFragment = new MyProfileFragment();
        settingsFragment  = new SettingsFragment();

        setFragment(homeFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {
                    //if home button is selected, change the color of the icon to primary color
                    case R.id.nav_home:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        //clear the activity stack anytime the home button is clicked...
                        //This helps make sure that after the user gets to the home page and presses..
                        //the home button it would not go back again after getting to the homepage...
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        return true;

                    case R.id.nav_profile:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        //called this here again to ensure that user is updated everytime the profile button is pressed.
                        user = ParseUser.getCurrentUser();
                        if(user!=null ) {
                            MyProfileFragment fragment = new MyProfileFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            //fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                            fragmentTransaction.replace(R.id.main_frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {
                            signIn();
                        } return true;

                    case R.id.nav_settings:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(settingsFragment);
                        return true;

                    case R.id.nav_news:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(newsFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    public void setFragment(Fragment fragment) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            //this line of code is used to animate fragment transitions
            //fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.replace(R.id.main_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

    }

    //this method is used to grant users all the permissions required for this app to work well.
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

    //this method ensures that when the app is on the home page..
    //the back button does not open a blank activity or open a fragment inside another fragment
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    //this block of code toggles the logout option on the menu
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem register = menu.findItem(R.menu.top_menu);
        if(ParseUser.getCurrentUser()!=null)
            menu.getItem(1).setVisible(true);
        else
            menu.getItem(1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);

        switch (item.getItemId()) {
            case R.id.help :
                setFragment(helpFragment);
                return true;
            case R.id.logout:
                ParseUser.logOut();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                //clear the activityStack
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //redirect to HomeFragment
                setFragment(homeFragment);
                return true;
            default:
                return false;
        }
    }

    public void signIn() {

        if (user == null) {

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.fragment_sign_in, null);
            final EditText usernameEditText = (EditText) mView.findViewById(R.id.loginUserNameEditText);
            final EditText passwordEditText = (EditText) mView.findViewById(R.id.loginUserPasswordEditText);
            Button singInButton = (Button) mView.findViewById(R.id.signInButton);

            mBuilder.setView(mView);
            dialog = mBuilder.create();

            singInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ParseUser.getCurrentUser() == null) {
                        String userName = usernameEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        //check and be sure that username and password fields are not empty
                        try {
                            ParseUser.logInInBackground(userName, password, new LogInCallback() {
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null) {
                                        dialog.dismiss();
                                        //setFragment(myProfileFragment);
                                        MyProfileFragment fragment = new MyProfileFragment();
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                        fragmentTransaction.replace(R.id.main_frame, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        //redirect to profile..
                                    } else {
                                        Toast.makeText(MainActivity.this, "Error Signing in", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Current User: " + user.getUsername(), Toast.LENGTH_SHORT).show();
                    }
                }
            } );
            //to show the dialog
            dialog.show();
        } else {
            //dont show anything.
        }
    }


}