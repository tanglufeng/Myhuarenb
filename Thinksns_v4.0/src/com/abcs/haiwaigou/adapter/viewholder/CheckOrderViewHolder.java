package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/2/26.
 */
public class CheckOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //    public TextView t_goods_name;
//    public TextView t_goods_price;
//    public TextView t_num;
//    public ImageView img_goods;
//    public CheckOrderViewHolder(View itemView, ItemOnClick itemOnClick) {
//        super(itemView);
//        t_goods_name = (TextView) itemView.findViewById(R.id.t_goods_name);
//        t_goods_price= (TextView) itemView.findViewById(R.id.t_goods_price);
//        t_num= (TextView) itemView.findViewById(R.id.t_num);
//        img_goods= (ImageView) itemView.findViewById(R.id.img_goods);
//        img_goods.setOnClickListener(this);
//        this.itemOnClick = itemOnClick;
//    }
//
//    ItemOnClick itemOnClick;
//
//
//    public interface ItemOnClick {
//        void onItemRootViewClick(int position);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == img_goods) {
//            itemOnClick.onItemRootViewClick(getAdapterPosition());
//        }
//    }


    public LinearLayout linear_goods,liner_yun_heji;
    public TextView t_freight;
    public TextView t_store_total;
    public TextView t_goods_total;
    public TextView t_store_name;
    public TextView t_mansong;
    public TextView t_mansong_desc;
    public EditText ed_words;
    public CheckOrderViewHolder(View itemView) {
        super(itemView);
        linear_goods= (LinearLayout) itemView.findViewById(R.id.linear_goods);
        liner_yun_heji= (LinearLayout) itemView.findViewById(R.id.liner_yun_heji);
        t_freight= (TextView) itemView.findViewById(R.id.t_freight);
        t_store_total= (TextView) itemView.findViewById(R.id.t_store_total);
        t_mansong= (TextView) itemView.findViewById(R.id.t_mansong);
        t_mansong_desc= (TextView) itemView.findViewById(R.id.t_mansong_desc);
//        t_goods_total= (TextView) itemView.findViewById(R.id.t_goods_total);
        t_store_name= (TextView) itemView.findViewById(R.id.t_store_name);
        ed_words= (EditText) itemView.findViewById(R.id.ed_words);

    }


    @Override
    public void onClick(View v) {

    }

}
