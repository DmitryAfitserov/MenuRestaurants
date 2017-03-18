package com.example.developer.test2.RecyclerViewAdaptersAndHolders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.developer.test2.databinding.ItemSearchBinding;

/**
 * Created by developer on 2/27/17.
 */

public class ViewHolderSearch extends RecyclerView.ViewHolder {


    public ItemSearchBinding binding;

    public ViewHolderSearch(View v) {
        super(v);
        binding = DataBindingUtil.bind(v);

    }
}
