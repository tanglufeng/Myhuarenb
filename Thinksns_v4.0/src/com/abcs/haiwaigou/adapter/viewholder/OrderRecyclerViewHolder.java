package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/2/26.
 */
public class OrderRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView t_order_time;
    public TextView t_total_zfb;
    public TextView t_total_wx;
    public LinearLayout linear_store;
    public LinearLayout linear_pay;
    public LinearLayout linear_order;
    public LinearLayout linear_zfb;
    public LinearLayout linear_wx;
    public OrderRecyclerViewHolder(View itemView) {
        super(itemView);
        t_order_time = (TextView) itemView.findViewById(R.id.t_order_time);
        t_total_zfb = (TextView) itemView.findViewById(R.id.t_total_zfb);
        t_total_wx = (TextView) itemView.findViewById(R.id.t_total_wx);
//            mHolder.group_zListView = (ZrcListView) convertView.findViewById(R.id.group_zListView);
//            mHolder.pay_zListView = (ZrcListView) convertView.findViewById(R.id.pay_zListView);
        linear_store = (LinearLayout) itemView.findViewById(R.id.linear_store);
        linear_pay = (LinearLayout) itemView.findViewById(R.id.linear_pay);
        linear_order = (LinearLayout) itemView.findViewById(R.id.linear_order);
        linear_zfb = (LinearLayout) itemView.findViewById(R.id.linear_zfb);
        linear_wx = (LinearLayout) itemView.findViewById(R.id.linear_wx);
    }

    @Override
    public void onClick(View v) {

    }
}
