package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/4/18.
 */
public class RedBagDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView t_name;
    public TextView t_time;
    public TextView t_price;
    public ImageView img_user;
    public ImageView img_my;
    public RedBagDetailViewHolder(View itemView) {
        super(itemView);
        img_user = (ImageView) itemView.findViewById(R.id.img_user);
        img_my = (ImageView) itemView.findViewById(R.id.img_my);
        t_name = (TextView) itemView.findViewById(R.id.t_name);
        t_time = (TextView) itemView.findViewById(R.id.t_time);
        t_price = (TextView) itemView.findViewById(R.id.t_price);
    }

    @Override
    public void onClick(View v) {
    }
}
