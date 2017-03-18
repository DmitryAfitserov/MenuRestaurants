package com.example.developer.test2.fragments;
import com.example.developer.test2.BackPressInterface;
import com.example.developer.test2.R;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.developer.test2.RecyclerViewAdaptersAndHolders.RecyclerViewAdapterRestaurants;
import com.example.developer.test2.databinding.RecyclerviewWithSrcollBinding;
import com.example.developer.test2.modules.AlfabetHelper;
import com.viethoa.RecyclerViewFastScroller;
import com.viethoa.models.AlphabetItem;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by developer on 2/24/17.
 */

public class RestaurantsList extends Fragment implements BackPressInterface {


    private List<String> mDataArray;
    private List<AlphabetItem> mAlphabetItems;
    RecyclerView recyclerView;
    RecyclerViewFastScroller fastScroller;
    Toolbar toolbar;
    ViewPager viewPager;
    DishesList dishesList;
    RecyclerviewWithSrcollBinding binding;

    public RestaurantsList() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.recyclerview_with_srcoll, container, false);

        View view = binding.getRoot();

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        editToolbar();

        recyclerView = binding.rvRecyclerViewScroll;
        recyclerView.setHasFixedSize(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar();
        fastScroller = binding.fastScroller;

        initialiseData();
        initialiseUI();
        viewPager= (ViewPager)container;
        viewPagerListener();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return view;
    }

    protected void initialiseData() {
        mDataArray = AlfabetHelper.getAlphabetData();

        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < mDataArray.size(); i++) {
            String name = mDataArray.get(i);
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
    }

    protected void initialiseUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerViewAdapterRestaurants adapter = new RecyclerViewAdapterRestaurants(true);
        recyclerView.swapAdapter(adapter, false);;

        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setUpAlphabet(mAlphabetItems);

        Observable<String> ob =  adapter.getPositionClicks();
        ob.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                createListOfDishes(s);
            }
        });
    }

    private void createListOfDishes(String idRestaurant){
        dishesList = new DishesList();
        dishesList.setData(true, idRestaurant);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.addToBackStack(null);
        transaction.replace(R.id.layout_rw_scroll, dishesList).commit();

    }
    private void viewPagerListener(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    if(getChildFragmentManager().getBackStackEntryCount() == 0){
                        editToolbar();
                    } else dishesList.manageToolbar();

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if(getChildFragmentManager().getBackStackEntryCount() == 0) {
            return false;
        }
            BackPressInterface childFragment = (BackPressInterface) getChildFragmentManager().getFragments().get(0);
        if(!childFragment.onBackPressed()){
            getChildFragmentManager().popBackStackImmediate();
            editToolbar();
            return true;
        }
        return true;
    }

    private void editToolbar(){
        toolbar.getMenu().clear();
        toolbar.setNavigationIcon(null);
        toolbar.setTitle(R.string.restaurants);
        toolbar.setTitleTextColor(Color.BLACK);
    }



}



