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
public class YYGGoodsFragmentViewHolder extends RecyclerView.ViewHolder{

    public ImageView img_icon;
    public ImageView img_buy;
    public ImageView img_isbuy;
    public TextView t_name;
    public TextView t_all_need;
    public TextView t_remain;
    public TextView t_total_money;
    public ProgressBar goods_prograss;
    public LinearLayout linear_root;
    public YYGGoodsFragmentViewHolder(View itemView) {
        super(itemView);
        img_icon= (ImageView) itemView.findViewById(R.id.img_icon);
        img_buy= (ImageView) itemView.findViewById(R.id.img_buy);
        img_isbuy= (ImageView) itemView.findViewById(R.id.img_isbuy);
        t_name= (TextView) itemView.findViewById(R.id.t_name);
        t_all_need= (TextView) itemView.findViewById(R.id.t_all_need);
        t_remain= (TextView) itemView.findViewById(R.id.t_remain);
        t_total_money= (TextView) itemView.findViewById(R.id.t_total_money);
        goods_prograss= (ProgressBar) itemView.findViewById(R.id.goods_prograss);
        linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);
    }
}
