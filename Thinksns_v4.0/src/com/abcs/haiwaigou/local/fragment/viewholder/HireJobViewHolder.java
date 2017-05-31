package com.abcs.haiwaigou.local.fragment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/9/6.
 */
public class HireJobViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public RelativeLayout relative_root;
    public TextView t_title;
    public TextView t_time;
    public TextView t_text;
    public ImageView img_icon;
    public HireJobViewHolder(View itemView,RootOnClick rootOnClick) {
        super(itemView);
        this.rootOnClick=rootOnClick;
        relative_root= (RelativeLayout) itemView.findViewById(R.id.relative_root);
        t_title= (TextView) itemView.findViewById(R.id.t_title);
        t_time= (TextView) itemView.findViewById(R.id.t_time);
        t_text= (TextView) itemView.findViewById(R.id.t_text);
        img_icon= (ImageView) itemView.findViewById(R.id.img_icon);
        relative_root.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //发布的每个item的点击事件
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
