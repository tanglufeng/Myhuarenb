package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.TagArr;
import com.abcs.sociax.android.R;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */
public class PeiSongTopAllTagAdapter extends RecyclerView.Adapter<PeiSongTopAllTagAdapter.MyHolder> {

    private final Context mContext;
    private final List<TagArr> allTopTagArr;

    public PeiSongTopAllTagAdapter(Context mContext, List<TagArr> allTopTagArr) {
        this.mContext = mContext;
        this.allTopTagArr = allTopTagArr;
    }

    public interface OnAllTagItemClickListener {
        void allTagItemClick(TagArr tagArr);
    }

    private OnAllTagItemClickListener listener;

    public void setOnItemClickListener(OnAllTagItemClickListener listener) {
        if (listener != null) {
            this.listener = listener;
        }

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_all_tag, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setData(allTopTagArr.get(position));
    }

    @Override
    public int getItemCount() {
        return allTopTagArr.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_tab;

        public MyHolder(View itemView) {
            super(itemView);
            tv_tab = (TextView) itemView.findViewById(R.id.tv_tab);
        }

        public void setData(final TagArr tagArr) {
            tv_tab.setText(tagArr.tagName);
            tv_tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.allTagItemClick(tagArr);

                }
            });
        }
    }


}
