package com.example.simas.locationtracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simas.locationtracker.R;
import com.example.simas.locationtracker.helper.DatabaseHelper;
import com.example.simas.locationtracker.model.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simas on 9/7/2016.
 */
public class MapFragmentLayout extends Fragment {

    public GoogleMap mMap;
    private static DatabaseHelper databaseHelper;
    private List<Location> locationList;
    private Marker marker;
    MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = DatabaseHelper.getInstance(getActivity());

        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                locationList = new ArrayList<Location>();

                if(marker != null){
                    mMap.clear();
                }
                locationList = databaseHelper.getAllLocation();
                for (int i = 0; i < locationList.size(); i++) {
                    Location location = locationList.get(i);
                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    marker = mMap.addMarker(new MarkerOptions().position(latlng).title(location.getTime()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
                }
            }
        });
    }
}
