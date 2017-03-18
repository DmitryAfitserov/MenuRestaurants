package com.example.developer.test2.RecyclerViewAdaptersAndHolders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.developer.test2.databinding.ItemDishesListBinding;

/**
 * Created by developer on 2/27/17.
 */

public class ViewHolderDishesItem extends RecyclerView.ViewHolder {

    ItemDishesListBinding binding;

    public ViewHolderDishesItem(View v) {
        super(v);
        binding = DataBindingUtil.bind(v);

    }
}
