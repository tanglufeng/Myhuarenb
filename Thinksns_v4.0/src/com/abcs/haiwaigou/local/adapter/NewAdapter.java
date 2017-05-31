package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.local.beans.New;
import com.abcs.haiwaigou.local.viewholder.NewViewHolder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * 更多新闻的adapter
 */
public class NewAdapter extends RecyclerArrayAdapter<New> {

    public NewAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewViewHolder(parent);
    }
}
