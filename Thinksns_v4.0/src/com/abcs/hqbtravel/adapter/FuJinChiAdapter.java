package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.RestauransBean;
import com.abcs.hqbtravel.holder.FuJinChiHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class FuJinChiAdapter extends RecyclerArrayAdapter<RestauransBean>{
    public FuJinChiAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FuJinChiHoder(parent);
    }
}
