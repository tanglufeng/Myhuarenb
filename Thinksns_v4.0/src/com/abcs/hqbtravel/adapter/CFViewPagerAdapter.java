package com.abcs.hqbtravel.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/16.
 */
public class CFViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> dates;
    ArrayList<String>title;

    public ArrayList<Fragment> getDatas(){
        return dates;
    }
    public ArrayList<String> getTitle(){
        return title;

    }
    public CFViewPagerAdapter(FragmentManager fm) {
        super(fm);
        dates=new ArrayList<Fragment>();
        title=new ArrayList();
        title.add("所有");
        title.add("好评");
        title.add("中评");
        title.add("差评");
//        title.add("有图");
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

//        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    //需要设置第三方
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }


    @Override
    public Fragment getItem(int position) {
        return dates.get(position);
    }



}
