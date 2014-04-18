package com.example.toiletfinder;

import android.content.res.Resources;

public class Toilet
{
	private Integer id;
	private String name;
	private String location;
	private Boolean occupied;
	private Integer methane_level;
	
	
	public Toilet(Integer id, String name, String location, Boolean occupied, Integer methane_level)
	{ 
		this.setId(id);
		this.setName(name);
		this.setLocation(location);
		this.setOccupied(occupied);
		this.setMethane_level(methane_level);
	}


	public Integer getMethane_level()
	{
		return methane_level;
	}


	public void setMethane_level(Integer methane_level)
	{
		if (methane_level <= 100)
			this.methane_level = methane_level;
	}


	public Boolean getOccupied()
	{
		return occupied;
	}

	public void setOccupied(Boolean occupied)
	{
		this.occupied = occupied;
	}


	public String getLocation()
	{
		return location;
	}


	public void setLocation(String location)
	{
		this.location = location;
	}


	public Integer getId()
	{
		return id;
	}


	public void setId(Integer id)
	{
		this.id = id;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}
	
}
