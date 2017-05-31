package com.abcs.haiwaigou.fragment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/2/26.
 */
public class ReturnAndRefundFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView t_type;
    public TextView t_code;
    public TextView t_time;
    public TextView t_store_name;
    public TextView t_goods_name;
    public TextView t_order_code;
    public TextView t_request;
    public TextView t_state;
    public TextView t_refund_money;
    public TextView t_detail;
    public ImageView img_goods_icon;
    public RelativeLayout relative_goods_icon;
    public ReturnAndRefundFragmentViewHolder(View itemView, ItemOnClick itemOnClick) {
        super(itemView);
        relative_goods_icon= (RelativeLayout) itemView.findViewById(R.id.relative_goods_icon);
        img_goods_icon= (ImageView) itemView.findViewById(R.id.img_goods_icon);
        t_type = (TextView) itemView.findViewById(R.id.t_type);
        t_code = (TextView) itemView.findViewById(R.id.t_code);
        t_time = (TextView) itemView.findViewById(R.id.t_time);
        t_store_name = (TextView) itemView.findViewById(R.id.t_store_name);
        t_goods_name = (TextView) itemView.findViewById(R.id.t_goods_name);
        t_order_code = (TextView) itemView.findViewById(R.id.t_order_code);
        t_request = (TextView) itemView.findViewById(R.id.t_request);
        t_state = (TextView) itemView.findViewById(R.id.t_state);
        t_refund_money = (TextView) itemView.findViewById(R.id.t_refund_money);
        t_detail = (TextView) itemView.findViewById(R.id.t_detail);
        t_detail.setOnClickListener(this);
        img_goods_icon.setOnClickListener(this);
        t_store_name.setOnClickListener(this);
        this.itemOnClick=itemOnClick;
    }
    ItemOnClick itemOnClick;

    public interface ItemOnClick {
        void onClickDetail(int position);
        void onImgClick(int position);
        void onStoreClick(int position);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.t_detail:
                itemOnClick.onClickDetail(getAdapterPosition());
                break;
            case R.id.img_goods_icon:
                itemOnClick.onImgClick(getAdapterPosition());
                break;
            case R.id.t_store_name:
                itemOnClick.onStoreClick(getAdapterPosition());
                break;
        }
    }
}
