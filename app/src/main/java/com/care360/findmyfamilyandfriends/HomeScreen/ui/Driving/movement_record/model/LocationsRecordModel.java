package com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.model;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class LocationsRecordModel implements Serializable {

    private LatLng location;

    public LocationsRecordModel(LatLng location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String toString() {
        return "LocationsRecordModel{" +
                "location=" + location +
                '}';
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
