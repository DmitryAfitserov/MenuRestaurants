package com.example.developer.test2.model;

import android.widget.ImageView;

import com.example.developer.test2.databinding.ItemMainListBinding;

/**
 * Created by developer on 26.02.2017.
 */

public class Restaurants {
    private String nameRestaurant;
    private String nameIcon;
    private int id;

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public String getNameIcon() {
        return nameIcon;
    }

    public void setNameIcon(String nameIcon) {
        this.nameIcon = nameIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
