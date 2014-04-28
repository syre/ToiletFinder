package com.example.toiletfinder;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syre on 4/27/14.
 */
public class ToiletPagerAdapter extends FragmentPagerAdapter
{
    final int PAGE_COUNT = 2;
    public ToiletPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new ToiletListFragment();
            case 1:
                return new ToiletMapFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }
}
