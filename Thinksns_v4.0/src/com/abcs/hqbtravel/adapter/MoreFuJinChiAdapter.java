package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.RestauransBean;
import com.abcs.hqbtravel.holder.MoreFuJinChiHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class MoreFuJinChiAdapter extends RecyclerArrayAdapter<RestauransBean>{
    public MoreFuJinChiAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoreFuJinChiHoder(parent);
    }
}
