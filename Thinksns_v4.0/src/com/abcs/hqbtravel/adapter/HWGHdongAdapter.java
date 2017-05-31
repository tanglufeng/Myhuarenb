package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.model.find.QiangGou;
import com.abcs.hqbtravel.holder.HWGHDongHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class HWGHdongAdapter extends RecyclerArrayAdapter<QiangGou.DatasEntry.GoodsListEntry>{
    public HWGHdongAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HWGHDongHoder(parent);
    }
}
