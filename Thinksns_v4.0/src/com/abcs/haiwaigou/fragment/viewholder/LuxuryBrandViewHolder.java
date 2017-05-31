package com.abcs.haiwaigou.fragment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abcs.sociax.android.R;


/**
 * Created by zjz on 2016/1/12.
 */
public class LuxuryBrandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView img_title;
    public ImageView img_big;
    public LinearLayout linear_small;

    public LuxuryBrandViewHolder(View itemView) {
        super(itemView);
        img_title= (ImageView) itemView.findViewById(R.id.img_title);
        img_big= (ImageView) itemView.findViewById(R.id.img_big);
        linear_small= (LinearLayout) itemView.findViewById(R.id.linear_small);
    }
    public LuxuryBrandViewHolder(View itemView, ItemOnClick itemOnClick) {
        super(itemView);
        img_title= (ImageView) itemView.findViewById(R.id.img_title);
        img_big= (ImageView) itemView.findViewById(R.id.img_big);
        linear_small= (LinearLayout) itemView.findViewById(R.id.linear_small);
        img_big.setOnClickListener(this);
        this.itemOnClick = itemOnClick;
    }

    ItemOnClick itemOnClick;
    public interface ItemOnClick {
        void onItemRootViewClick(int position);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_big:
                itemOnClick.onItemRootViewClick(getAdapterPosition());
                break;
        }
    }
}
