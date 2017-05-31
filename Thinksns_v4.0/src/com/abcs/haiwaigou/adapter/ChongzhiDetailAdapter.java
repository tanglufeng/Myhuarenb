package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.adapter.viewholder.ChongzhiDetailViewHolder;
import com.abcs.haiwaigou.model.Recharge;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.Util;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class ChongzhiDetailAdapter extends RecyclerView.Adapter<ChongzhiDetailViewHolder>{

    Context context;
    private SortedList<Recharge> mSortedList;
    private Activity activity;

    public ChongzhiDetailAdapter(Activity activity) {
        this.activity = activity;
        mSortedList = new SortedList<>(Recharge.class, new SortedList.Callback<Recharge>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Recharge o1, Recharge o2) {

                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Recharge oldItem, Recharge newItem) {
                return oldItem.getRecharge_id().equals(newItem.getRecharge_id());
            }

            @Override
            public boolean areItemsTheSame(Recharge item1, Recharge item2) {
                return item1.getId() == item2.getId();
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }
        });
    }

    public SortedList<Recharge> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Recharge> list) {
        mSortedList.beginBatchedUpdates();

        for (Recharge itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Recharge> items) {
        mSortedList.beginBatchedUpdates();
        for (Recharge item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public ChongzhiDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_recharge_detail, parent, false);
        ChongzhiDetailViewHolder hwgFragmentViewHolder = new ChongzhiDetailViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(ChongzhiDetailViewHolder holder, int position) {
        Recharge item=mSortedList.get(position);
        holder.t_add_time.setText(Util.format.format(item.getAdd_time()));
        holder.t_desc.setText(item.getDescription());
        if(item.getAvailable_amount()<0){
            holder.t_expend.setText(""+item.getAvailable_amount());
            holder.t_income.setText("0.00");
        }else {
            holder.t_expend.setText("0.00");
            holder.t_income.setText("+"+item.getAvailable_amount());
        }
        if(item.getFreeze_amount()!=0){
            if(item.getFreeze_amount()>0){
                holder.t_freeze.setText("+"+item.getFreeze_amount());
            }else {
                holder.t_freeze.setText(""+item.getFreeze_amount());
            }
        }else {
            holder.t_freeze.setText("0.00");
        }
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
