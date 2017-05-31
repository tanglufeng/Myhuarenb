package com.abcs.haiwaigou.yyg.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/7/2 0002.
 */
public class YYGMyBuyRecordViewHolder extends RecyclerView.ViewHolder{

    public ImageView img_icon;
    public TextView t_title;
    public TextView t_all_need;
    public TextView t_remain;
    public TextView t_canyu;
    public TextView t_money;
    public ProgressBar goods_prograss;
    public LinearLayout linear_root;
    public YYGMyBuyRecordViewHolder(View itemView) {
        super(itemView);
        img_icon= (ImageView) itemView.findViewById(R.id.img_icon);
        t_title= (TextView) itemView.findViewById(R.id.t_title);
        t_all_need= (TextView) itemView.findViewById(R.id.t_all_need);
        t_remain= (TextView) itemView.findViewById(R.id.t_remain);
        t_canyu= (TextView) itemView.findViewById(R.id.t_canyu);
        t_money= (TextView) itemView.findViewById(R.id.t_money);
        goods_prograss= (ProgressBar) itemView.findViewById(R.id.goods_prograss);
        linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);
    }
}
