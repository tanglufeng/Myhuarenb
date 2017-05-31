package com.abcs.haiwaigou.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.adapter.viewholder.DeliverViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/26.
 */
public class DeliverAdapter extends RecyclerView.Adapter<DeliverViewHolder> {
    public Handler handler = new Handler();

    ArrayList<Goods>goods;
    Context context;

    public ArrayList<Goods> getDatas() {
        return goods;
    }

    public DeliverAdapter() {
        this.goods = new ArrayList<>();
    }

    @Override
    public DeliverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_deliver, parent, false);
        DeliverViewHolder hwgFragmentViewHolder = new DeliverViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(DeliverViewHolder holder, final int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.line.getLayoutParams();
        if (position == 0) {
            holder.title.setVisibility(View.VISIBLE);
            holder.date.setText(goods.get(position).getTitle());
            params.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_title);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.txt_date_content);
        } else { // 不是第一条数据
            // 本条数据和上一条数据的时间戳相同，时间标题不显示
            if (goods.get(position).getTitle().equals(goods.get(position - 1).getTitle())) {
                holder.title.setVisibility(View.GONE);
                params.addRule(RelativeLayout.ALIGN_TOP, R.id.txt_date_content);
                params.addRule(RelativeLayout.ALIGN_BOTTOM,
                        R.id.txt_date_content);
            } else {
                //本条数据和上一条的数据的时间戳不同的时候，显示数据
                holder.title.setVisibility(View.VISIBLE);
                holder.date.setText(goods.get(position).getTitle());
                params.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_title);
                params.addRule(RelativeLayout.ALIGN_BOTTOM,
                        R.id.txt_date_content);
            }
        }
        holder.line.setLayoutParams(params);
        holder.content.setText(goods.get(position).getSubhead());
//        holder.date.setText(goods.get(position).getTitle());
//        holder.content.setText(goods.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }
}
