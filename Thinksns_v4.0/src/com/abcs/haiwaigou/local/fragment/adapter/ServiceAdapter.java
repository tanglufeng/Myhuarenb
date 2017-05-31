package com.abcs.haiwaigou.local.fragment.adapter;

import android.app.Activity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.fragment.viewholder.HireJobViewHolder;
import com.abcs.haiwaigou.local.fragment.viewholder.ServiceViewHolder;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/9/6.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceViewHolder> {
    Activity activity;
    ServiceViewHolder.RootOnClick rootOnClick;
    public ArrayList<HireFind> hireFinds;
    private SortedList<HireFind> mSortedList;

    public ServiceAdapter(Activity activity, ServiceViewHolder.RootOnClick rootOnClick) {
        this.hireFinds=new ArrayList<HireFind>();
        this.activity = activity;
        this.rootOnClick = rootOnClick;
        mSortedList = new SortedList<>(HireFind.class, new SortedList.Callback<HireFind>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(HireFind o1, HireFind o2) {

                if (o1.getCreateTime()>o2.getCreateTime()) {
                    return -1;
                } else if (o1.getCreateTime()< o2.getCreateTime()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(HireFind oldItem, HireFind newItem) {
                return oldItem.getTitle().equals(newItem.getTitle());
            }

            @Override
            public boolean areItemsTheSame(HireFind item1, HireFind item2) {
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
    public SortedList<HireFind> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<HireFind> list) {
        mSortedList.beginBatchedUpdates();

        for (HireFind itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<HireFind> items) {
        mSortedList.beginBatchedUpdates();
        for (HireFind item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_item_service, parent, false);
        ServiceViewHolder currentViewHolder = new ServiceViewHolder(view,rootOnClick);
        return currentViewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        HireFind hireFind=mSortedList.get(position);
        holder.t_title.setText(hireFind.getTitle());
        holder.t_time.setText(Util.formatzjz3.format(hireFind.getPubTime()*1000));
        ImageLoader.getInstance().displayImage(hireFind.getIcon(),holder.img_icon, Options.getListOptions());
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
