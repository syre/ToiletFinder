package com.example.toiletfinder;

import android.util.Pair;

import java.util.List;

/**
 * Created by syre on 4/27/14.
 */
public class ToiletGroup
{
    private Integer id;
    private Pair<Double, Double> latlng;
    private String name;
    private List<Toilet> toilets;

    public ToiletGroup(Integer id, Pair<Double, Double> latlng, String name, List<Toilet> toilets)
    {
        this.setId(id);
        this.setLatLng(latlng);
        this.setName(name);
        this.setToilets(toilets);
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public Pair<Double, Double> getLatLng()
    {
        return latlng;
    }

    public void setLatLng(Pair<Double, Double> latlng)
    {
        this.latlng = latlng;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Toilet> getToilets()
    {
        return toilets;
    }

    public void setToilets(List<Toilet> toilets)
    {
        this.toilets = toilets;
    }
}
