package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/2/26.
 */
public class CommentRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView img_goods_icon;
    public ImageView img_1;
    public ImageView img_2;
    public ImageView img_3;
    public ImageView img_4;
    public ImageView img_5;
    public TextView t_goods_name;
    public TextView t_time;
    public TextView t_comment_info;
    public TextView t_explain;
    public RatingBar ratingBar;
    public LinearLayout linear_share;
    public LinearLayout linear_explain;
    public Button btn_share;
    public RelativeLayout relative_pic1;
    public RelativeLayout relative_pic2;
    public RelativeLayout relative_pic3;
    public RelativeLayout relative_pic4;
    public RelativeLayout relative_pic5;
    public CommentRecyclerViewHolder(View itemView,ItemOnClick itemOnClick) {
        super(itemView);
        img_1= (ImageView) itemView.findViewById(R.id.img_1);
        img_2= (ImageView) itemView.findViewById(R.id.img_2);
        img_3= (ImageView) itemView.findViewById(R.id.img_3);
        img_4= (ImageView) itemView.findViewById(R.id.img_4);
        img_5= (ImageView) itemView.findViewById(R.id.img_5);
        relative_pic1= (RelativeLayout) itemView.findViewById(R.id.relative_pic1);
        relative_pic2= (RelativeLayout) itemView.findViewById(R.id.relative_pic2);
        relative_pic3= (RelativeLayout) itemView.findViewById(R.id.relative_pic3);
        relative_pic4= (RelativeLayout) itemView.findViewById(R.id.relative_pic4);
        relative_pic5= (RelativeLayout) itemView.findViewById(R.id.relative_pic5);
        img_goods_icon= (ImageView) itemView.findViewById(R.id.img_goods_icon);
        t_goods_name = (TextView) itemView.findViewById(R.id.t_goods_name);
        t_time = (TextView) itemView.findViewById(R.id.t_time);
        t_comment_info = (TextView) itemView.findViewById(R.id.t_comment_info);
        t_explain = (TextView) itemView.findViewById(R.id.t_explain);
        linear_share = (LinearLayout) itemView.findViewById(R.id.linear_share);
        linear_explain = (LinearLayout) itemView.findViewById(R.id.linear_explain);
        ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        btn_share = (Button) itemView.findViewById(R.id.btn_share);
        this.itemOnClick=itemOnClick;
        btn_share.setOnClickListener(this);
        img_goods_icon.setOnClickListener(this);
    }
    ItemOnClick itemOnClick;


    public interface ItemOnClick {
        void onItemRootViewClick(int position);
        void onBtnShare(int position);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_share:
                itemOnClick.onBtnShare(getAdapterPosition());
                break;
            case R.id.img_goods_icon:
                itemOnClick.onItemRootViewClick(getAdapterPosition());
                break;
        }
    }
}
