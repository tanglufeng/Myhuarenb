package com.abcs.haiwaigou.local.fragment.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/9/6.
 */
public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public CardView cv_root;
    public TextView t_title;
    public TextView t_time;
    public TextView t_money;
    public TextView t_address;
    public ImageView img_icon;
    public ServiceViewHolder(View itemView, RootOnClick rootOnClick) {
        super(itemView);
        this.rootOnClick=rootOnClick;
        cv_root= (CardView) itemView.findViewById(R.id.cv_root);
        t_title= (TextView) itemView.findViewById(R.id.t_title);
        t_time= (TextView) itemView.findViewById(R.id.t_time);
        t_money= (TextView) itemView.findViewById(R.id.t_money);
        t_address= (TextView) itemView.findViewById(R.id.t_address);
        img_icon= (ImageView) itemView.findViewById(R.id.img_icon);
        cv_root.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_root:
                rootOnClick.itemRootOnClick(getAdapterPosition());
                break;
        }
    }
    RootOnClick rootOnClick;
    public interface RootOnClick{
        void itemRootOnClick(int position);
    }
}
