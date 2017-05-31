package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.InitCountryViewPager;
import com.abcs.haiwaigou.model.Country;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/14.
 */
public class GridAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    private ArrayList<Country> countriesList;
    Activity activity;
    LayoutInflater inflater = null;
    public Handler handler = new Handler();

    public GridAdapter(Activity activity, ArrayList<Country> countriesList
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
        final Country c = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(activity);
            convertView = mInflater.inflate(R.layout.hwg_gridview_country, null);

            mHolder = new ViewHolder();

            mHolder.t_goods_name = (TextView) convertView.findViewById(R.id.tljr_view_sygird_name);
            mHolder.img_goods = (ImageView) convertView.findViewById(R.id.tljr_view_sygird_icon);

            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }


        //图片加载
        LoadPicture loadPicture = new LoadPicture();
        loadPicture.initPicture(mHolder.img_goods, c.getPicarr());

        mHolder.img_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, InitCountryViewPager.class);
//                Intent intent=new Intent(activity, CountryGoodsActivity.class);
                intent.putExtra("cid", countriesList.get(position).getCid());
                intent.putExtra("country", countriesList.get(position).getName());
//                activity.overridePendingTransition(R.anim.country_in_activity, R.anim.country_out_activity);
//                Toast.makeText(activity,"这是"+countriesList.get(position).getName(),Toast.LENGTH_SHORT).show();
                activity.startActivity(intent);
            }
        });
//		mHolder.img_goods.setImageResource(goods.getPicarr()+"");
//		mHolder.img_goods.setImageURI(Uri.parse(goods.getPicarr()));

//        MyApplication myApp = (MyApplication) activity.getApplicationContext();
//        ImageAware imageAware = new ImageViewAware(mHolder.img_goods, false);
//        myApp.getImageLoader().displayImage(goods.getPicarr(), imageAware, myApp.getFaceDisplayImageLoad());

        return convertView;

    }


    private static class ViewHolder {

        TextView t_goods_name;
        ImageView img_goods;


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return countriesList == null ? 0 : countriesList.size();
    }


    @Override
    public Country getItem(int position) {
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
