package com.abcs.huaqiaobang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.CityAcitivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/19.
 */
public class PopAdapter extends BaseAdapter {

    private ArrayList<CityAcitivity.PopBean> list = null;
    private Context mContext;

    public PopAdapter(Context mContext, ArrayList<CityAcitivity.PopBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_bankcity, null);

        CityAcitivity.PopBean mContent = (CityAcitivity.PopBean) getItem(position);
        int section = getSectionForPosition(position);
        TextView tv_catagory = (TextView) view.findViewById(R.id.tv_catagory);
        TextView tv_bank = (TextView) view.findViewById(R.id.tv_bank);
        if (mContent != null) {
            if (position == getPositionForSection(section)) {
                tv_catagory.setVisibility(View.VISIBLE);
                tv_catagory.setText(mContent.getSortLetters());
            } else {
                tv_catagory.setVisibility(View.GONE);

            }
            tv_bank.setText(list.get(position).getName());
        }

        return view;
    }

    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }


    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }
}
