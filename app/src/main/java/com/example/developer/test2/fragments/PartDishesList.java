package com.example.developer.test2.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.developer.test2.AsyncTaskOther;
import com.example.developer.test2.CallBackAsyncTask;
import com.example.developer.test2.R;
import com.example.developer.test2.RecyclerViewAdaptersAndHolders.RecyclerViewAdapterPartDishes;
import com.example.developer.test2.databinding.RecyclerviewBinding;
import com.example.developer.test2.model.PartDish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 2/24/17.
 */

public class PartDishesList extends Fragment implements CallBackAsyncTask {

    RecyclerView recyclerView;
    RecyclerViewAdapterPartDishes adapter;
    private Toolbar toolbar;
    String idDish;
    List<PartDish> mDataArray;
    private RecyclerviewBinding binding;

    public PartDishesList() {
    }

    public void setData(String idDish){
        this.idDish = idDish;
        mDataArray = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AsyncTaskOther asyncTaskOther = new AsyncTaskOther(this, mDataArray, idDish, false);
        asyncTaskOther.execute();

        binding = DataBindingUtil.inflate(inflater, R.layout.recyclerview, container, false);
        View view = binding.getRoot();

        recyclerView = binding.rvRecyclerView;
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RecyclerViewAdapterPartDishes(mDataArray);
        recyclerView.setAdapter(adapter);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        editToolBar();

        return view;
    }

    public void editToolBar(){

        toolbar.getMenu().clear();
        toolbar.setTitle("");

        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.icon_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EEE", "back to dishes");
                getActivity().onBackPressed();

            }

            });



    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }
}
