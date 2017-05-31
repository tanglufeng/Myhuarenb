package com.abcs.haiwaigou.fragment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/4/26.
 */
public class PointDetailViewHolder extends RecyclerView.ViewHolder{
    public TextView t_desc;
    public TextView t_add_time;
    public TextView t_points;
    public PointDetailViewHolder(View itemView ) {
        super(itemView);
        t_add_time= (TextView) itemView.findViewById(R.id.t_add_time);
        t_desc= (TextView) itemView.findViewById(R.id.t_desc);
        t_points= (TextView) itemView.findViewById(R.id.t_points);
    }
}
