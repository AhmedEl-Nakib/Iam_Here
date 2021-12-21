package com.nakib.iamhere.utils.locationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        double latitude = Double.valueOf(intent.getStringExtra("latitude"));
        double longitude = Double.valueOf(intent.getStringExtra("longitude"));

        double speed = Double.valueOf(intent.getStringExtra("speed"));
        double altitude = Double.valueOf(intent.getStringExtra("altitude"));

        Log.i("Dataaaaaa",String.valueOf(latitude));
        //Set it to some model class then maintain it in List saved in  sharedprefences - this will help you call the SendtoInternet Method
        //less frequently - i mean based on the number of list items you can take decision that once the list contains 5 items- send it to //the server - Its totally upto you.
//        SendtoInternet(latitude,longitude,altitude,speed);


    }
}