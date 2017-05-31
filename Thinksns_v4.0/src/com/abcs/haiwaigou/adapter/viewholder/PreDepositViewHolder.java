package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class PreDepositViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    public TextView t_name;
    public TextView t_sn;
    public TextView t_account;
    public TextView t_bank;
    public TextView t_add_time;
    public TextView t_pay_time;
    public TextView t_money;
    public ImageView img_state;
    public LinearLayout linear_pay_time;
    public PreDepositViewHolder(View itemView) {
        super(itemView);
        t_add_time = (TextView) itemView.findViewById(R.id.t_add_time);
        t_pay_time = (TextView) itemView.findViewById(R.id.t_pay_time);
        t_sn = (TextView) itemView.findViewById(R.id.t_sn);
        t_account = (TextView) itemView.findViewById(R.id.t_account);
        t_name = (TextView) itemView.findViewById(R.id.t_name);
        t_bank = (TextView) itemView.findViewById(R.id.t_bank);
        t_money = (TextView) itemView.findViewById(R.id.t_money);
        img_state= (ImageView) itemView.findViewById(R.id.img_state);
        linear_pay_time= (LinearLayout) itemView.findViewById(R.id.linear_pay_time);
    }

    @Override
    public void onClick(View v) {

    }
}
