package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.adapter.viewholder.PreDepositViewHolder;
import com.abcs.haiwaigou.model.Prediposit;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.Util;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class PreDepositAdapter extends RecyclerView.Adapter<PreDepositViewHolder>{

    Context context;
    private SortedList<Prediposit> mSortedList;
    private Activity activity;

    public PreDepositAdapter(Activity activity) {
        this.activity = activity;
        mSortedList = new SortedList<>(Prediposit.class, new SortedList.Callback<Prediposit>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Prediposit o1, Prediposit o2) {

                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Prediposit oldItem, Prediposit newItem) {
                return oldItem.getPdc_sn().equals(newItem.getPdc_sn());
            }

            @Override
            public boolean areItemsTheSame(Prediposit item1, Prediposit item2) {
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

    public SortedList<Prediposit> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Prediposit> list) {
        mSortedList.beginBatchedUpdates();

        for (Prediposit itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Prediposit> items) {
        mSortedList.beginBatchedUpdates();
        for (Prediposit item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public PreDepositViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_predeposit_detail, parent, false);
        PreDepositViewHolder hwgFragmentViewHolder = new PreDepositViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(PreDepositViewHolder holder, int position) {
        Prediposit item=mSortedList.get(position);
        holder.t_add_time.setText(Util.format.format(item.getPdc_add_time()));
        holder.t_sn.setText(item.getPdc_sn());
        holder.t_money.setText(item.getPdc_amount()+"元");
        holder.t_bank.setText(item.getPdc_bank_name());
        holder.t_account.setText(item.getPdc_bank_no());
        holder.t_name.setText(item.getPdc_bank_user());
        if(item.getPdc_payment_state().equals("0")){
            holder.linear_pay_time.setVisibility(View.GONE);
            holder.img_state.setImageResource(R.drawable.img_hwg_unpay);
        }else if(item.getPdc_payment_state().equals("1")){
            holder.linear_pay_time.setVisibility(View.VISIBLE);
            holder.t_pay_time.setText(Util.format.format(item.getPdc_payment_time()));
            holder.img_state.setImageResource(R.drawable.img_hwg_paid);
        }
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
