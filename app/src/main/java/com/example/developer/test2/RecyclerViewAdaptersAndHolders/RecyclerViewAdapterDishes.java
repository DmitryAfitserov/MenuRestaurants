package com.example.developer.test2.RecyclerViewAdaptersAndHolders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.developer.test2.R;
import com.example.developer.test2.databinding.ItemDishesListBinding;
import com.example.developer.test2.model.Dishes;
import java.util.ArrayList;
import java.util.List;
    import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by developer on 2/24/17.
 */

public class RecyclerViewAdapterDishes extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Dishes> mDataArray;
    private final PublishSubject<String> onClickSubject = PublishSubject.create();
    private int NAME_ITEMS_HOLDER = 1;
    private int ITEM_HOLDER = 2;

    private List<Integer> listChanged = new ArrayList<>();

    public RecyclerViewAdapterDishes(boolean e, List mDataArray) {
        this.mDataArray =  mDataArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_HOLDER){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemDishesListBinding binding = ItemDishesListBinding.inflate(inflater, parent, false);
            RecyclerView.ViewHolder vh =
                    new ViewHolderDishesItem(binding.getRoot());
            return vh;
        }
        if(viewType == NAME_ITEMS_HOLDER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name_items,
                    parent, false);
            ViewHolderNameItems vh = new ViewHolderNameItems(v);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ViewHolderDishesItem){
            ((ViewHolderDishesItem) holder).binding.setDish(mDataArray.get(listChanged.get(position)));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onClickSubject.onNext(mDataArray.get(listChanged.get(position)).getId());
                }
            });
        } else  {
            ViewHolderNameItems myHolder = (ViewHolderNameItems)holder;
            myHolder.mTextView.setText(mDataArray.get(listChanged.get(position+1)).getNameCategory());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(!mDataArray.isEmpty()){

            if(listChanged.get(position)== -1){
                return NAME_ITEMS_HOLDER;
            } else return ITEM_HOLDER;
        }
        return 0;
    }

    public Observable<String> getPositionClicks(){
        return onClickSubject.asObservable();
    }

    @Override
    public int getItemCount() {
        if(mDataArray.isEmpty()){
            return 0;
        } else {
            listChanged.clear();
            listChanged.add(-1);
            listChanged.add(0);
            for(int i = 1; i< mDataArray.size(); i++){
                if(!mDataArray.get(i-1).getNameCategory().equals(mDataArray.get(i).getNameCategory())){

                    listChanged.add(-1);
                    listChanged.add(i);

                } else {
                    listChanged.add(i);
                }

            }
            return listChanged.size();
        }
    }

}
