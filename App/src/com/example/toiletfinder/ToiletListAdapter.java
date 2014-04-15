package com.example.toiletfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;
import java.util.List;

public class ToiletListAdapter extends ArrayAdapter 
{ 
	private Context context;
	
	public ToiletListAdapter(Context context, List items) 
	{ 
		super(context, android.R.layout.simple_list_item_1, items);
		this.context = context; 
	} 
	
	/** * Holder for the list items. */ 
	private class ViewHolder
	{ 
		TextView titleText;
		TextView occupied;
		ProgressBar methane_level;
	} 

	public View getView(int position, View convertView, ViewGroup parent)
	{ 
		ViewHolder holder = null;
		Toilet item = (Toilet)getItem(position);
		View viewToUse = null;
		// This block exists to inflate the settings list item conditionally based on whether
		// we want to support a grid or list view.
		LayoutInflater mInflater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) 
		{ 
			viewToUse = mInflater.inflate(R.layout.toilet_list_item, null);
			holder = new ViewHolder();
			holder.titleText = (TextView)viewToUse.findViewById(R.id.titleTextView);
			holder.occupied = (TextView)viewToUse.findViewById(R.id.occupiedTextView);
			holder.methane_level = (ProgressBar)viewToUse.findViewById(R.id.methane_level_bar);
			viewToUse.setTag(holder);
		} 
		else 
		{
			viewToUse = convertView; 
			holder = (ViewHolder) viewToUse.getTag();
		} 
		
		holder.titleText.setText(item.getName());
		holder.methane_level.setProgress(item.getMethane_level());
		holder.methane_level.setIndeterminate(false);
		holder.occupied.setText(item.getOccupied().toString());
		return viewToUse; 
	}
}