package com.example.toiletfinder;

import android.os.Handler;
import android.util.Log;

import com.rabbitmq.client.QueueingConsumer;

public class MQConsumer extends IMQConnector
{
	private String queuename;
	private QueueingConsumer subscription;
	private onReceiveMessageHandler handler;
	private Handler messagehandler = new Handler();
	private Handler consumeHandler = new Handler();
	private byte[] lastmessage;
	
	public MQConsumer(String server, String exchange, String exchangetype)
	{
		super(server, exchange, exchangetype);
	}
	
	public interface onReceiveMessageHandler
	{
		public void onReceiveMessage(byte[] message);
	};

	
	final Runnable returnmessage = new Runnable()
	{
		public void run()
		{
			handler.onReceiveMessage(lastmessage);
		}
	};
	
	final Runnable consumerunner = new Runnable()
	{
		public void run()
		{
			consume();
		}
	};
	
	@Override
	public boolean connectToBroker()
	{
		if (super.connectToBroker())
		{
			try
			{
				queuename = model.queueDeclare().getQueue();
				model.queueBind(queuename, exchange, "");
				subscription = new QueueingConsumer(model);
				model.basicConsume(queuename, true, subscription);
			}
			 catch(Exception e)
			{
				Log.d("ToiletFinder","Exception was caught in connectToBroker: "+e);
			}
			
			Running = true;
			consumeHandler.post(consumerunner);
			return true;
		}
		return false;
	}
	
	public void addBinding(String routingkey)
	{
		try
		{
			model.queueBind(queuename, exchange, routingkey);
		}
		catch(Exception e)
		{
			Log.d("ToiletFinder","Exception was caught in addBinding: "+e);
		}
	}
	
	public void removeBinding(String routingkey)
	{
		try
		{
			model.queueUnbind(queuename, exchange, routingkey);
		}
		catch(Exception e)
		{
			Log.d("ToiletFinder","Exception was caught in removeBinding: "+e);
		}
	}
	
	public void setOnReceiveMessageHandler(onReceiveMessageHandler handler)
	{
        this.handler = handler;
    };
    
	private void consume()
	{
		Thread thread = new Thread()
		{
			@Override
			public void run()
			{
				while(Running)
				{
					QueueingConsumer.Delivery delivery;
					try
					{
						delivery = subscription.nextDelivery();
						lastmessage = delivery.getBody();
						messagehandler.post(returnmessage);
						Log.d("ToiletFinder","Consuming: "+returnmessage);
					}
					catch(Exception e)
					{
						Log.d("ToiletFinder","Exception was caught in consume: "+e);
					}
				}
			}
		};
		thread.start();
	}
	
	public void dispose()
	{
		Running = false;
	}
}
