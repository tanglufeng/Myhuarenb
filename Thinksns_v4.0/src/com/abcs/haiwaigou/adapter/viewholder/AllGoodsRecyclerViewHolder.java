package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/2/26.
 */
public class AllGoodsRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView t_goods_name;
    public TextView t_goods_money;
    public TextView t_goods_count;
    public TextView t_goods_works;
    public ImageView img_goods_icon;
    public TextView t_desc;
    public ImageView img_btn_add;
    public LinearLayout linear_root;
    public AllGoodsRecyclerViewHolder(View itemView,ItemOnClick itemOnClick) {
        super(itemView);
        img_goods_icon = (ImageView) itemView.findViewById(R.id.img_goods_icon);
        img_btn_add = (ImageView) itemView.findViewById(R.id.img_btn_add);
        t_goods_name = (TextView) itemView.findViewById(R.id.t_goods_name);
        t_desc = (TextView) itemView.findViewById(R.id.t_desc);
        t_goods_works = (TextView) itemView.findViewById(R.id.t_goods_works);
        t_goods_money = (TextView) itemView.findViewById(R.id.t_goods_money);
        t_goods_count = (TextView) itemView.findViewById(R.id.t_goods_count);
        linear_root = (LinearLayout) itemView.findViewById(R.id.linear_root);
        linear_root.setOnClickListener(this);
        this.itemOnClick = itemOnClick;
    }
    ItemOnClick itemOnClick;


    public interface ItemOnClick {
        void onItemRootViewClick(int position);
    }

    @Override
    public void onClick(View v) {
        if (v == linear_root) {
            itemOnClick.onItemRootViewClick(getAdapterPosition());
        }
    }
}
