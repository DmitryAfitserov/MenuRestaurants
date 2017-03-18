package com.example.developer.test2.RecyclerViewAdaptersAndHolders;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.developer.test2.R;
import com.example.developer.test2.databinding.ItemMainListBinding;
import com.example.developer.test2.databinding.ItemMainListWithAnimBinding;
import com.example.developer.test2.databinding.ItemSearchBinding;
import com.example.developer.test2.model.Restaurants;
import com.example.developer.test2.Singleton;
import com.viethoa.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by developer on 2/24/17.
 */

public class RecyclerViewAdapterRestaurants extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter {

    List<Restaurants> mDataArray;
    private final PublishSubject<String> onClickSubject = PublishSubject.create();
    private final PublishSubject<String> onViewAttachedToWindow = PublishSubject.create();
    private boolean isRestaurants;
    private int SEARCH_HOLDER = 0;
    private int NAME_ITEMS_HOLDER = 1;
    private int ITEM_HOLDER_RESTAURANT = 2;
    private int ITEM_HOLDER_FAVORITE = 3;

    public RecyclerViewAdapterRestaurants(boolean isRestaurants) {
        this.isRestaurants = isRestaurants;
        if(isRestaurants){
            mDataArray = new ArrayList<>(Singleton.getState().getListRestaurants());
        } else {
            mDataArray = Singleton.getState().getListFavorite();
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == SEARCH_HOLDER){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemSearchBinding binding = ItemSearchBinding.inflate(inflater, parent, false);
            ViewHolderSearch vh = new ViewHolderSearch(binding.getRoot());
            return vh;
        }
        if(viewType == NAME_ITEMS_HOLDER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name_items, parent, false);
            ViewHolderNameItems vh = new ViewHolderNameItems(v);
            return vh;
        }
        if(viewType == ITEM_HOLDER_RESTAURANT){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemMainListBinding binding = ItemMainListBinding.inflate(inflater, parent, false);
            ViewHolderRestaurantItem vh = new ViewHolderRestaurantItem(binding.getRoot());
            return vh;

        }
        if(viewType == ITEM_HOLDER_FAVORITE){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemMainListWithAnimBinding binding =
                    ItemMainListWithAnimBinding.inflate(inflater, parent, false);
            RecyclerView.ViewHolder vh = new ViewHolderFavorit(binding.getRoot());
            return vh;
        }
        else return null;

    }

    @Override
    public int getItemViewType(int position) {
        if(isRestaurants){
            if(position == 0){
                return SEARCH_HOLDER;
            }
            if(position == 1){
                return NAME_ITEMS_HOLDER;
            }
            if(position > 1){
                return ITEM_HOLDER_RESTAURANT;
            }
        }
        if(!isRestaurants){
            return ITEM_HOLDER_FAVORITE;
        }
        return ITEM_HOLDER_RESTAURANT;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ViewHolderRestaurantItem){
            ViewHolderRestaurantItem myviewHolder = (ViewHolderRestaurantItem)holder;
            myviewHolder.binding.setRestaurant(mDataArray.get(position - 2));

            int point = mDataArray.get(position - 2).getNameIcon().lastIndexOf(".");
            int id = Singleton.getState().getContext().getResources()
                    .getIdentifier(
                            mDataArray.get(position - 2).getNameIcon().substring(0, point)
                            , "drawable",
                            Singleton.getState().getContext().getPackageName());

            myviewHolder.imageView.setImageDrawable(ContextCompat
                    .getDrawable(Singleton.getState().getContext(),id));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id;
                        id = mDataArray.get(position - 2).getId();
                        onClickSubject.onNext(String.valueOf(id));
                }
            });
        }

        if(holder instanceof ViewHolderFavorit){
            ViewHolderFavorit myviewHolder = (ViewHolderFavorit)holder;
            myviewHolder.binding.setRestaurants(mDataArray.get(position));
            int point = mDataArray.get(position).getNameIcon().lastIndexOf(".");
            int id = Singleton.getState().getContext().getResources()
                    .getIdentifier(
                            mDataArray.get(position).getNameIcon().substring(0, point)
                            , "drawable",
                            Singleton.getState().getContext().getPackageName());

            myviewHolder.imageView.setImageDrawable(ContextCompat
                    .getDrawable(Singleton.getState().getContext(),id));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id;
                        id = mDataArray.get(position).getId();

                    onClickSubject.onNext(String.valueOf(id));

                }
            });
        }

        if(holder instanceof ViewHolderNameItems){
            ViewHolderNameItems myviewHolder = (ViewHolderNameItems)holder;
            myviewHolder.mTextView.setText(R.string.choose_restaurants);

        }
        if(holder instanceof ViewHolderSearch){
            final ViewHolderSearch myviewHolder = (ViewHolderSearch)holder;
            myviewHolder.binding.searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!myviewHolder.binding.searchView.isActivated()){
                        myviewHolder.binding.searchView.setIconified(false);

                    }
                }
            });


            myviewHolder.binding.searchView.setOnQueryTextListener(
                    new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.length() == 0){
                        mDataArray.clear();
                        mDataArray.addAll(Singleton.getState().getListRestaurants());

                        notifyDataSetChanged();
                    } else{
                        mDataArray.clear();
                        for(Restaurants restaurant: Singleton.getState().getListRestaurants()){
                            if(restaurant.getNameRestaurant().toLowerCase().contains(newText)){
                                mDataArray.add(restaurant);
                            }
                        }
                        notifyDataSetChanged();

                    }
                    return false;
                }
            });
        }
    }

    public Observable<String> getPositionClicks(){
        return onClickSubject.asObservable();
    }

    public Observable<String> getNotifyObservable(){
        return onViewAttachedToWindow.asObservable();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if(holder instanceof ViewHolderFavorit){

            if(mDataArray.get(mDataArray.size() -1).getId() ==
                    ((ViewHolderFavorit)holder).binding.getRestaurants().getId()){
                onViewAttachedToWindow.onNext(null);
            }

        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        if(isRestaurants){
            return mDataArray.size() + 2;
        } else {
            return mDataArray.size();
        }
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return null;
    }

}
