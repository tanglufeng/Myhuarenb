package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.ShopBean;
import com.abcs.hqbtravel.holder.FuJinMaiHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class FuJinMaiAdapter extends RecyclerArrayAdapter<ShopBean>{
    public FuJinMaiAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FuJinMaiHoder(parent);
    }
}
