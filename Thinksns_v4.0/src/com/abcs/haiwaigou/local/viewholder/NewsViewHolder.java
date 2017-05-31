package com.abcs.haiwaigou.local.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/9/6.
 */
public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public RelativeLayout relative_root;
    public TextView t_title;
    public TextView t_time;
    public TextView t_content;
    public ImageView img_icon;
    public NewsViewHolder(View itemView, RootOnClick rootOnClick) {
        super(itemView);
        this.rootOnClick=rootOnClick;
        relative_root= (RelativeLayout) itemView.findViewById(R.id.relative_root);
        t_title= (TextView) itemView.findViewById(R.id.t_title);
        t_time= (TextView) itemView.findViewById(R.id.t_time);
        t_content= (TextView) itemView.findViewById(R.id.t_content);
        img_icon= (ImageView) itemView.findViewById(R.id.img_icon);
        relative_root.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_root:
                rootOnClick.itemRootOnClick(getAdapterPosition());
                break;
        }
    }
    RootOnClick rootOnClick;
    public interface RootOnClick{
        void itemRootOnClick(int position);
    }
}
