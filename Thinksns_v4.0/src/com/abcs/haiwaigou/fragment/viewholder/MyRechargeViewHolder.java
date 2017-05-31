package com.abcs.haiwaigou.fragment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/4/26.
 */
public class MyRechargeViewHolder extends RecyclerView.ViewHolder{
    public RelativeLayout relative_left;
    public TextView t_price;
    public TextView t_desc;
    public TextView t_end_time;
    public ImageView img_state;
    public MyRechargeViewHolder(View itemView) {
        super(itemView);
        relative_left= (RelativeLayout) itemView.findViewById(R.id.relative_left);
        t_price= (TextView) itemView.findViewById(R.id.t_price);
        t_desc= (TextView) itemView.findViewById(R.id.t_desc);
        t_end_time= (TextView) itemView.findViewById(R.id.t_end_time);
        img_state= (ImageView) itemView.findViewById(R.id.img_state);
    }

}
