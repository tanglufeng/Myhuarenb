package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.ZhuLi;
import com.abcs.hqbtravel.holder.ZhuLiHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class ZhuLiAdapter extends RecyclerArrayAdapter<ZhuLi.BodyEntity.ServicesEntity>{
    public ZhuLiAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ZhuLiHoder(parent);
    }
}
