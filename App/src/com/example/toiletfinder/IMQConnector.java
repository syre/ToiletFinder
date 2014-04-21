package com.example.toiletfinder;

import android.os.AsyncTask;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class IMQConnector
{
	public String server;
	public String exchange;
	
	protected Channel model = null;
	protected Connection connection;
	
	protected boolean Running;
	
	protected String exchangetype;
	
	public IMQConnector(String server, String exchange, String exchangetype)
	{
		this.server = server;
		this.exchange = exchange;
		this.exchangetype = exchangetype;
	}
	
	private class ConnectToBrokerTask extends AsyncTask<Void,Void,Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{
			if (model != null && model.isOpen())
			{
				Log.d("ToiletFinder", "connection to broker is already open");
				return true;
			}
			try
			{
				Log.d("ToiletFinder", "Opening connection to broker");
				ConnectionFactory connectionfactory = new ConnectionFactory();
				connectionfactory.setHost(server);
				connectionfactory.setUsername("node");
				connectionfactory.setPassword("node");
				connection = connectionfactory.newConnection();
				model = connection.createChannel();
				model.exchangeDeclare(exchange, exchangetype);
				return true;
			}
			catch (Exception e)
			{
				Log.d("ToiletFinder", "error connecting to broker: "+e);
				return false;
			}
		}
	}
	
	public void dispose() throws Exception
	{
		try
		{
			if (connection != null)
			{
				connection.close();
			}
			if (model != null)
			{
				model.abort();
			}
		}
		catch (Exception e)
		{
			System.out.print("Error attempting to close connection: "+e);
		}
	}
	
	public boolean connectToBroker()
	{
		AsyncTask<Void, Void, Boolean> result = new ConnectToBrokerTask().execute();
		try
		{
			return result.get();
		} 
		catch (Exception e)
		{
			System.out.print("Error attempting to retrieve connect result: "+e);
			return false;
		}
		
	}
}
