package com.abcs.hqbtravel.adapter;


import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.DaohangDetials2;
import com.abcs.hqbtravel.entity.SanBiDetialsPic;
import com.abcs.hqbtravel.holder.DHangDetials2Hoder;
import com.abcs.hqbtravel.holder.SanBiDetialsHoder;
import com.abcs.hqbtravel.view.activity.SanBiDetialsPicActivity;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class SanBiDetialsAdapter extends RecyclerArrayAdapter<SanBiDetialsPic.BodyBean.CommentItem> {
    public SanBiDetialsAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new SanBiDetialsHoder(parent);
    }

}
