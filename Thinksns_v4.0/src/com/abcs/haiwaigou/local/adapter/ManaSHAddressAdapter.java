package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.local.beans.New;
import com.abcs.haiwaigou.local.viewholder.ManaSHAddressHolder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 *
 */
public class ManaSHAddressAdapter extends RecyclerArrayAdapter<New> {

    public ManaSHAddressAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ManaSHAddressHolder(parent);
    }
}
