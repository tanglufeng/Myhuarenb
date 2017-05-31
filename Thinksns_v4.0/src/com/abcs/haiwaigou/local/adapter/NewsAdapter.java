package com.abcs.haiwaigou.local.adapter;

import android.app.Activity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.beans.LNews;
import com.abcs.haiwaigou.local.fragment.viewholder.HireJobViewHolder;
import com.abcs.haiwaigou.local.viewholder.NewsViewHolder;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/9/6.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    Activity activity;
    NewsViewHolder.RootOnClick rootOnClick;
    public ArrayList<LNews> newsList;
    private SortedList<LNews> mSortedList;

    public NewsAdapter(Activity activity, NewsViewHolder.RootOnClick rootOnClick) {
        this.newsList = new ArrayList<LNews>();
        this.activity = activity;
        this.rootOnClick = rootOnClick;
        mSortedList = new SortedList<>(LNews.class, new SortedList.Callback<LNews>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(LNews o1, LNews o2) {

                if (o1.getIds() < o2.getIds()) {
                    return -1;
                } else if (o1.getIds() > o2.getIds()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(LNews oldItem, LNews newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areItemsTheSame(LNews item1, LNews item2) {
                return item1.getId().equals(item2.getId());
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

    public SortedList<LNews> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<LNews> list) {
        mSortedList.beginBatchedUpdates();

        for (LNews itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<LNews> items) {
        mSortedList.beginBatchedUpdates();
        for (LNews item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_item_news, parent, false);
        NewsViewHolder currentViewHolder = new NewsViewHolder(view, rootOnClick);
        return currentViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        LNews lNews = mSortedList.get(position);
        holder.t_title.setText(lNews.getTitle());
        holder.t_time.setVisibility(View.VISIBLE);
        if (lNews.getAuditTime() < 2 * 1000000000) {
            holder.t_time.setText(Util.formatDisplayTime(lNews.getAuditTime() * 1000));
        } else {
            holder.t_time.setText(Util.formatDisplayTime(lNews.getAuditTime()));
        }
        ImageLoader.getInstance().displayImage(lNews.getImage(), holder.img_icon, Options.getListOptions());
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
