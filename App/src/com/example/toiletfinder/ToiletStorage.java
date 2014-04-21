package com.example.toiletfinder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;

public class ToiletStorage
{
	private static Toilet findToilet(Integer id)
	{
		for (Toilet t : toilets)
			if (t.getId() == id)
				return t;
		return null;
	}
	
	@SuppressWarnings("serial")
	private static List<Toilet> toilets = new ArrayList<Toilet>() {{
			add(new Toilet(0, "Toilet 1", "Til højre for springvandet", false, 23, new Pair<Double, Double>(56.130561, 9.546643)));
			add(new Toilet(1, "Toilet 2", "til venstre for springvandet", true, 70, new Pair<Double, Double>(55.785955, 12.524138) ));	}};
	
	public static List<Toilet> getToilets()
	{
		return toilets;
	}
	
	public static Boolean updateToilet(String message)
	{
		try
		{
			JSONObject object = new JSONObject(message);
			Integer id = (Integer)object.get("id");
			Toilet toilet = toilets.get(toilets.indexOf(findToilet(id)));
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
	
}
