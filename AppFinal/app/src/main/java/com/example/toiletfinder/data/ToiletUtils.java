package com.example.toiletfinder.data;

import android.util.Pair;

import com.example.toiletfinder.model.Toilet;
import com.example.toiletfinder.model.ToiletGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ToiletUtils {

    public static Toilet parseToiletJSon(String requestResult) {
        Toilet toilet = null;
        try {
            final JSONObject object = new JSONObject(requestResult);
            final Integer group_id = (Integer) object.get("group_id");
            final Integer id = (Integer) object.get("id");
            toilet = new Toilet(id, group_id);
            if (object.get("occupied") != null) {
                Integer integer = (Integer) object.get("occupied");
                toilet.setOccupied(integer != null && integer < 1);
            }
            if (object.get("methane_level") != null)
                toilet.setMethane_level((Integer) object.get("methane_level"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.print("Exception in ToiletController, JSONObject conversion: " + e);
        }
        return toilet;
    }

    public static ToiletGroup parseToiletGroupJSon(String requestResult) {
        ToiletGroup toiletGroup = null;
        final Map<String, Toilet> toilets = new HashMap<String, Toilet>();
        try {
            JSONObject obj = new JSONObject(requestResult);
            JSONArray toiletgroup = obj.getJSONArray("groups");
            for (int i = 0; i < toiletgroup.length(); i++) {
                Integer id = Integer.parseInt(toiletgroup.getJSONObject(i).getString("id"));
                Double lat = Double.parseDouble(toiletgroup.getJSONObject(i).getString("lat"));
                Double lng = Double.parseDouble(toiletgroup.getJSONObject(i).getString("lng"));
                String name = (toiletgroup.getJSONObject(i).getString("name"));
                JSONArray toiletsArray = toiletgroup.getJSONObject(i).getJSONArray("toilets");
                for (int j = 0; j < toiletsArray.length(); j++) {
                    Integer toilet_id = Integer.parseInt(toiletsArray.getJSONObject(j).getString("id"));
                    toilets.put(toilet_id.toString(), getToilet(toiletsArray.getJSONObject(j)));
                }
                toiletGroup = new ToiletGroup(id, new Pair<Double, Double>(lat, lng), name, toilets);
            }
//            Log.d("ToiletFinder", toiletgroup.toString());
            return toiletGroup;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    private static Toilet getToilet(JSONObject jsonObject) {
        try {
            Integer toilet_id = Integer.parseInt(jsonObject.getString("id"));
            String description = jsonObject.getString("description");
            String location = jsonObject.getString("location");
            Boolean occupied = (Integer.parseInt(jsonObject.getString("occupied")) == 1);
            Integer methane_level = (Integer.parseInt(jsonObject.getString("methane_level")));
            Double toilet_lat = Double.parseDouble(jsonObject.getString("lat"));
            Double toilet_lng = Double.parseDouble(jsonObject.getString("lng"));
            return new Toilet(toilet_id, toilet_id, description, location, occupied, methane_level, new Pair<Double, Double>(toilet_lat, toilet_lng));
        } catch (Exception e) {

        }
        return null;
    }

    public static boolean setMarker(GoogleMap googleMap, Toilet toilet) {
        if (!toilet.isMarkerSet()) {
            toilet.setUpMarkers(googleMap);
        } else {
            toilet.markerEvaluate();
        }
        return true;
    }

    public static boolean setMarker(GoogleMap googleMap, ToiletGroup toiletGroup) {
        if (!toiletGroup.isMarkerAdded()) {
            googleMap.addMarker(toiletGroup.getMarker());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(toiletGroup.getLatLng().first, toiletGroup.getLatLng().second), 19);
            googleMap.moveCamera(update);
            toiletGroup.setMarkerAdded(true);
        }
        return true;
    }
}
