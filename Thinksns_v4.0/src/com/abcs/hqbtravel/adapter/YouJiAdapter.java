package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.YouJi;
import com.abcs.hqbtravel.holder.YouJiHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class YouJiAdapter extends RecyclerArrayAdapter<YouJi.BodyEntity.ItemsEntity>{
    public YouJiAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new YouJiHoder(parent);
    }
}
