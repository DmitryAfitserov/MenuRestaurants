package com.example.developer.test2.RecyclerViewAdaptersAndHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.developer.test2.R;

/**
 * Created by developer on 2/27/17.
 */

public class ViewHolderNameItems extends RecyclerView.ViewHolder {
    public TextView mTextView;

    public ViewHolderNameItems(View v) {
        super(v);

        mTextView = (TextView) v.findViewById(R.id.vh_text_name_items);
    }

}
