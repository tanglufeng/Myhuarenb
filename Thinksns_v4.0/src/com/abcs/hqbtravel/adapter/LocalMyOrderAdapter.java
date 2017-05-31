package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.model.HuoHMyOrder;
import com.abcs.hqbtravel.holder.LocalMyOrderHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class LocalMyOrderAdapter extends RecyclerArrayAdapter<HuoHMyOrder.DatasEntry>{
    public LocalMyOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocalMyOrderHoder(parent);
    }
}
