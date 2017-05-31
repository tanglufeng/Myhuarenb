package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.TagArr;
import com.abcs.sociax.android.R;

import java.util.List;

/**
 * Created by zjz on 2016/1/14.
 */
public class GridLocalTagAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    private List<TagArr> countriesList;
    Activity activity;
    LayoutInflater inflater = null;
    public Handler handler = new Handler();

    public GridLocalTagAdapter(Activity activity, List<TagArr> countriesList
    ) {
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.countriesList = countriesList;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder = null;
        final TagArr c = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(activity);
            convertView = mInflater.inflate(R.layout.item_textview_t, null);
            mHolder = new ViewHolder();
            mHolder.t_goods_name=(TextView) convertView.findViewById(R.id.tv_tab);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        if(!TextUtils.isEmpty(c.tagName)){
            mHolder.t_goods_name.setText(c.tagName);
        }

        return convertView;
    }


    private static class ViewHolder {

        TextView t_goods_name;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return countriesList == null ? 0 : countriesList.size();
    }


    @Override
    public TagArr getItem(int position) {
        if (countriesList != null && countriesList.size() != 0) {
            if (position >= countriesList.size()) {
                return countriesList.get(0);
            }
            return countriesList.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


}
