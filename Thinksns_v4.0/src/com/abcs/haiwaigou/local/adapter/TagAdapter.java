package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.getShaiXuanFirm;
import com.abcs.haiwaigou.view.TabLayout.OnInitSelectedPosition;
import com.abcs.sociax.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanHailong on 15/10/19.
 */
public class TagAdapter<T> extends BaseAdapter implements OnInitSelectedPosition {

    private final Context mContext;
    private final List<T> mDataList;

    public TagAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_text_mangsong, null);
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hd_text_sx, null);

        TextView textView = (TextView) view.findViewById(R.id.tv_tag);
        getShaiXuanFirm.DatasBean.TagListBean t = (getShaiXuanFirm.DatasBean.TagListBean)mDataList.get(position);

        if(t!=null){
            if(!TextUtils.isEmpty(t.tagName)){
                textView.setText((t).tagName);
            }
        }

        return view;
    }

    public void onlyAddAll(List<T> datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<T> datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }

    @Override
    public boolean isSelectedPosition(int position) {
        if (position % 2 == 0) {
            return true;
        }
        return false;
    }
}
