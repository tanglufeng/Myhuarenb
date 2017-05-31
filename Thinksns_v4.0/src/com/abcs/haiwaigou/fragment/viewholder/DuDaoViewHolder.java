package com.abcs.haiwaigou.fragment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.sociax.android.R;


/**
 * Created by zjz on 2016/1/12.
 */
public class DuDaoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tv_instrution;
    public ImageView imge_dudao;

    public DuDaoViewHolder(View itemView) {
        super(itemView);
        tv_instrution= (TextView) itemView.findViewById(R.id.tv_instrution);
        imge_dudao= (ImageView) itemView.findViewById(R.id.imge_dudao);
    }
    public DuDaoViewHolder(View itemView, ItemOnClick itemOnClick) {
        super(itemView);
        tv_instrution= (TextView) itemView.findViewById(R.id.tv_instrution);
        imge_dudao= (ImageView) itemView.findViewById(R.id.imge_dudao);
        imge_dudao.setOnClickListener(this);
        this.itemOnClick = itemOnClick;
    }

    ItemOnClick itemOnClick;
    public interface ItemOnClick {
        void onItemRootViewClick(int position);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imge_dudao:
                itemOnClick.onItemRootViewClick(getAdapterPosition());
                break;
        }
    }
}
