package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.haiwaigou.local.beans.NewHireFind;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class NewHireJobAdapter extends RecyclerArrayAdapter<NewHireFind.MsgBean>{
    public NewHireJobAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewHireJobHoder(parent);
    }
}
