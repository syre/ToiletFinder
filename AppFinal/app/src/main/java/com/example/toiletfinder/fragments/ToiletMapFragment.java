package com.example.toiletfinder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.toiletfinder.R;
import com.example.toiletfinder.data.ToiletController;
import com.example.toiletfinder.model.ToiletGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ToiletMapFragment extends SupportMapFragment implements GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    private List<Marker> markerlist = new ArrayList<Marker>();


    // Container Activity must implement this interface
    public interface ToiletMapInterface {
        public void updateMap();
    }

    public void updateMap() {
        if (map!=null) {
            map.getUiSettings().setRotateGesturesEnabled(false);
            int changes = 0;
            double latCenter = 0;
            double lngCenter = 0;
            for (ToiletGroup tg : ToiletController.getToiletGroups().values()) {
                if (changes == 0) {
                    latCenter = 0;
                    lngCenter = 0;
                }
                changes++;
                double lat = tg.getLatLng().first;
                double lng = tg.getLatLng().second;
                latCenter += lat;
                lngCenter += lng;
                Marker mark = map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet)).title(tg.getId().toString()));
                markerlist.add(mark);
            }
            if (changes > 0) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latCenter / changes, lngCenter / lngCenter), 19));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.toiletmapfragment, container, false);
        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
    }

    public void setup() {
        map.getUiSettings().setRotateGesturesEnabled(false);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(56, 10), 6);
        map.moveCamera(update);
        map.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer id = Integer.parseInt(marker.getTitle());
        Toast.makeText(getActivity(), "now subscribed to toiletgroup: " + id, Toast.LENGTH_SHORT).show();
        return true;
    }
}
