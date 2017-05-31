package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity2;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.view.MyListView;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/26.
 */
public class AllGoodsAdapter extends BaseAdapter{
    private ArrayList<Goods> goodsList;
    Activity activity;
    LayoutInflater inflater = null;
    //    ZrcListView listView;
    MyListView listView;
    public Handler handler = new Handler();

    public AllGoodsAdapter(Activity activity, ArrayList<Goods> goodsList,
                        MyListView listView) {
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
            convertView = mInflater.inflate(R.layout.hwg_item_goods, null);

            mHolder = new ViewHolder();

            mHolder.img_goods_icon = (ImageView) convertView.findViewById(R.id.img_goods_icon);
            mHolder.t_goods_name = (TextView) convertView.findViewById(R.id.t_goods_name);
            mHolder.t_goods_works = (TextView) convertView.findViewById(R.id.t_goods_works);
            mHolder.t_goods_money = (TextView) convertView.findViewById(R.id.t_goods_money);
            mHolder.t_goods_count = (TextView) convertView.findViewById(R.id.t_goods_count);
            mHolder.linear_root = (LinearLayout) convertView.findViewById(R.id.linear_root);

            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }

		/*
         * 左上角日期
		 */

        mHolder.t_goods_name.setText(goods.getTitle());
        mHolder.t_goods_money.setText(goods.getMoney() + "");
        mHolder.t_goods_count.setText(goods.getGoods_salenum());
//        mHolder.t_goods_works.setText(goods.getSubhead());

        //图片加载
        LoadPicture loadPicture=new LoadPicture();
        loadPicture.initPicture(mHolder.img_goods_icon, goods.getPicarr());

        mHolder.linear_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, GoodsDetailActivity2.class);
                intent.putExtra("sid",goodsList.get(position).getGoods_id());
//                intent.putExtra("pic",goodsList.get(position).getPicarr());
                activity.startActivity(intent);
            }
        });

        return convertView;

    }


    private static class ViewHolder {
        TextView t_goods_name;
        TextView t_goods_money;
        TextView t_goods_count;
        TextView t_goods_works;
        ImageView img_goods_icon;
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
