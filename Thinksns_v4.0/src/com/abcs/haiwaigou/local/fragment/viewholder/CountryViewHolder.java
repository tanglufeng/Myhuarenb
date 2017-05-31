package com.abcs.haiwaigou.local.fragment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.view.MyGridView;
import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/9/6.
 */
public class CountryViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout linear_country;
    public TextView t_country_name;
    public TextView t_pingyin;
    public TextView t_num;
    public MyGridView gridview_hot_city;
    public ImageView img_arrow;
    public CountryViewHolder(View itemView) {
        super(itemView);
        linear_country= (LinearLayout) itemView.findViewById(R.id.linear_country);
        t_country_name= (TextView) itemView.findViewById(R.id.t_country_name);
        t_pingyin= (TextView) itemView.findViewById(R.id.t_pingyin);
        t_num= (TextView) itemView.findViewById(R.id.t_num);
        gridview_hot_city= (MyGridView) itemView.findViewById(R.id.gridview_hot_city);
        img_arrow= (ImageView) itemView.findViewById(R.id.img_arrow);
    }

}
