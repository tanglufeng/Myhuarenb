package com.abcs.haiwaigou.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.adapter.viewholder.FaXianHoder;
import com.abcs.haiwaigou.model.FaXian;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class FaXianAdapter extends RecyclerArrayAdapter<FaXian.DatasBean>{
    public FaXianAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FaXianHoder(parent);
    }
}
