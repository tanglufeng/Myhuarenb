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
public class YYGPreRecordViewHolder extends RecyclerView.ViewHolder{

    public ImageView img_icon;
    public TextView t_title;
    public TextView t_winner;
    public TextView t_luckyNum;
    public TextView t_time;
    public TextView t_canyu;
    public TextView t_money;
    public LinearLayout linear_root;
    public YYGPreRecordViewHolder(View itemView) {
        super(itemView);
        img_icon= (ImageView) itemView.findViewById(R.id.img_icon);
        t_title= (TextView) itemView.findViewById(R.id.t_title);
        t_winner= (TextView) itemView.findViewById(R.id.t_winner);
        t_luckyNum= (TextView) itemView.findViewById(R.id.t_luckyNum);
        t_time= (TextView) itemView.findViewById(R.id.t_time);
        t_canyu= (TextView) itemView.findViewById(R.id.t_canyu);
        t_money= (TextView) itemView.findViewById(R.id.t_money);
        linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);
    }
}
