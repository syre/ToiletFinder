package com.example.toiletfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by syre on 4/27/14.
 */
public class ToiletGroupInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private LinearLayout ll;
    private View v;
    LayoutInflater inflater;

    public ToiletGroupInfoWindowAdapter(Context context)
    {
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        v = inflater.inflate(R.layout.toiletgroup_infowindow, null);
        LinearLayout ll = (LinearLayout)v.findViewById(R.id.infowindowll);
        TextView text = (TextView)v.findViewById(R.id.title);
        TextView snippet = (TextView)v.findViewById(R.id.snippet);
        text.setText("Title");
        snippet.setText("Snippet");

        return v;
    }
}
