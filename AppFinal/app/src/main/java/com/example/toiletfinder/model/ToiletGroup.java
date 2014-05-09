package com.example.toiletfinder.model;

import android.util.Pair;

import com.example.toiletfinder.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class ToiletGroup {
    private Integer id;
    private Pair<Double, Double> latlng;
    private String name;
    private Map<String, Toilet> toilets;

    MarkerOptions marker;
    boolean markerAdded = false;

    public ToiletGroup(Integer id, Pair<Double, Double> latlng, String name, Map<String, Toilet> toilets) {
        this.setId(id);
        this.setLatLng(latlng);
        this.setName(name);
        this.setToilets(toilets);
        this.marker = new MarkerOptions().position(new LatLng(latlng.first, latlng.second)).icon(BitmapDescriptorFactory.fromResource(R.drawable.toiletunknown)).title(this.getId().toString());

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Pair<Double, Double> getLatLng() {
        return latlng;
    }

    public void setLatLng(Pair<Double, Double> latlng) {
        this.latlng = latlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Toilet> getToilets() {
        return toilets;
    }

    public void setToilets(Map<String, Toilet> toilets) {
        this.toilets = toilets;
    }

    public MarkerOptions getMarker() {
        return marker;
    }

    public boolean isMarkerAdded() {
        return markerAdded;
    }

    public void setMarkerAdded(boolean markerAdded) {
        this.markerAdded = markerAdded;
    }
}
