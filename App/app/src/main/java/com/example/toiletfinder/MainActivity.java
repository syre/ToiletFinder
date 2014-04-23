package com.example.toiletfinder;

import com.example.toiletfinder.MQConsumer.onReceiveMessageHandler;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
public class MainActivity extends Activity {

	private MQConsumer consumer;
	private ToiletListFragment toiletlistfragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
        {
        	toiletlistfragment = new ToiletListFragment();
        	getFragmentManager().beginTransaction().add(R.id.toiletlistframe, toiletlistfragment).commit();
        }
    	
    }

    public void onFetchButtonClick(View v)
    {
    	Toast.makeText(this, "connecting to broker!", Toast.LENGTH_SHORT).show();
    	Log.d("ToiletFinder", "Connecting to broker");
    	consumer = new MQConsumer(RESTHelper.SERVERHOSTNAME, "logs", "fanout");
        consumer.connectToBroker();
        consumer.setOnReceiveMessageHandler(new onReceiveMessageHandler()
        {
        	public void onReceiveMessage(byte[] message)
        	{
        		String text = "";
                try {
                    text = new String(message, "UTF8");
                } catch (Exception e) {
                    System.out.print("Exception caught in onReceiveMessage: "+e);
                }
                
					Log.d("ToiletFinder", text);
					ToiletStorage.updateToilet(text);
					toiletlistfragment.getListView().invalidateViews();
        	}
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	
    }
    
    @Override
    protected void onPause()
    {
    	super.onPause();
    	if (consumer != null)
    		consumer.dispose();
    }
    
}
