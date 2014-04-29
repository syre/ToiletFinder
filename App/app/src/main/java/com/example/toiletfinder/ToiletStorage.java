package com.example.toiletfinder;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

public class ToiletStorage
{
	
	private static Toilet findToilet(List<Toilet> toilets, Integer id)
	{
		for (Toilet t : toilets)
			if (t.getId() == id)
				return t;
		return null;
	}
	
	@SuppressWarnings("serial")
	private static List<ToiletGroup> toiletgrouplist = new ArrayList<ToiletGroup>();
	
	public static List<ToiletGroup> getToiletGroups()
	{
		return toiletgrouplist;
	}
	
	public static Boolean updateToilet(String message)
	{
		try
		{
			JSONObject object = new JSONObject(message);
            Integer group_id = (Integer)object.get("group_id");
			Integer id = (Integer)object.get("id");
			Toilet toilet = findToilet(toiletgrouplist.get(group_id).getToilets(), id);
			if (object.get("occupied") != null)
				toilet.setOccupied((Boolean)object.get("occupied"));
			if (object.get("methane_level") != null)
				toilet.setMethane_level((Integer)object.get("methane_level"));
			
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			System.out.print("Exception in ToiletStorage, JSONObject conversion: "+e);
		}
		return null;
	}

	public static Boolean fetchToiletGroups(Integer groupid)
	{
		AsyncTask<Integer, Void, String> result = new RESTHelper.ToiletRetrieverTask().execute(groupid);

		try
		{
			String request = result.get();
			JSONObject obj = new JSONObject(request);
			JSONArray toiletgroup = obj.getJSONArray("groups");
			for (int i = 0; i < toiletgroup.length(); i++)
			{
				Integer id = Integer.parseInt(toiletgroup.getJSONObject(i).getString("id"));
                Double lat = Double.parseDouble(toiletgroup.getJSONObject(i).getString("lat"));
                Double lng = Double.parseDouble(toiletgroup.getJSONObject(i).getString("lng"));
                String name = (toiletgroup.getJSONObject(i).getString("name"));
                JSONArray toilets = (toiletgroup.getJSONObject(i).getJSONArray("toilets"));
                List<Toilet> toiletlistforgroup = new ArrayList<Toilet>();
                for (int j = 0; j < toilets.length(); j++)
                {
                    Integer toilet_id = Integer.parseInt(toilets.getJSONObject(i).getString("id"));
                    String description = toilets.getJSONObject(i).getString("description");
				    String location = toilets.getJSONObject(i).getString("location");
				    Boolean occupied = (Integer.parseInt(toilets.getJSONObject(i).getString("occupied")) == 1 ? true : false);
				    Integer methane_level = (Integer.parseInt(toilets.getJSONObject(i).getString("methane_level")));
				    Double toilet_lat = Double.parseDouble(toilets.getJSONObject(i).getString("lat"));
				    Double toilet_lng = Double.parseDouble(toilets.getJSONObject(i).getString("lng"));
				    toiletlistforgroup.add(new Toilet(toilet_id, id, description, location, occupied, methane_level, new Pair<Double, Double>(toilet_lat, toilet_lng)));
                }
                toiletgrouplist.add(new ToiletGroup(id, new Pair<Double, Double>(lat,lng), name, toiletlistforgroup));

            }
			Log.d("ToiletFinder", toiletgroup.toString());
			return true;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
}
