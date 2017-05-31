package com.abcs.haiwaigou.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.activity.RefundDetailActivity;
import com.abcs.haiwaigou.fragment.viewholder.ReturnAndRefundFragmentViewHolder;
import com.abcs.haiwaigou.model.Refund;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/16.
 */
public class RefundFragmentAdapter extends RecyclerView.Adapter<ReturnAndRefundFragmentViewHolder> {
    public Handler handler = new Handler();

    Context context;
    private SortedList<Refund> mSortedList;

    ReturnAndRefundFragmentViewHolder.ItemOnClick itemOnClick;

    Activity activity;

    public RefundFragmentAdapter(Activity activity, ReturnAndRefundFragmentViewHolder.ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
        this.activity = activity;
        mSortedList = new SortedList<>(Refund.class, new SortedList.Callback<Refund>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Refund o1, Refund o2) {

                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Refund oldItem, Refund newItem) {
                return oldItem.getAdd_time().equals(newItem.getAdd_time());
            }

            @Override
            public boolean areItemsTheSame(Refund item1, Refund item2) {
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


    public SortedList<Refund> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Refund> list) {
        mSortedList.beginBatchedUpdates();

        for (Refund itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Refund> items) {
        mSortedList.beginBatchedUpdates();
        for (Refund item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public ReturnAndRefundFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_refund_and_return, parent, false);
        ReturnAndRefundFragmentViewHolder hwgFragmentViewHolder = new ReturnAndRefundFragmentViewHolder(view, itemOnClick);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(ReturnAndRefundFragmentViewHolder holder, final int position) {
        final Refund item = mSortedList.get(position);
        holder.t_order_code.setText(item.getOrder_sn());
        holder.t_code.setText(item.getRefund_sn());
        holder.t_store_name.setText(item.getStore_name());
        holder.t_goods_name.setText(item.getGoods_name());
        holder.t_type.setText("退款编号：");
        holder.t_time.setText(Util.format.format(item.getAdd_time()*1000)+"");
        if(item.getGoods_image().equals("null")){
            holder.relative_goods_icon.setVisibility(View.GONE);
        }else {
            holder.relative_goods_icon.setVisibility(View.VISIBLE);
            LoadPicture loadPicture=new LoadPicture();
            String url=TLUrl.getInstance().URL_hwg_comment_goods+item.getStore_id()+"/"+item.getGoods_image();
            loadPicture.initPicture(holder.img_goods_icon,url);
        }
        holder.t_refund_money.setText("¥" + item.getRefund_amount());
        switch (item.getRefund_state()){
            case "1":
                holder.t_state.setText("待审核");
                holder.t_request.setText("无");
                break;
            case "2":
                holder.t_state.setText("待审核");
                holder.t_request.setText("待审核");
                break;
            case "3":
                if(item.getSeller_state().equals("2")){
                    holder.t_state.setText("已完成");
                    holder.t_request.setText("已完成");
                }else if(item.getSeller_state().equals("3")){
                    holder.t_state.setText("不同意");
                    holder.t_request.setText("无");
                }

                break;
        }
        holder.t_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(activity, RefundDetailActivity.class);
                intent.putExtra("refund_id",item.getRefund_id());
                intent.putExtra("isRefund",true);
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
