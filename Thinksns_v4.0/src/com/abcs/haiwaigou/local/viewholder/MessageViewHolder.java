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
public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public RelativeLayout relative_root;
    public ImageView img_icon;
    public ImageView img_location;
    public TextView t_title;
    public TextView t_time;
    public TextView t_address;
    public TextView t_tips;
    public TextView t_text;

    public MessageViewHolder(View itemView, RootOnClick rootOnClick) {
        super(itemView);
        this.rootOnClick = rootOnClick;
        relative_root = (RelativeLayout) itemView.findViewById(R.id.relative_root);
        img_icon = (ImageView) itemView.findViewById(R.id.img_icon);
        img_location = (ImageView) itemView.findViewById(R.id.img_location);
        t_title = (TextView) itemView.findViewById(R.id.t_title);
        t_time = (TextView) itemView.findViewById(R.id.t_time);
        t_address = (TextView) itemView.findViewById(R.id.t_address);
        t_tips = (TextView) itemView.findViewById(R.id.t_tips);
        t_text = (TextView) itemView.findViewById(R.id.t_text);
        relative_root.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_root:
                rootOnClick.itemRootOnClick(getAdapterPosition());
                break;
        }
    }

    RootOnClick rootOnClick;

    public interface RootOnClick {
        void itemRootOnClick(int position);
    }
}
