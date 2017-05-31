package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.Adults;
import com.abcs.hqbtravel.holder.ShiBaJiaHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class ShiBaJiaAdapter extends RecyclerArrayAdapter<Adults>{
    public ShiBaJiaAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShiBaJiaHoder(parent);
    }
}
