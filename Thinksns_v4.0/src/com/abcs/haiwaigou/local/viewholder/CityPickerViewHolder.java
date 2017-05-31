package com.abcs.haiwaigou.local.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.abcs.haiwaigou.view.MyGridView;
import com.abcs.sociax.android.R;

/**
 * Created by Administrator on 2016/8/30.
 */
public class CityPickerViewHolder extends RecyclerView.ViewHolder{
    public TextView t_country_name;
    public MyGridView gridview_hot_city;
    public CityPickerViewHolder(View itemView) {
        super(itemView);
        t_country_name= (TextView) itemView.findViewById(R.id.t_country_name);
        gridview_hot_city= (MyGridView) itemView.findViewById(R.id.gridview_hot_city);
    }
}
