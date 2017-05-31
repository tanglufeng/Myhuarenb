package com.abcs.hqbtravel.adapter;


import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.DaohangDetials2;
import com.abcs.hqbtravel.holder.DHangDetials2Hoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class DHangDetials2Adapter extends RecyclerArrayAdapter<DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity> {
    public DHangDetials2Adapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DHangDetials2Hoder(parent);
    }
}
