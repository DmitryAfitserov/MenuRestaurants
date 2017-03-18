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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.developer.test2.RecyclerViewAdaptersAndHolders.RecyclerViewAdapterRestaurants;
import com.example.developer.test2.databinding.RecyclerviewBinding;
import com.example.developer.test2.Singleton;


import rx.Observable;
import rx.functions.Action1;

/**
 * Created by developer on 2/22/17.
 */

public class FavoritList extends Fragment implements BackPressInterface{

    RecyclerView recyclerView;
    RecyclerViewAdapterRestaurants adapter;
    Toolbar toolbar;
    ViewPager viewPager;
    DishesList dishesList;
    boolean isEditState = false;
    int lastEditPosition = -1;
    private RecyclerviewBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.recyclerview, container, false);

        View view = binding.getRoot();

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        recyclerView = binding.rvRecyclerView;
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RecyclerViewAdapterRestaurants(false);
        recyclerView.setAdapter(adapter);
        viewPager = (ViewPager)container;
        viewPagerListener();
        createObObservable();

        return view;
    }

    private void createObObservable(){
        Observable<String> click =  adapter.getPositionClicks();
        click.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if(!isEditState){
                    createListOfDishes(s);
                } else {
                    editItem(false);
                }
            }
        });
        Observable<String> notify =  adapter.getNotifyObservable();
        notify.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if(isEditState){
                    lastEditPosition = -1;
                    doneItem(true);
                    editItemNow();
                }
            }
        });
    }

    private void createListOfDishes(String idRestaurant){
        dishesList = new DishesList();
        dishesList.setData(false, idRestaurant);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.addToBackStack(null);
        transaction.replace(R.id.layout_rw, dishesList).commit();

    }
    private void viewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    adapter.notifyDataSetChanged();
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

    public void editToolbar(){
        toolbar.getMenu().clear();
        toolbar.setNavigationIcon(null);
        toolbar.setTitle(R.string.favorite);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.inflateMenu(R.menu.favorite_menu);
        if(!isEditState){
            toolbar.getMenu().getItem(0).setTitle("Edit");
        } else {
            toolbar.getMenu().getItem(0).setTitle("Done");
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(!isEditState){
                    toolbar.getMenu().getItem(0).setTitle("Done");
                    isEditState = true;
                    editItem(true);

                } else {
                    toolbar.getMenu().getItem(0).setTitle("Edit");
                    doneItem(false);
                    isEditState = false;
                }
                return false;
            }
        });
    }
    public void doneItem(boolean isNow){

        if(isEditState){
            for(int i = 0; i < Singleton.getState().getListFavorite().size(); i++){
                if(isNow){
                    Log.d("EEE", "delete count");
                    toBackBan(i, true);
                    toBackDelete(i, true);
                } else {
                    toBackBan(i, false);
                    toBackDelete(i, false);
                }

            }
            lastEditPosition = -1;
        }

    }

    public void editItemNow(){
        if(isEditState == true){
            for(int i = 0; i < Singleton.getState().getListFavorite().size(); i++){
                toFrontBan(i, true);
            }
        }

    }

    private void editItem(boolean isFirstCall){
        if(isFirstCall){
            for(int i = 0; i < Singleton.getState().getListFavorite().size(); i++){
                toFrontBan(i, false);
            }
        } else {

            if(lastEditPosition != -1){
                toBackDelete(lastEditPosition, false);
                toFrontBan(lastEditPosition, false);

            }
            lastEditPosition = -1;

        }

    }
    private void toFrontBan(final int pos, boolean isNow){
        View view = recyclerView.getLayoutManager().findViewByPosition(pos);
        if(view == null){
            Log.d("EEE", "error ban");
            return;
        }
        View ban = view.findViewById(R.id.ban);
        ban.clearAnimation();
        View rel = view.findViewById(R.id.rellay);
        rel.clearAnimation();
        Animation animBan;
        Animation animRel;
        if(isNow){
            animBan = AnimationUtils.loadAnimation(getContext(), R.anim.ban_in_now);
            animRel = AnimationUtils.loadAnimation(getContext(), R.anim.rel_in_now);
        } else {
            animBan = AnimationUtils.loadAnimation(getContext(), R.anim.ban_in);
            animRel = AnimationUtils.loadAnimation(getContext(), R.anim.rel_in);
        }
        ban.startAnimation(animBan);
        rel.startAnimation(animRel);
        ban.setVisibility(View.VISIBLE);
        ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastEditPosition == -1){
                    toBackBan(pos, false);
                    toFrontDelete(pos);
                    lastEditPosition = pos;
                    Log.d("EEE", "pos start 1" );
                } else if(lastEditPosition != pos) {
                    Log.d("EEE", "pos start 2");
                    toBackDelete(lastEditPosition, false);
                    toFrontBan(lastEditPosition ,false);
                    toBackBan(pos, false);
                    toFrontDelete(pos);
                    lastEditPosition = pos;
                } else if(lastEditPosition == pos){
                    Log.d("EEE", "pos start 3" );
                    toBackDelete(pos,false);
                    toFrontBan(pos, false);
                    lastEditPosition = -1;
                }

            }
        });
    }

    private void toBackBan(int pos, boolean isNow){
        View view = recyclerView.getLayoutManager().findViewByPosition(pos);
        if(view == null){
            return;
        }

        View ban = view.findViewById(R.id.ban);
        ban.clearAnimation();
        ban.setOnClickListener(null);

        if(ban.getVisibility() == View.VISIBLE){
            View rel = view.findViewById(R.id.rellay);
            rel.clearAnimation();
            ban.setOnClickListener(null);
            Animation animBan;
            Animation animRel;
            if(isNow){
                animBan = AnimationUtils.loadAnimation(getContext(), R.anim.ban_out_now);
                ban.startAnimation(animBan);
            } else {
                animBan = AnimationUtils.loadAnimation(getContext(), R.anim.ban_out);
                animRel = AnimationUtils.loadAnimation(getContext(), R.anim.rel_out);
                ban.startAnimation(animBan);
                rel.startAnimation(animRel);
            }


            ban.setVisibility(View.GONE);
        }
    }
    private void toFrontDelete(final int pos){
        View view = recyclerView.getLayoutManager().findViewByPosition(pos);
        final View delete = view.findViewById(R.id.rel_delete);
        delete.clearAnimation();
        delete.setOnClickListener(null);
        Animation animBan = AnimationUtils.loadAnimation(getContext(), R.anim.delete_in);
        delete.startAnimation(animBan);
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getState().deleteFromFavoriteList(-1, pos);
                adapter.notifyDataSetChanged();
                delete.setVisibility(View.GONE);
                delete.setOnClickListener(null);
            }
        });
    }

    private void toBackDelete(int pos, boolean isNow){

        View view = recyclerView.getLayoutManager().findViewByPosition(pos);
        if(view == null){
            return;
        }

        View delete = view.findViewById(R.id.rel_delete);
        delete.clearAnimation();
        if(delete.getVisibility() == View.VISIBLE){
            delete.setOnClickListener(null);
            Animation animBan;
            if(isNow){
                animBan = AnimationUtils.loadAnimation(getContext(), R.anim.delete_out_now);
            } else {
                animBan = AnimationUtils.loadAnimation(getContext(), R.anim.delete_out);
            }

            delete.startAnimation(animBan);
            delete.setVisibility(View.GONE);
        }

    }

}
