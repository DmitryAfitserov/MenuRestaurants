package com.example.developer.test2;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developer.test2.fragments.FavoritList;

/**
 * Created by developer on 2/23/17.
 */

public class ViewPagerFragment extends Fragment{


    protected ViewPager viewPager;
    private Toolbar toolbar;
    private Drawable restaurantsToolbarDrawable;
    private Drawable favoriteToolbarDrawable;
    private ViewPagerAdapter adapter;
    private Drawable restaurantsTab;
    private Drawable favoriteTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.viewpager, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.clearAnimation();
        tabLayout.setSelectedTabIndicatorHeight(0);

        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

        final TabLayout.Tab tab0 = tabLayout.getTabAt(0);
        tab0.setCustomView(getTabView(0));
        final TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        tab1.setCustomView(getTabView(1));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    if(restaurantsToolbarDrawable == null){
                        restaurantsToolbarDrawable = ResourcesCompat.getDrawable(getResources(),
                                R.drawable.maintool, null);
                    }
                    toolbar.setBackground(restaurantsToolbarDrawable);
                    reverse(tab0, tab1, false);

                }
                if(position == 1){

                    if(favoriteToolbarDrawable == null){
                        favoriteToolbarDrawable = ResourcesCompat.getDrawable(getResources(),
                                R.drawable.favtool, null);
                    }
                    reverse(tab0, tab1, true);
                    toolbar.setBackground(favoriteToolbarDrawable);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        AsyncTaskFirstData asyncTaskFirstData = new AsyncTaskFirstData(getContext());
        asyncTaskFirstData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return rootView;
    }

    public boolean onBackPressed() {

        BackPressInterface currentFragment = (BackPressInterface) adapter.getRegisteredFragment(viewPager.getCurrentItem());
        if (currentFragment != null) {

            return currentFragment.onBackPressed();
        }
        return false;
    }

    private View getTabView(int position) {
        View tab = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);

        TextView tv = (TextView) tab.findViewById(R.id.tab_text);
        ImageView iv = (ImageView)tab.findViewById(R.id.tab_image);
        if(position == 0){
            restaurantsTab = ResourcesCompat.getDrawable(getResources(), R.drawable.restaurant, null);
            restaurantsTab.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
            tv.setTextColor(Color.BLUE);
            tv.setText(R.string.restaurants);
            iv.setImageDrawable(restaurantsTab);
        } else {
            favoriteTab = ResourcesCompat.getDrawable(getResources(), R.drawable.star, null);
            favoriteTab.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            tv.setTextColor(Color.GRAY);
            tv.setText(R.string.favorite);
            iv.setImageDrawable(favoriteTab);
        }
        return tab;
    }
    private void reverse(TabLayout.Tab tabRes, TabLayout.Tab tabFav, boolean isResToFav){
        TextView tvRes = (TextView) tabRes.getCustomView().findViewById(R.id.tab_text);
        TextView tvFav = (TextView) tabFav.getCustomView().findViewById(R.id.tab_text);
        if(isResToFav){

            tvRes.setTextColor(Color.GRAY);
            tvFav.setTextColor(Color.BLUE);
            restaurantsTab.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            favoriteTab.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        } else {
            tvRes.setTextColor(Color.BLUE);
            tvFav.setTextColor(Color.GRAY);
            restaurantsTab.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
            favoriteTab.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        }
    }

}
