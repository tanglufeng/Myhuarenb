package com.abcs.huaqiaobang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.abcs.huaqiaobang.fragment.ProductFrament;

/**
 * Created by Administrator on 2016/1/20.
 */
public class ProductFragmentAdapter extends FragmentPagerAdapter {


    public ProductFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        return ProductFrament.getInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
