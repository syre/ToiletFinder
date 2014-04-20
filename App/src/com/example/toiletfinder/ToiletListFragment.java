package com.example.toiletfinder;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
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
		Toast.makeText(getActivity(), item.getName() + " Clicked!" , Toast.LENGTH_SHORT).show();
	}
}
