package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.AllGoodsActivity2;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/22.
 */
public class GoodsChildAdpater extends BaseAdapter{
    private ArrayList<Goods> goodsList;
    Activity activity;
    LayoutInflater inflater = null;
    ZrcListView listView;
    //    MyListView listView;
    public Handler handler = new Handler();


    public GoodsChildAdpater(Activity activity, ArrayList<Goods> goodsList,
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
            convertView = mInflater.inflate(R.layout.hwg_item_expandlist_child, null);

            mHolder = new ViewHolder();

            mHolder.t_txt = (TextView) convertView.findViewById(R.id.t_txt);
            mHolder.linear_root= (LinearLayout) convertView.findViewById(R.id.linear_root);
            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }

		/*
         * 左上角日期
		 */

        mHolder.t_txt.setText(goods.getTitle());
        mHolder.linear_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, AllGoodsActivity2.class);
                intent.putExtra("gc_id",goodsList.get(position).getGc_id());
                activity.startActivity(intent);
                Log.i("zjz","gc_id="+goodsList.get(position).getGc_id());
            }
        });

        return convertView;

    }


    private static class ViewHolder {
        TextView t_txt;
        LinearLayout linear_root;
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
