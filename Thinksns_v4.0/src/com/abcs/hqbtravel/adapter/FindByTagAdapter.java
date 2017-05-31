package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.FindByTag;
import com.abcs.hqbtravel.holder.FindBytagHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class FindByTagAdapter extends RecyclerArrayAdapter<FindByTag.BodyEntity.ListEntity>{
    public FindByTagAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FindBytagHoder(parent);
    }
}
