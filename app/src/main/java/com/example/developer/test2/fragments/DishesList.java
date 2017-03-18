package com.example.developer.test2.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.developer.test2.AsyncTaskOther;
import com.example.developer.test2.BackPressInterface;
import com.example.developer.test2.CallBackAsyncTask;
import com.example.developer.test2.R;
import com.example.developer.test2.RecyclerViewAdaptersAndHolders.RecyclerViewAdapterDishes;
import com.example.developer.test2.databinding.RecyclerviewBinding;
import com.example.developer.test2.model.Dishes;
import com.example.developer.test2.Singleton;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by developer on 2/24/17.
 */

public class DishesList extends Fragment implements BackPressInterface, CallBackAsyncTask {

    RecyclerView recyclerView;
    RecyclerViewAdapterDishes adapter;
    private Toolbar toolbar;
    PartDishesList partDishesList;
    Boolean isRestaurants;
    private List<Dishes> mDataArray;
    private String idRestaurant;
    private MediaPlayer mMediaPlayer;
    private RecyclerviewBinding binding;

    public DishesList() {

    }

    public void setData(boolean isRestaurants, String id_restaurant){
        this.isRestaurants = isRestaurants;
        mDataArray = new ArrayList<>();
        this.idRestaurant = id_restaurant;
        Log.d("EEE", "give int " + id_restaurant);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("EEE", "give int as ");
        if(savedInstanceState == null){
            AsyncTaskOther asyncTaskOther = new AsyncTaskOther(this, mDataArray, idRestaurant , true);
            asyncTaskOther.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.recyclerview, container, false);

        View view = binding.getRoot();

        recyclerView = binding.rvRecyclerView;
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RecyclerViewAdapterDishes(true, mDataArray);
        recyclerView.setAdapter(adapter);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        Drawable back = ContextCompat.getDrawable(getActivity(), R.drawable.icon_back);
        back.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        editToolBar();
        createObObservable();
        //recyclerView.getTouchables().get()

        return view;
    }

    private void createObObservable(){
        Observable<String> ob =  adapter.getPositionClicks();
        ob.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                createListOfParts(s);
                Log.d("EEE", "fav " + s);
            }
        });
    }

    private void createListOfParts(String idDish){
        partDishesList = new PartDishesList();
        partDishesList.setData(idDish);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        // Store the Fragment in stack
        transaction.addToBackStack("dishes");
        transaction.replace(R.id.layout_rw, partDishesList).commit();

    }

    @Override
    public boolean onBackPressed() {
        if(getChildFragmentManager().getBackStackEntryCount() == 0) {
            return false;
        }

        getChildFragmentManager().popBackStackImmediate();
        editToolBar();
        return true;


    }

    public void manageToolbar(){
        if(getChildFragmentManager().getBackStackEntryCount() == 0){
            editToolBar();
        } else {
            partDishesList.editToolBar();

        }

    }
    private void editToolBar(){

        toolbar.getMenu().clear();
        toolbar.setTitle("");

        toolbar.inflateMenu(R.menu.add_menu);
        Drawable back =ContextCompat.getDrawable(getActivity(), R.drawable.icon_back);
        back.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.icon_back));
        if(Singleton.getState().checkInFavorite(Integer.valueOf(idRestaurant))){
            toolbar.getMenu().findItem(R.id.add_to_favorit_item).getIcon().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        } else toolbar.getMenu().findItem(R.id.add_to_favorit_item).getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }

        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.add_to_favorit_item) {
                    if(Singleton.getState().checkInFavorite(Integer.valueOf(idRestaurant))){
                        Singleton.getState().deleteFromFavoriteList(Integer.valueOf(idRestaurant), -1);
                        toolbar.getMenu().findItem(R.id.add_to_favorit_item).getIcon()
                                .setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                    } else{
                        if(mMediaPlayer == null){
                            mMediaPlayer = new MediaPlayer();
                        }
                        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.fav);
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.setLooping(false);
                        mMediaPlayer.start();
                        Singleton.getState().addToFavoriteList(Integer.valueOf(idRestaurant));
                        toolbar.getMenu().findItem(R.id.add_to_favorit_item).getIcon()
                                .setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);


                    }

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }
}
