package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.DaoHang2;
import com.abcs.hqbtravel.holder.DaoHangHoder2;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class DaoHangAdapter2 extends RecyclerArrayAdapter<DaoHang2.BodyEntity.ItemsEntity> {
    public DaoHangAdapter2(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DaoHangHoder2(parent);
    }
}
