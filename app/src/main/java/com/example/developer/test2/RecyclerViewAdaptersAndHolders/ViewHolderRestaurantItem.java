package com.example.developer.test2.RecyclerViewAdaptersAndHolders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.developer.test2.R;
import com.example.developer.test2.databinding.ItemMainListBinding;

/**
 * Created by developer on 2/27/17.
 */

public class ViewHolderRestaurantItem extends RecyclerView.ViewHolder {

    public ImageView imageView;
    ItemMainListBinding binding;

    public ViewHolderRestaurantItem(View v) {
        super(v);
        binding = DataBindingUtil.bind(v);

        imageView = (ImageView) v.findViewById(R.id.iv_image);
    }
}
