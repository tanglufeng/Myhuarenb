package com.abcs.sociax.t4.android.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 类说明：资讯分类
 */
public class HWGHDongListPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> tables;

    public HWGHDongListPagerAdapter(FragmentManager fm, ArrayList<String> tables) {
        super(fm);
        this.tables = tables;
    }

    @Override
    public Fragment getItem(int position) {

        FragmentInformation fragmentSociax = new FragmentInformation();

        Bundle data = new Bundle();
//        data.putInt("cid", tables.get(position).getId());

        fragmentSociax.setArguments(data);
//        Log.v("Information", "getItem cid-->" + tables.get(position).getId() + ", position:" + position);
        return fragmentSociax;
    }

    @Override
    public int getCount() {
        return tables.size();
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
