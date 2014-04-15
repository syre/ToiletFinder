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
	private List<Toilet> toiletListItemList;
	private ArrayAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		toiletListItemList = new ArrayList<Toilet>();
		toiletListItemList.add(new Toilet(0, "Toilet 1", "Til højre for springvandet", false, 23));
		toiletListItemList.add(new Toilet(1, "Toilet 2", "til venstre for springvandet", true, 70 ));
		toiletListItemList.add(new Toilet(2, "Toilet 3", "Til højre for dammen", true, 99));
		adapter = new ToiletListAdapter(getActivity(), toiletListItemList);
		setListAdapter(adapter);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onListItemClick(ListView parent, View view, int position, long id)
	{
		Toilet item = this.toiletListItemList.get(position);
		Toast.makeText(getActivity(), item.getName() + " Clicked!" , Toast.LENGTH_SHORT).show();
	}
}
