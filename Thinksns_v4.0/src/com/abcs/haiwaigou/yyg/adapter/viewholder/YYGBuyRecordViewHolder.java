package com.abcs.haiwaigou.yyg.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/7/2 0002.
 */
public class YYGBuyRecordViewHolder extends RecyclerView.ViewHolder{

    public ImageView img_user;
    public TextView t_name;
    public TextView t_ip;
    public TextView t_cishu;
    public TextView t_time;
    public YYGBuyRecordViewHolder(View itemView) {
        super(itemView);
        img_user= (ImageView) itemView.findViewById(R.id.img_user);
        t_name= (TextView) itemView.findViewById(R.id.t_name);
        t_cishu= (TextView) itemView.findViewById(R.id.t_cishu);
        t_ip= (TextView) itemView.findViewById(R.id.t_ip);
        t_time= (TextView) itemView.findViewById(R.id.t_time);
    }
}
