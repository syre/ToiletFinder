package com.example.toiletfinder;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import android.os.AsyncTask;
import android.util.Log;

public class RESTHelper
{
	public static final String SERVERHOSTNAME = "192.168.1.202";
	
	public static class ToiletRetrieverTask extends AsyncTask<Void,Void,String>
	{

		@Override
		protected String doInBackground(Void... params)
		{
			return RESTHelper.retrieveToiletsFromREST();
		}
		
	}
	public static String retrieveToiletsFromREST()
	{
		String port = "5000";
		String protocol = "http://";
		String rest_url = protocol+SERVERHOSTNAME+":"+port+"/toiletgroups/";
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
}
