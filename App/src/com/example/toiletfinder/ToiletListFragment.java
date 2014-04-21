package com.example.toiletfinder;

import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ToiletListFragment extends ListFragment
{
	private ArrayAdapter<Toilet> adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		adapter = new ToiletListAdapter(getActivity(), ToiletStorage.getToilets());
		setListAdapter(adapter);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onListItemClick(ListView parent, View view, int position, long id)
	{
		Toilet item = ToiletStorage.getToilets().get(position);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
			    Uri.parse("https://maps.google.com/maps?q=loc:"+item.getLatLng().first+","+item.getLatLng().second));
			startActivity(intent);
		Toast.makeText(getActivity(), item.getName() + " Clicked!" , Toast.LENGTH_SHORT).show();
	}
}
