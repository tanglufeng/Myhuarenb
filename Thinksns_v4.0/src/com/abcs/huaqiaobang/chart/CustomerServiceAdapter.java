package com.abcs.huaqiaobang.chart;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.tljr.news.HuanQiuShiShi;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/7.
 */
public class CustomerServiceAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<RealBean> array;

    public CustomerServiceAdapter(Context context, ArrayList<RealBean> array) {

        this.context = context;
        this.array = array;

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RealViewHolder holder = null;
        RealBean rb = array.get(position);
        int type = getItemViewType(position);
        if (convertView == null) {
            if (type == 0) {
                convertView = LayoutInflater.from(context).inflate(R.layout.tljr_activity_chart_item_right, null);

            }
            if (type == 1) {
                convertView = LayoutInflater.from(context).inflate(R.layout.tljr_activity_chart_item, null);
            }

            holder = new RealViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.image = (CircularImage) convertView.findViewById(R.id.im);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (RealViewHolder) convertView.getTag();
        }
        holder.name.setText(rb.name);
        holder.content.setText(rb.msg);
        try {
            holder.time.setText(HuanQiuShiShi.getStandardDateBySimple(Util.getDate(rb.time)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.image.setTag(rb.image);
        final ImageView imageView = holder.image;
        final String url_img = rb.image;
//        MyApplication.imageLoader.displayImage(url_img, imageView, Options.getCircleListOptions());

        MyApplication.imageLoader.displayImage(url_img, imageView, Options.getCircleListOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (imageView.getTag().equals(url_img))
                    MyApplication.imageLoader.displayImage(url_img, imageView);

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        if (array.get(position).uid.equals(MyApplication.getInstance().self.getId())) {
            return 0;
        } else {
            return 1;
        }
    }

    public final class RealViewHolder {
        TextView time;
        TextView content;
        TextView name;
        ImageView image;
    }

    public static class RealBean {
        long time;
        String name;
        String msg;
        String uid;
        String id;
        String image;
    }
}
