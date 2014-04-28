package com.example.toiletfinder;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Created by syre on 4/27/14.
 */
public class ToiletMapFragment extends SupportMapFragment
{
    private View view;
    private GoogleMap map;
    private List<Marker> markerlist = new ArrayList<Marker>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.toiletmapfragment, container, false);
        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
    }

    public void setup()
    {
        map.getUiSettings().setRotateGesturesEnabled(false);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(56,10),6);
        map.moveCamera(update);
        map.setInfoWindowAdapter(new ToiletGroupInfoWindowAdapter(getActivity()));
        for (ToiletGroup tg : ToiletStorage.getToiletGroups())
        {
            double lat = tg.getLatLng().first;
            double lng = tg.getLatLng().second;
            Marker mark = map.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet)));

            markerlist.add(mark);

        }
    }
}
