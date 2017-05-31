package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.local.beans.RestauDetisl;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class RestaurenAdapter extends RecyclerArrayAdapter<RestauDetisl.MsgBean>{
    public RestaurenAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new RestaurenHoder(parent);
    }
}
