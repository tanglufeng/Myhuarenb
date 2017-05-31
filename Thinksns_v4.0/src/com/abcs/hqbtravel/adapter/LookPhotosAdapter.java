package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.LookPhotos;
import com.abcs.hqbtravel.holder.LookPhotosHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class LookPhotosAdapter extends RecyclerArrayAdapter<LookPhotos.BodyBean>{
    public LookPhotosAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new LookPhotosHoder(parent);
    }
}
