package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.ShopBean;
import com.abcs.hqbtravel.holder.MoreFuJinMaiHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class MoreFuJinMaiAdapter extends RecyclerArrayAdapter<ShopBean>{
    public MoreFuJinMaiAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoreFuJinMaiHoder(parent);
    }
}
