package com.example.developer.test2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.util.ArraySet;
import android.util.Log;

import com.example.developer.test2.model.Restaurants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by developer on 2/24/17.
 */

public class Singleton {

    private static Singleton singleton;
    private List<Restaurants> listRestaurants;
    private List<Restaurants> listFavorite;
    private Context context;

    private Singleton(){
        listFavorite = new ArrayList<>();
        listRestaurants = new ArrayList<>();
    }

    public static Singleton getState(){
        if(singleton == null){
            return singleton = new Singleton();
        }
        return singleton;
    }

    public List<Restaurants> getListRestaurants() {
        return listRestaurants;
    }


    public List<Restaurants> getListFavorite() {
        return listFavorite;
    }

    public void setItemRestaurant(Restaurants restaurant){
        listRestaurants.add(restaurant);

    }
    public void setItemFavorite(Restaurants restaurant){
        listFavorite.add(restaurant);

    }

    public void createFavoritList(Context context){
        this.context = context;
        SharedPreferences mSharedPreference =   PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = mSharedPreference.getStringSet("set", null);

        if(set == null){
            Log.d("EEE", "pres null");
        } else {
            listFavorite.clear();
            List<Restaurants> listRes = Singleton.getState().getListRestaurants();
            for(Restaurants restaurant: listRes){
                for(String st: set){
                    if(restaurant.getId() == Integer.valueOf(st)){
                        Singleton.getState().setItemFavorite(restaurant);
                    }
                }
            }
        }
    }
    public Set<String> createSet(){
        Set<String> set = new ArraySet<>();
        return set;
    }

    public void addToFavoriteList(int id){
        SharedPreferences mSharedPreference =   PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = mSharedPreference.getStringSet("set", null);
        if(set == null){
            set = createSet();
        }
        set.add(String.valueOf(id));
        SharedPreferences.Editor mEdit1 = mSharedPreference.edit();
        mEdit1.clear();
        mEdit1.putStringSet("set", set);
        listFavorite.clear();
        for(Restaurants restaurant: listRestaurants){
            for(String pos: set){
                if(restaurant.getId() == Integer.valueOf(pos)){

                    listFavorite.add(restaurant);
                }
            }

        }
        mEdit1.commit();
    }

    public void deleteFromFavoriteList(int id, int position){
        SharedPreferences mSharedPreference =   PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = mSharedPreference.getStringSet("set", null);

        if(position != -1){
            set.remove(String.valueOf(listFavorite.get(position).getId()));
            SharedPreferences.Editor mEdit1 = mSharedPreference.edit();
            mEdit1.clear();

            mEdit1.putStringSet("set", set);
            mEdit1.commit();
        }
        if(id != -1){
            set.remove(String.valueOf(id));
            SharedPreferences.Editor mEdit1 = mSharedPreference.edit();
            mEdit1.clear();

            mEdit1.putStringSet("set", set);
            mEdit1.commit();
        }
        listFavorite.clear();
        for(Restaurants restaurant: listRestaurants){
            for(String pos: set){
                if(restaurant.getId() == Integer.valueOf(pos)){

                    listFavorite.add(restaurant);
                }
            }
        }

    }

    public boolean checkInFavorite(int id){
        if(listFavorite.isEmpty()){
            return false;

        } else {
            for(Restaurants restaurant: listFavorite){
                if(restaurant.getId() == id){
                    return true;
                }
            }
            return false;
        }
    }

    public Context getContext() {
        return context;
    }
}
