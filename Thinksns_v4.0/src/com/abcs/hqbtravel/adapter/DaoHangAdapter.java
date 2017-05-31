package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.DaoHang;
import com.abcs.hqbtravel.holder.DaoHangHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class DaoHangAdapter extends RecyclerArrayAdapter<DaoHang.BodyEntity.ItemsEntity>{
    public DaoHangAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DaoHangHoder(parent);
    }
}
