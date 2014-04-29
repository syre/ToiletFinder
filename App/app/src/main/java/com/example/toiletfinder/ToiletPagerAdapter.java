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
    private FragmentManager fragmentmanager;
    private List<Fragment> fragmentlist;
    public ToiletPagerAdapter(FragmentManager fm)
    {
        super(fm);
        fragmentmanager = fm;
        fragmentlist = new ArrayList<Fragment>();
        fragmentlist.add(new ToiletListFragment());
        fragmentlist.add(new ToiletMapFragment());
    }

    @Override
    public Fragment getItem(int position)
    {
        String name = makeFragmentName(R.id.pager, position);
        Fragment f = fragmentmanager.findFragmentByTag(name);
            switch(position)
            {
                case(0):
                    return new ToiletListFragment();
                case(1):
                    return new ToiletMapFragment();
                default:
                    return new ToiletListFragment();
            }
    }

    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
}
