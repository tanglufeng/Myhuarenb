package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.hqbtravel.holder.FuJinWanHoder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class FuJinWanAdapter extends RecyclerArrayAdapter<TouristAttractionsBean>{
    public FuJinWanAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FuJinWanHoder(parent);
    }
}
