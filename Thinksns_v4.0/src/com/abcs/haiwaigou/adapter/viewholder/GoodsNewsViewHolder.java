package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/4/18.
 */
public class GoodsNewsViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout linear_root;
    public TextView t_state_and_name;
    public TextView t_detail;
    public ImageView img_goods_icon;
    public RelativeLayout relative_detail;
    public TextView t_time;
    public GoodsNewsViewHolder(View itemView) {
        super(itemView);
        linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);
        t_state_and_name = (TextView) itemView.findViewById(R.id.t_state_and_name);
        t_detail = (TextView) itemView.findViewById(R.id.t_detail);
        t_time = (TextView) itemView.findViewById(R.id.t_time);
        img_goods_icon = (ImageView) itemView.findViewById(R.id.img_goods_icon);
        relative_detail= (RelativeLayout) itemView.findViewById(R.id.relative_detail);
    }
}
