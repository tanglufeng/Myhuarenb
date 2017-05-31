package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.AllStoreListActivity;
import com.abcs.haiwaigou.model.Store;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/20.
 */
public class AllStoreAdapter extends BaseAdapter {
    private ArrayList<Store> goodsList;
    Activity activity;
    LayoutInflater inflater = null;
    ZrcListView listView;
    //    MyListView listView;
    public Handler handler = new Handler();


    public AllStoreAdapter(Activity activity, ArrayList<Store> goodsList,
                           ZrcListView listView) {
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.goodsList = goodsList;
        this.listView = listView;


        inflater = LayoutInflater.from(activity);
    }


    ImageView imageView;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder = null;
        final Store goods = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(activity);
            convertView = mInflater.inflate(R.layout.hwg_item_store, null);

            mHolder = new ViewHolder();

            mHolder.t_goods_name = (TextView) convertView.findViewById(R.id.t_goods_name);
            mHolder.rl_root= (RelativeLayout) convertView.findViewById(R.id.rl_root);

            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }

		/*
         * 左上角日期
		 */

        mHolder.t_goods_name.setText(goods.getName());

        mHolder.rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, AllStoreListActivity.class);
                intent.putExtra("sc_id",goodsList.get(position).getId());
                activity.startActivity(intent);
            }
        });

        return convertView;

    }


    private static class ViewHolder {
        TextView t_goods_name;
        RelativeLayout rl_root;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return goodsList == null ? 0 : goodsList.size();
    }


    @Override
    public Store getItem(int position) {
        if (goodsList != null && goodsList.size() != 0) {
            if (position >= goodsList.size()) {
                return goodsList.get(0);
            }
            return goodsList.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
