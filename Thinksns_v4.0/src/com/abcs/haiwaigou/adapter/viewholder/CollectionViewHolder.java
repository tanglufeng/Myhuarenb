package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;


/**
 * Created by zjz on 2016/4/18.
 */
public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView t_goods_name;
    public TextView t_goods_money;
    public TextView t_goods_sales;
    public ImageView img_goods_icon;
    public ImageView img_delete;
    public LinearLayout linear_root;
    public CheckBox btn_check;
    public CollectionViewHolder(View itemView,ItemRootOnclick itemRootOnclick) {
        super(itemView);
        this.itemRootOnclick=itemRootOnclick;
        img_goods_icon = (ImageView) itemView.findViewById(R.id.img_goods_icon);
        t_goods_name = (TextView) itemView.findViewById(R.id.t_goods_name);
        t_goods_money = (TextView) itemView.findViewById(R.id.t_goods_money);
        t_goods_sales = (TextView) itemView.findViewById(R.id.t_goods_sales);
        linear_root = (LinearLayout) itemView.findViewById(R.id.linear_root);
        img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
        btn_check= (CheckBox) itemView.findViewById(R.id.btn_check);
        img_goods_icon.setOnClickListener(this);
    }
    ItemRootOnclick itemRootOnclick;
    public interface ItemRootOnclick{
        void ItemRootClick(int position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_goods_icon:
                itemRootOnclick.ItemRootClick(getAdapterPosition());
                break;
        }
    }
}
