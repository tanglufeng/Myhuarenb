package com.abcs.haiwaigou.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.adapter.viewholder.HuoDongHoder;
import com.abcs.haiwaigou.model.HuoDong;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class HuoDongAdapter extends RecyclerArrayAdapter<HuoDong.DatasEntry.ChoicenessActivityEntry>{
    public HuoDongAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HuoDongHoder(parent);
    }
}
