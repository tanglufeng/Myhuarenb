package com.abcs.haiwaigou.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.fragment.viewholder.PointDetailViewHolder;
import com.abcs.haiwaigou.model.Points;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.Util;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/4/26.
 */
public class PointDetailAdapter extends RecyclerView.Adapter<PointDetailViewHolder>{
    Context context;
    private SortedList<Points> mSortedList;
    private Activity activity;

    public PointDetailAdapter(Activity activity) {
        this.activity = activity;
        mSortedList = new SortedList<>(Points.class, new SortedList.Callback<Points>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Points o1, Points o2) {

                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Points oldItem, Points newItem) {
                return oldItem.getPl_id().equals(newItem.getPl_id());
            }

            @Override
            public boolean areItemsTheSame(Points item1, Points item2) {
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

    public SortedList<Points> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Points> list) {
        mSortedList.beginBatchedUpdates();

        for (Points itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Points> items) {
        mSortedList.beginBatchedUpdates();
        for (Points item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public PointDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_fragment_point, parent, false);
        PointDetailViewHolder hwgFragmentViewHolder = new PointDetailViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(PointDetailViewHolder holder, int position) {
        Points item=mSortedList.get(position);
        if(item.getPl_stage().equals("app")){
            holder.t_desc.setText("积分兑换");
        }else if(item.getPl_stage().equals("login")){
            holder.t_desc.setText("会员登录");
        }else if(item.getPl_stage().equals("comments")){
            holder.t_desc.setText("商品评论");
        }else if(item.getPl_stage().equals("order")){
            holder.t_desc.setText("订单消费");
        }else if(item.getPl_stage().equals("pointorder")){
            holder.t_desc.setText("礼品兑换");
        }else if(item.getPl_stage().equals("system")){
            holder.t_desc.setText("积分管理");
        }else if(item.getPl_stage().equals("regist")){
            holder.t_desc.setText("用户注册");
        }
        holder.t_add_time.setText(Util.format1.format(item.getPl_addtime()*1000)+"");
        if(item.getPl_points().contains("-")){
            holder.t_points.setText(item.getPl_points());
            holder.t_points.setTextColor(Color.parseColor("#1fbe3a"));
        }else {
            holder.t_points.setText("+"+item.getPl_points());
            holder.t_points.setTextColor(Color.parseColor("#f22828"));
        }
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
