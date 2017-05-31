package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.local.beans.HWGWanLeDeTials;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class HWGWanLeDetialAdapter extends RecyclerArrayAdapter<HWGWanLeDeTials.GoodsListBean>{
    public HWGWanLeDetialAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HWGWanLeDetialHoder(parent);
    }
}
