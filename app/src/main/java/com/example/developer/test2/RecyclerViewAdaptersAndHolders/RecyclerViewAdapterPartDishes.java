package com.example.developer.test2.RecyclerViewAdaptersAndHolders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.developer.test2.databinding.ItemPartDishesListBinding;
import com.example.developer.test2.model.PartDish;

import java.util.List;

/**
 * Created by developer on 2/24/17.
 */

public class RecyclerViewAdapterPartDishes extends RecyclerView.Adapter<RecyclerViewAdapterPartDishes.MyViewHolder> {

    private List<PartDish> mDataArray;

    public RecyclerViewAdapterPartDishes(List list) {
        this.mDataArray = list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPartDishesListBinding binding = ItemPartDishesListBinding.inflate(inflater, parent, false);
        RecyclerViewAdapterPartDishes.MyViewHolder vh =
                new RecyclerViewAdapterPartDishes.MyViewHolder(binding.getRoot());

        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.binding.setPartdishes(mDataArray.get(position));

    }

    @Override
    public int getItemCount() {
        return mDataArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemPartDishesListBinding binding;

        public MyViewHolder(View v) {
            super(v);

            binding = DataBindingUtil.bind(v);
        }
    }

}
