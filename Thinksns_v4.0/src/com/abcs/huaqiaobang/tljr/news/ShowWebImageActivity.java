package com.abcs.huaqiaobang.tljr.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.tljr.news.fragment.ShowImageFragment;
import com.abcs.huaqiaobang.tljr.news.viewpager.DepthPageTransformer;
import com.abcs.huaqiaobang.tljr.news.widget.ImageViewPager;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowWebImageActivity extends BaseFragmentActivity {

    private String imagePath = null;

    private ArrayList<String> uList;

    ImageViewPager pager;
    ShowImageAdapter adapter;

    TextView tljr_txt_page;

    private HashMap<Integer, Fragment> fragmentList = new HashMap<Integer, Fragment>();

    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tljr_activity_webimageshow);
        this.imagePath = getIntent().getStringExtra("image");
        uList = getIntent().getExtras().getStringArrayList("ulist");
        currentPosition = uList.indexOf(imagePath);
        adapter = new ShowImageAdapter(getSupportFragmentManager());

        tljr_txt_page = (TextView) findViewById(R.id.tljr_txt_page);
        findViewById(R.id.tljr_img_news_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        pager = (ImageViewPager) findViewById(R.id.imgeview_pager);
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                tljr_txt_page.setText(position + 1 + "/" + uList.size());
                currentPosition = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });


        pager.setCurrentItem(currentPosition);
        tljr_txt_page.setText(currentPosition + 1 + "/" + uList.size());


    }

    public void svaePic(View view) {


        ShowImageFragment showFragment = (ShowImageFragment) fragmentList.get(currentPosition);
        showFragment.savePicture();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();


    }


    public class ShowImageAdapter extends FragmentPagerAdapter {
        FragmentManager fm;

        public ShowImageAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            // super.destroyItem(container, position, object);

            View view = (View) object;
            ((ViewPager) container).removeView(view);
            view = null;

        }

        @Override
        public Fragment getItem(int position) {
            if (fragmentList.get(position) == null) {
                ShowImageFragment sif = ShowImageFragment.getInstance(uList.get(position), position, pager);
                fragmentList.put(position, sif);
                return sif;
            } else {
                ShowImageFragment sif = (ShowImageFragment) fragmentList.get(position);
                return sif;
            }


        }

        @Override
        public int getCount() {
            return uList.size();
        }
    }

}
