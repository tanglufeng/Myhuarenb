package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.model.Store;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/14.
 */
public class MyGridAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    private ArrayList<Store> countriesList;
    Activity activity;
    LayoutInflater inflater = null;
    public Handler handler = new Handler();

    public MyGridAdapter(Activity activity, ArrayList<Store> countriesList
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
        final Store c = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_shopping_store_item, null);

            mHolder = new ViewHolder();

            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);

            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }

        //图片加载
        LoadPicture loadPicture = new LoadPicture();
        Log.i("zjz", "avatar=" + c.getAvatar());
        String s=TLUrl.getInstance().URL_hwg_store_head+c.getAvatar();
        loadPicture.initPicture(mHolder.img_icon, s);
//        loadPicture.initPicture(mHolder.img_icon, "https://www.baidu.com/img/bd_logo1.png");
//        loadPicture.initPicture(mHolder.img_icon, "http://www.huaqiaobang.com/data/upload/shop/store/05091987599978118_sm.png");
        mHolder.tv_name.setText(c.getName());
        mHolder.img_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        TextView tv_name;
       ImageView img_icon;


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return countriesList == null ? 0 : countriesList.size();
    }


    @Override
    public Store getItem(int position) {
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
