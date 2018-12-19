/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.felixkalu.kweekmed;

import android.app.Application;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.Map;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("b91236d8ad85d95ff3337ed3546723d6ea1ddee0")
            .clientKey("79630adccc5470970ef013abf33daf1ed2f4a5cc")
            .server("http://18.191.252.34:80/parse/")
            .build()
    );

    //without this block of code cloudinary would not work.
    Map config = new HashMap();
    config.put("cloud_name", "the-software-gurus-place");
    config.put("api_key", "544298666218839");
    MediaManager.init(this, config);


    //The commented code below is used to check and confirm that the app is correctly connected to parse server. it is commented because we have confirmed that the connection is working as it should.
    // To revise and remember how this whole thing is done, watch section8, Lecture 128.

//    ParseObject object = new ParseObject("ExampleObjectKweekMed");
//    object.put("Number", "08178058769");
//    object.put("Name", "Tony");
//
//    object.saveInBackground(new SaveCallback() {
//      @Override
//      public void done(ParseException ex) {
//        if (ex == null) {
//          Log.i("Parse Result", "Successful!");
//        } else {
//          Log.i("Parse Result", "Failed" + ex.toString());
//        }
//      }
//    });

    //this line of code automatically creates the user sign up but if you want to take control of user sign up, remove the line. Watch section8, Lecture 131 to understand better

    //ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);
  }
}
