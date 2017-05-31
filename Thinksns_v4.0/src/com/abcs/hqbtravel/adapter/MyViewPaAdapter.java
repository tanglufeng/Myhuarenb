package com.abcs.hqbtravel.adapter;/**
 * Created by Administrator on 2017/1/4.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.YuanHuiCenterActivity;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;

import java.util.List;
import java.util.Map;

/**
 * User: zds
 * Date: 2017-01-04
 * Time: 15:23
 * FIXME
 */
public class MyViewPaAdapter extends PagerAdapter {

    List<Map<String,Object>> viewLists;
    Activity mContext;

    public MyViewPaAdapter(Activity mactivity,List<Map<String,Object>> lists)
    {
        viewLists = lists;
        mContext = mactivity;
    }

    @Override
    public int getCount() {    //获得size
        // TODO Auto-generated method stub
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View view, int position, Object object)      //销毁Item
    {
        ImageView x=(ImageView)viewLists.get(position).get("view");
        x.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ((ViewPager) view).removeView(x);
    }

    @Override
    public Object instantiateItem(View view, int position)  //实例化Item
    {
        ImageView x=(ImageView)viewLists.get(position).get("view");
        final String flag=viewLists.get(position).get("flag").toString();
        x.setScaleType(ImageView.ScaleType.FIT_CENTER);
        MyApplication.imageLoader.displayImage(viewLists.get(position).get("url").toString(), x, MyApplication.getListOptions());
        ((ViewPager) view).addView(x, 0);

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag.equals("2")){

                    if(MyApplication.getInstance().getMykey()==null){
                        Intent tt=new Intent(mContext,WXEntryActivity.class);
                        tt.putExtra("isthome",true);
                        mContext.startActivity(tt);
                    }else {
                        Intent irre=new Intent(mContext,YuanHuiCenterActivity.class);
                        mContext.startActivity(irre);
                        mContext.finish();
                    }
                }
            }
        });

        return viewLists.get(position).get("view");
    }

}