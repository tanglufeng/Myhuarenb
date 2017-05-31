package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/2/26.
 */
public class DeliverViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//    public TextView t_text;
    public RelativeLayout title;
    public View line;
    public TextView date;
    public TextView content;
    public DeliverViewHolder(View itemView) {
        super(itemView);
//        t_text = (TextView) itemView.findViewById(R.id.t_text);
        date = (TextView) itemView
                .findViewById(R.id.txt_date_time);
        content = (TextView) itemView
                .findViewById(R.id.txt_date_content);
        line = (View) itemView.findViewById(R.id.v_line);
        title = (RelativeLayout) itemView
                .findViewById(R.id.rl_title);

    }

    @Override
    public void onClick(View v) {

    }
}
