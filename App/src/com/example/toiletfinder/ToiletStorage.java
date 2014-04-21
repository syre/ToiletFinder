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
	public static final String SERVERHOSTNAME = "http://10.0.2.2";
	
	private static Toilet findToilet(Integer id)
	{
		for (Toilet t : toiletlist)
			if (t.getId() == id)
				return t;
		return null;
	}
	
	@SuppressWarnings("serial")
	private static List<Toilet> toiletlist = new ArrayList<Toilet>();
	
	public static List<Toilet> getToilets()
	{
		return toiletlist;
	}
	
	public static Boolean updateToilet(String message)
	{
		try
		{
			JSONObject object = new JSONObject(message);
			Integer id = (Integer)object.get("id");
			Toilet toilet = toiletlist.get(toiletlist.indexOf(findToilet(id)));
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
	
	private static class HttpGetTask extends AsyncTask<Void,Void,String>
	{

		@Override
		protected String doInBackground(Void... params)
		{
			return retrieveToiletsFromREST();
		}
		
	}
	private static String retrieveToiletsFromREST()
	{
		String port = "5000";
		String rest_url = SERVERHOSTNAME+":"+port+"/toilets/";
		HttpURLConnection connection = null;
		String requeststring = null;
		try
		{
			URL url = new URL(rest_url);
			connection = (HttpURLConnection) url.openConnection();
			InputStream input = new BufferedInputStream(connection.getInputStream());
			requeststring = IOUtils.toString(input);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (connection != null)
				connection.disconnect();
		}
		Log.d("ToiletFinder", requeststring);
		return requeststring;
	}
	
	public static Boolean fetchToilets()
	{
		AsyncTask<Void, Void, String> result = new HttpGetTask().execute();

		try
		{
			String request = result.get();
			JSONObject obj = new JSONObject(request);
			JSONArray toilets = obj.getJSONArray("results");
			for (int i = 0; i < toilets.length(); i++)
			{
				Integer id = Integer.parseInt(toilets.getJSONObject(i).getString("id"));
				String description = toilets.getJSONObject(i).getString("description");
				String location = toilets.getJSONObject(i).getString("location");
				Boolean occupied = (Integer.parseInt(toilets.getJSONObject(i).getString("occupied")) == 1 ? true : false);
				Integer methane_level = (Integer.parseInt(toilets.getJSONObject(i).getString("methane_level")));
				Double lat = Double.parseDouble(toilets.getJSONObject(i).getString("lat"));
				Double lng = Double.parseDouble(toilets.getJSONObject(i).getString("lng"));
				toiletlist.add(new Toilet(id, description, location, occupied, methane_level, new Pair<Double, Double>(lat, lng)));
			}
			Log.d("ToiletFinder", toiletlist.toString());
			return true;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
}
