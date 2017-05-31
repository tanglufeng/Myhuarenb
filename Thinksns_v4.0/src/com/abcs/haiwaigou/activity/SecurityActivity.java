package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SecurityActivity extends BaseActivity {


    @InjectView(R.id.securityViewpager)
    ViewPager securityViewpager;
    private int[] pic = new int[]{
            R.drawable.img_hwg_baozhang5,
            R.drawable.img_hwg_baozhang4,
            R.drawable.img_hwg_baozhang3,
            R.drawable.img_hwg_baozhang2,
            R.drawable.img_hwg_baozhang1,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        ButterKnife.inject(this);

        final List<View> list = new ArrayList<View>();
        for (int i = 0; i < pic.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(pic[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            list.add(imageView);
        }
        securityViewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(list.get(position));
                return list.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }
        });
    }
}
