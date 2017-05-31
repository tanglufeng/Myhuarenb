package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.hqbtravel.holder.MoreFuJinWanHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class MoreFuJinWanAdapter extends RecyclerArrayAdapter<TouristAttractionsBean>{
    public MoreFuJinWanAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoreFuJinWanHoder(parent);
    }
}
