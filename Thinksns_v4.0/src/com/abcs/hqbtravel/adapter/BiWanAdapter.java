package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.hqbtravel.holder.BiWanHoder2;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class BiWanAdapter extends RecyclerArrayAdapter<TouristAttractionsBean>{
    public BiWanAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BiWanHoder2(parent);
    }
}
