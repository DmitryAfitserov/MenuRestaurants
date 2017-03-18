package com.example.developer.test2.modules;

import com.example.developer.test2.model.Restaurants;
import com.example.developer.test2.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 2/24/17.
 */

public class AlfabetHelper {

    public static List<String> getAlphabetData() {
        List<String> list = new ArrayList<String>();
        for(Restaurants restaurants : Singleton.getState().getListRestaurants()){
            list.add(restaurants.getNameRestaurant().substring(0,1) + " item 1");
        }
        return list;
    }
}
