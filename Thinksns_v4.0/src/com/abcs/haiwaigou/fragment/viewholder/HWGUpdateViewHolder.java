package com.abcs.haiwaigou.fragment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/1/12.
 */
public class HWGUpdateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //    public ImageView img_pinpai;
//    public TextView t_remain;
//    public TextView t_saled;
//    public LinearLayout linear_root;
//    public HWGFragmentViewHolder(View itemView, ItemOnClick itemOnClick) {
//        super(itemView);
//        img_pinpai= (ImageView) itemView.findViewById(R.id.img_pinpai);
//        t_remain= (TextView) itemView.findViewById(R.id.t_remain);
//        t_saled= (TextView) itemView.findViewById(R.id.t_saled);
//        linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);
//        linear_root.setOnClickListener(this);
//        this.itemOnClick=itemOnClick;
//    }
//
//
//    ItemOnClick itemOnClick;
//    public interface ItemOnClick{
//        void onItemRootViewClick(int position);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if(v==linear_root){
//            itemOnClick.onItemRootViewClick(getAdapterPosition());
//        }
//    }


    public ImageView img_goods_icon;
    public TextView t_goods_name;
    public TextView t_goods_money;
    public TextView t_goods_oldmoney;
    public LinearLayout linear_root;

    public HWGUpdateViewHolder(View itemView, ItemOnClick itemOnClick) {
        super(itemView);
        img_goods_icon= (ImageView) itemView.findViewById(R.id.img_goods_icon);
        t_goods_name= (TextView) itemView.findViewById(R.id.t_goods_name);
        t_goods_money= (TextView) itemView.findViewById(R.id.t_goods_money);
        t_goods_oldmoney= (TextView) itemView.findViewById(R.id.t_goods_oldmoney);
        linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);
        linear_root.setOnClickListener(this);
        this.itemOnClick = itemOnClick;
    }


    ItemOnClick itemOnClick;

    public interface ItemOnClick {
        void onItemUpdateViewClick(int position);
    }

    @Override
    public void onClick(View v) {
        if (v == linear_root) {
            itemOnClick.onItemUpdateViewClick(getAdapterPosition());
        }
    }
}
