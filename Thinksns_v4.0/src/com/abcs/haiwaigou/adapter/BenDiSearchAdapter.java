package com.abcs.haiwaigou.adapter;


import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.adapter.viewholder.BenDiSearchHoder;
import com.abcs.haiwaigou.model.BenDiSearch;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class BenDiSearchAdapter extends RecyclerArrayAdapter<BenDiSearch.DatasBean> {
    public BenDiSearchAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BenDiSearchHoder(parent);
    }
}
