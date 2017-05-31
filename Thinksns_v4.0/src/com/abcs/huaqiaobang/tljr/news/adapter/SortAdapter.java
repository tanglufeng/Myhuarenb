package com.abcs.huaqiaobang.tljr.news.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.tljr.news.HuanQiuShiShi;
import com.abcs.huaqiaobang.tljr.news.bean.CountrySortModel;

import java.text.ParseException;
import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<CountrySortModel> list = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<CountrySortModel> list) {
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

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        CountrySortModel mContent = (CountrySortModel) getItem(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_select_city, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_news_country);
            viewHolder.tvDesc = (TextView) view.findViewById(R.id.tv_news_desc);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_catagory);
            viewHolder.imgCountry = (ImageView) view.findViewById(R.id.img_news_country);
            viewHolder.tvDate = (TextView) view.findViewById(R.id.tv_news_date);
            viewHolder.tvCount = (TextView) view.findViewById(R.id.notify_circle);
            viewHolder.line_tv_catagory = view.findViewById(R.id.line_tv_catagory);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);

        if (mContent != null) {
            if (position == getPositionForSection(section)) {
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(mContent.getSortLetters());
            } else {
                viewHolder.tvLetter.setVisibility(View.GONE);
                viewHolder.line_tv_catagory.setVisibility(View.GONE);
            }

            viewHolder.tvCount.setVisibility(mContent.getCount() > 0 ? View.VISIBLE : View.GONE);
            viewHolder.tvCount.setText(mContent.getCount() + "");

            viewHolder.tvTitle.setText(mContent.getCountry());
            Log.i("Sort", mContent.getDate() + "");
            viewHolder.tvDesc.setText(mContent.getDesc());
            try {
                viewHolder.tvDate.setText(HuanQiuShiShi.getStandardDate(mContent.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            viewHolder.imgCountry.setTag(mContent.getCountry_imgurl());


            if (viewHolder.imgCountry.getTag().equals(mContent.getCountry_imgurl())) {
                MyApplication.getInstance().imageLoader.displayImage(mContent.getCountry_imgurl(), viewHolder.imgCountry, Options.getListOptions());
            }
        }

        return view;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        TextView tvDesc;
        TextView tvDate;
        TextView tvCount;
        View line_tv_catagory;
        ImageView imgCountry;
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


    @Override
    public Object[] getSections() {
        return null;
    }
}