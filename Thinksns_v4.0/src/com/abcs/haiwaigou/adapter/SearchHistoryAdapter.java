package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.adapter.viewholder.SearchHistoryViewHolder;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/12.
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryViewHolder> {
    ArrayList<String>historys;
    Context context;
    Activity activity;
    public ArrayList<String> getDatas() {
        return historys;
    }

    SearchHistoryViewHolder.ItemRootOnclick itemOnClick;

    public SearchHistoryAdapter(Activity activity,SearchHistoryViewHolder.ItemRootOnclick itemOnClick) {
        this.activity=activity;
        this.itemOnClick = itemOnClick;
        this.historys = new ArrayList<>();
    }

    @Override
    public SearchHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_search_history, parent, false);
        SearchHistoryViewHolder hwgFragmentViewHolder = new SearchHistoryViewHolder(view, itemOnClick);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchHistoryViewHolder holder, final int position) {
        String strings=historys.get(position);
        holder.t_history.setText(strings);
    }

    @Override
    public int getItemCount() {
        return historys.size();
    }
}
