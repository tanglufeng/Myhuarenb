package com.abcs.haiwaigou.fragment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/4/26.
 */
public class RechargeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public RelativeLayout relative_left;
    public TextView t_price;
    public TextView t_desc;
    public TextView t_end_time;
    public TextView t_point;
    public TextView t_text;
    public TextView t_name;
    public RelativeLayout linear_root;
    public ImageView img_xia;
    public RechargeViewHolder(View itemView,ItemOnClick itemOnClick) {
        super(itemView);
        this.itemOnClick=itemOnClick;
        relative_left= (RelativeLayout) itemView.findViewById(R.id.relative_left);
        t_price= (TextView) itemView.findViewById(R.id.t_price);
        t_desc= (TextView) itemView.findViewById(R.id.t_desc);
        t_end_time= (TextView) itemView.findViewById(R.id.t_end_time);
        t_point= (TextView) itemView.findViewById(R.id.t_point);
        t_text= (TextView) itemView.findViewById(R.id.t_text);
        t_name= (TextView) itemView.findViewById(R.id.t_name);
        linear_root= (RelativeLayout) itemView.findViewById(R.id.linear_root);
        img_xia= (ImageView) itemView.findViewById(R.id.img_xia);
        linear_root.setOnClickListener(this);
    }

    ItemOnClick itemOnClick;
    public interface ItemOnClick{
        void ItemRootClick(int position);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_root:
                itemOnClick.ItemRootClick(getAdapterPosition());
                break;
        }
    }
}
