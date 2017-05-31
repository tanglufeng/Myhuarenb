package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.HuiYuanTeDian;
import com.abcs.hqbtravel.holder.TravelTeDianHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class TravelTeDianAdapter extends RecyclerArrayAdapter<HuiYuanTeDian.VipTdListBean>{
    public TravelTeDianAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TravelTeDianHoder(parent);
    }
}
