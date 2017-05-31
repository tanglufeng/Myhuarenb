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

import com.abcs.haiwaigou.activity.GoodsSortDetailActivity;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/20.
 */
public class GoodsSortAdapter extends BaseAdapter {
    private ArrayList<Goods> goodsList;
    Activity activity;
    LayoutInflater inflater = null;
    ZrcListView listView;
    //    MyListView listView;
    public Handler handler = new Handler();


    public GoodsSortAdapter(Activity activity, ArrayList<Goods> goodsList,
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
        final Goods goods = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(activity);
            convertView = mInflater.inflate(R.layout.hwg_item_goods_sort, null);

            mHolder = new ViewHolder();

            mHolder.t_goods_name = (TextView) convertView.findViewById(R.id.t_goods_name);
            mHolder.t_goods_subname = (TextView) convertView.findViewById(R.id.t_goods_subname);
            mHolder.rl_root= (RelativeLayout) convertView.findViewById(R.id.rl_root);

            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }

		/*
         * 左上角日期
		 */

        mHolder.t_goods_name.setText(goods.getTitle());
        mHolder.t_goods_subname.setText(goods.getSubhead());


        mHolder.rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, GoodsSortDetailActivity.class);
                intent.putExtra("gc_id",goodsList.get(position).getGc_id());
                activity.startActivity(intent);
            }
        });

        return convertView;

    }


    private static class ViewHolder {
        TextView t_goods_name;
        TextView t_goods_subname;
        RelativeLayout rl_root;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return goodsList == null ? 0 : goodsList.size();
    }


    @Override
    public Goods getItem(int position) {
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
