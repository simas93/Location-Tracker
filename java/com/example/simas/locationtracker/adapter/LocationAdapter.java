package com.example.simas.locationtracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simas.locationtracker.model.Location;
import com.example.simas.locationtracker.R;
import com.example.simas.locationtracker.helper.DatabaseHelper;

import java.util.List;

/**
 * Created by simas on 9/2/2016.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Holder> {

    List<Location> locationList;

    public LocationAdapter (Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        locationList = databaseHelper.getAllLocation();
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Location currentLocation = locationList.get(position);
        holder.latitude.setText(String.valueOf(currentLocation.getLatitude()));
        holder.longitude.setText(String.valueOf(currentLocation.getLongitude()));
        holder.address.setText(currentLocation.getLocationAddress());
        holder.time.setText(currentLocation.getTime());
        holder.listNumber.setText(String.valueOf(position + 1)+ ".");
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        protected TextView listNumber, latitude, longitude, address, time;
        public Holder(View itemView) {
            super(itemView);
            listNumber = (TextView) itemView.findViewById(R.id.list_number);
            latitude = (TextView) itemView.findViewById(R.id.latitude);
            longitude = (TextView) itemView.findViewById(R.id.longitude);
            address = (TextView) itemView.findViewById(R.id.address);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
