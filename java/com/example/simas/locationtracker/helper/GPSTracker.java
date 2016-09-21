package com.example.simas.locationtracker.helper;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    boolean canGetLocation = false;
    private DatabaseHelper databaseHelper;
    private Location location;
    private double latitude;
    private double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5;

    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        getLocation();
    }

    @TargetApi(23)
    public Location getLocation() {

        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if(isGPSEnabled) {
                    this.canGetLocation = true;
                    System.out.println("GPS Enabled");
                    if(location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if(locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                } else if (isNetworkEnabled) {
                    this.canGetLocation = true;

                    System.out.println("Network Enabled");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void addMarker() {
        if(canGetLocation()){
            double latitude = getLatitude();
            double longitude = getLongitude();
            List<Address> address = null;
            try {
                address = new Geocoder(this).getFromLocation(latitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            com.example.simas.locationtracker.model.Location myLocation = new com.example.simas.locationtracker.model.Location();
            if (address != null){
                myLocation.setLocationAddress(address.get(0).getAddressLine(0));
            }else {
                myLocation.setLocationAddress("");
            }

            myLocation.setLatitude(latitude);
            myLocation.setLongitude(longitude);
            myLocation.setTime(getDateTime());
            databaseHelper.addLocation(myLocation);
            if (databaseHelper.getCount() > 50){
                databaseHelper.deleteLocation();
            }
        }
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MMM d, HH:mm", Locale.ENGLISH);
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Apsilankiau");
        this.location = location;
        addMarker();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}


