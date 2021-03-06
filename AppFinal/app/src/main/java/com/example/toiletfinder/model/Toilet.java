package com.example.toiletfinder.model;

import android.util.Pair;

import com.example.toiletfinder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Toilet {
    private Integer id;
    private Integer group_id;
    private String description;
    private String location;
    private Boolean occupied;
    private Integer methane_level;
    private Pair<Double, Double> latlng;

    Marker occupiedMarker;
    Marker freeMarker;
    Marker unknownMarker;
    boolean markerSet = false;

    public Toilet(Integer id, Integer group_id) {
        this.setId(id);
        this.setGroupId(group_id);
    }

    public Toilet(Integer id, Integer group_id, String description, String location, Boolean occupied, Integer methane_level, Pair<Double, Double> latlng) {
        this.setId(id);
        this.setGroupId(group_id);
        this.setDescription(description);
        this.setLocation(location);
        this.setOccupied(occupied);
        this.setMethane_level(methane_level);
        this.setLatLng(latlng);
    }

    public void setUpMarkers(GoogleMap map) {
        occupiedMarker = map.addMarker(new MarkerOptions().position(new LatLng(latlng.first, latlng.second)).icon(BitmapDescriptorFactory.fromResource(R.drawable.toiletocc)).title(this.getId().toString()));
        freeMarker = map.addMarker(new MarkerOptions().position(new LatLng(latlng.first, latlng.second)).icon(BitmapDescriptorFactory.fromResource(R.drawable.toiletfree)).title(this.getId().toString()));
        unknownMarker = map.addMarker(new MarkerOptions().position(new LatLng(latlng.first, latlng.second)).icon(BitmapDescriptorFactory.fromResource(R.drawable.toiletunknown)).title(this.getId().toString()));
        markerSet = true;
        markerEvaluate();
    }

    private void synchMakersWithMap() {
    }


    public void markerEvaluate() {
        if (occupied != null) {
            occupiedMarker.setVisible(occupied);
            freeMarker.setVisible(!occupied);
            unknownMarker.setVisible(false);
        } else {
            occupiedMarker.setVisible(false);
            freeMarker.setVisible(false);
            unknownMarker.setVisible(true);
        }
    }


    public void setLatLng(Pair<Double, Double> latlng) {
        this.latlng = latlng;

    }

    public Pair<Double, Double> getLatLng() {
        return latlng;
    }

    public void setGroupId(Integer groupid) {
        this.group_id = groupid;
    }

    public Integer getGroupId() {
        return this.group_id;
    }

    public Integer getMethane_level() {
        return methane_level;
    }


    public void setMethane_level(Integer methane_level) {
        if (methane_level <= 100)
            this.methane_level = methane_level;
    }


    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isMarkerSet() {
        return markerSet;
    }
}

