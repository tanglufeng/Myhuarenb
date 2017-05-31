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
public class NewHireJobViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public RelativeLayout relative_root;
    public TextView t_title;
    public TextView t_numbers;
    public TextView tv_address;
    public ImageView img_icon;
    public NewHireJobViewHolder(View itemView, RootOnClick rootOnClick) {
        super(itemView);
        this.rootOnClick=rootOnClick;
        relative_root= (RelativeLayout) itemView.findViewById(R.id.relative_root);
        t_title= (TextView) itemView.findViewById(R.id.t_title);
        t_numbers= (TextView) itemView.findViewById(R.id.t_numbers);
        tv_address= (TextView) itemView.findViewById(R.id.tv_address);
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
