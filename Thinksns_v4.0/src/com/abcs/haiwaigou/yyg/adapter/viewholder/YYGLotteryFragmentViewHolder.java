package com.abcs.haiwaigou.yyg.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.yyg.view.MyTextView;
import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/7/2 0002.
 */
public class YYGLotteryFragmentViewHolder extends RecyclerView.ViewHolder{

    public ImageView img_icon;
    public ImageView img_isbuy;
    public ImageView img_isget;
    public ImageView img_isluck;
    public TextView t_name;
    public MyTextView t_count_time;
    public RelativeLayout relative_done;
    public RelativeLayout relative_root;
    public LinearLayout linear_undo;
    public TextView t_winner;
    public TextView t_code;
    public TextView t_joins;
    public TextView t_time;
    public TextView t_total_money;
    public YYGLotteryFragmentViewHolder(View itemView) {
        super(itemView);
        img_icon= (ImageView) itemView.findViewById(R.id.img_icon);
        img_isbuy= (ImageView) itemView.findViewById(R.id.img_isbuy);
        img_isget= (ImageView) itemView.findViewById(R.id.img_isget);
        img_isluck= (ImageView) itemView.findViewById(R.id.img_isluck);
        t_name= (TextView) itemView.findViewById(R.id.t_name);
        t_count_time= (MyTextView) itemView.findViewById(R.id.t_count_time);
        t_winner= (TextView) itemView.findViewById(R.id.t_winner);
        t_code= (TextView) itemView.findViewById(R.id.t_code);
        t_joins= (TextView) itemView.findViewById(R.id.t_joins);
        t_time= (TextView) itemView.findViewById(R.id.t_time);
        t_total_money= (TextView) itemView.findViewById(R.id.t_total_money);
        relative_done= (RelativeLayout) itemView.findViewById(R.id.relative_done);
        relative_root= (RelativeLayout) itemView.findViewById(R.id.relative_root);
        linear_undo= (LinearLayout) itemView.findViewById(R.id.linear_undo);
    }
}
