package com.example.simas.locationtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.simas.locationtracker.helper.GPSTracker;

/**
 * Created by simas on 9/4/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    static GPSTracker gps;

    @Override
    public void onReceive(Context context, Intent intent) {
            if (gps == null) {
                gps = new GPSTracker(context);
            }
    }
}
