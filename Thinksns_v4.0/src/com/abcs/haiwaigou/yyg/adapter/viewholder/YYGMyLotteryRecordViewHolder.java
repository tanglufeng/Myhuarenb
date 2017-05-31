package com.abcs.haiwaigou.yyg.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/7/2 0002.
 */
public class YYGMyLotteryRecordViewHolder extends RecyclerView.ViewHolder{

    public ImageView img_icon;
    public TextView t_title;
    public TextView t_all_need;
    public TextView t_luckyNum;
    public TextView t_time;
    public TextView t_canyu;
    public TextView t_money;
    public TextView t_state;
    public LinearLayout linear_root;
    public LinearLayout linear_state;
    public ImageView img_state;
    public YYGMyLotteryRecordViewHolder(View itemView) {
        super(itemView);
        img_icon= (ImageView) itemView.findViewById(R.id.img_icon);
        img_state= (ImageView) itemView.findViewById(R.id.img_state);
        t_title= (TextView) itemView.findViewById(R.id.t_title);
        t_all_need= (TextView) itemView.findViewById(R.id.t_all_need);
        t_luckyNum= (TextView) itemView.findViewById(R.id.t_luckyNum);
        t_time= (TextView) itemView.findViewById(R.id.t_time);
        t_canyu= (TextView) itemView.findViewById(R.id.t_canyu);
        t_money= (TextView) itemView.findViewById(R.id.t_money);
        t_state= (TextView) itemView.findViewById(R.id.t_state);
        linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);
        linear_state= (LinearLayout) itemView.findViewById(R.id.linear_state);
    }
}
