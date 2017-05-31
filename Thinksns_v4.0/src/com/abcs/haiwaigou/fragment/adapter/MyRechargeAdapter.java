package com.abcs.haiwaigou.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.fragment.viewholder.MyRechargeViewHolder;
import com.abcs.haiwaigou.model.Voucher;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.Util;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/4/26.
 */
public class MyRechargeAdapter extends RecyclerView.Adapter<MyRechargeViewHolder>{
    Context context;
    private SortedList<Voucher> mSortedList;
    private Activity activity;

    public MyRechargeAdapter(Activity activity) {
        this.activity = activity;
        mSortedList = new SortedList<>(Voucher.class, new SortedList.Callback<Voucher>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Voucher o1, Voucher o2) {

                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Voucher oldItem, Voucher newItem) {
                return oldItem.getVoucher_t_title().equals(newItem.getVoucher_t_title());
            }

            @Override
            public boolean areItemsTheSame(Voucher item1, Voucher item2) {
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

    public SortedList<Voucher> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Voucher> list) {
        mSortedList.beginBatchedUpdates();

        for (Voucher itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Voucher> items) {
        mSortedList.beginBatchedUpdates();
        for (Voucher item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public MyRechargeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_my_coupon, parent, false);
        MyRechargeViewHolder hwgFragmentViewHolder = new MyRechargeViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(MyRechargeViewHolder holder, int position) {
        Voucher item=mSortedList.get(position);
        holder.t_price.setText("¥"+item.getVoucher_t_price());
        holder.t_desc.setText("购物满"+item.getVoucher_t_limit()+"元可用");
        holder.t_end_time.setText("有效期至"+Util.format1.format(item.getVoucher_t_end_date()*1000));
        if(Integer.valueOf(item.getVoucher_t_price())<50){
            //蓝色
            holder.t_price.setBackgroundResource(R.drawable.img_hwg_my_coupon_blue);
        }else if(Integer.valueOf(item.getVoucher_t_price())>=50&&Integer.valueOf(item.getVoucher_t_price())<100){
            holder.t_price.setBackgroundResource(R.drawable.img_hwg_my_coupon_yellow);
        }else if(Integer.valueOf(item.getVoucher_t_price())>=100){
            holder.t_price.setBackgroundResource(R.drawable.img_hwg_my_coupon_pink);
        }
        if(item.getVoucher_t_state().equals("1")){
            holder.img_state.setVisibility(View.GONE);
        }else if(item.getVoucher_t_state().equals("2")){
            holder.img_state.setImageResource(R.drawable.img_hwg_yishiyoong);
        }else if(item.getVoucher_t_state().equals("3")){
            holder.img_state.setImageResource(R.drawable.img_hwg_yishixiao);
        }
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
