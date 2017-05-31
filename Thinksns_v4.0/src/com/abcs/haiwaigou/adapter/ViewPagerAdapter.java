package com.abcs.haiwaigou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/13.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
//    ArrayList<GoodsFragment> goodsFragment;
//    ArrayList<DetailFragment> detailFragments;
//    ArrayList<CommentFragment>commentFragments;
    ArrayList<Fragment>all;
    ArrayList<String>title;

    public ArrayList<Fragment> getDatas(){
        return all;
    }
    public ArrayList<String> getTitle(){
        return title;

    }
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        all=new ArrayList<Fragment>();
        title=new ArrayList();
        title.add("图文详情");
        title.add("商品评价");
        title.add("评论");
    }




    @Override
    public int getCount() {
        return all.size();
    }

    //需要设置第三方
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }


    @Override
    public Fragment getItem(int position) {
        return all.get(position);
    }


}
