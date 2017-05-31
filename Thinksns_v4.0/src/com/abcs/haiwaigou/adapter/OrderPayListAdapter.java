package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.model.PayWay;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/29.
 */
public class OrderPayListAdapter extends BaseAdapter {

    private ArrayList<PayWay> payList;

    Activity activity;
    LayoutInflater inflater = null;
    ZrcListView listView;
    //    MyListView listView;
    public Handler handler = new Handler();

    String total;
    public OrderPayListAdapter(Activity activity, ArrayList<PayWay> payList,String total,
                                ZrcListView listView) {
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.listView = listView;
        this.payList=payList;
        this.total=total;

        inflater = LayoutInflater.from(activity);
    }


    ImageView imageView;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder = null;
        final PayWay pay = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(activity);
            convertView = mInflater.inflate(R.layout.hwg_item_order_pay, null);

            mHolder = new ViewHolder();
            mHolder.t_total= (TextView) convertView.findViewById(R.id.t_total);
            mHolder.t_pay= (TextView) convertView.findViewById(R.id.t_pay);
            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }


        mHolder.t_pay.setText(pay.getPayment_name());
        mHolder.t_total.setText(total);
//        mHolder.group_zListView.setAdapter();
//        mHolder.pay_zListView.setAdapter();
        return convertView;

    }


    private static class ViewHolder {
        TextView t_total;
        TextView t_pay;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return payList == null ? 0 : payList.size();
    }


    @Override
    public PayWay getItem(int position) {
        if (payList != null && payList.size() != 0) {
            if (position >= payList.size()) {
                return payList.get(0);
            }
            return payList.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
