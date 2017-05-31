package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/4/18.
 */
public class SearchHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView t_history;

    public SearchHistoryViewHolder(View itemView, ItemRootOnclick itemRootOnclick) {
        super(itemView);
        this.itemRootOnclick=itemRootOnclick;
        t_history = (TextView) itemView.findViewById(R.id.t_history);
        t_history.setOnClickListener(this);
    }
    ItemRootOnclick itemRootOnclick;
    public interface ItemRootOnclick{
        void ItemRootClick(int position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.t_history:
                itemRootOnclick.ItemRootClick(getAdapterPosition());
                break;
        }
    }
}
