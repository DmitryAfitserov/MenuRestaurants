package com.example.developer.test2;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.developer.test2.fragments.FavoritList;
import com.example.developer.test2.fragments.RestaurantsList;

/**
 * Created by developer on 2/23/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {


    private final Resources resources;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


    public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new RestaurantsList();

            case 1:
                return new FavoritList();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
