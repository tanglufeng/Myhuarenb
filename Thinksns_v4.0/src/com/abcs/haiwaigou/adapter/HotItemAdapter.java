package com.abcs.haiwaigou.adapter;


import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.adapter.viewholder.HotItemHoder;
import com.abcs.haiwaigou.model.HotItem;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class HotItemAdapter extends RecyclerArrayAdapter<HotItem.DatasBean> {
    public HotItemAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotItemHoder(parent);
    }
}
