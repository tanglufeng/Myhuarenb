package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/4/18.
 */
public class MyRedBagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView t_time;
    public TextView t_price;
    public RelativeLayout relative_root;
    public MyRedBagViewHolder(View itemView) {
        super(itemView);
        t_time = (TextView) itemView.findViewById(R.id.t_time);
        t_price = (TextView) itemView.findViewById(R.id.t_price);
        relative_root= (RelativeLayout) itemView.findViewById(R.id.relative_root);
    }

    @Override
    public void onClick(View v) {
    }
}
