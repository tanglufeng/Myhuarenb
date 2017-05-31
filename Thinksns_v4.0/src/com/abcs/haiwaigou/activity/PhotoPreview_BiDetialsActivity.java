package com.abcs.haiwaigou.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.view.CircleIndicator;
import com.abcs.haiwaigou.view.HackyViewPager;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoPreview_BiDetialsActivity extends BaseActivity {

    @InjectView(R.id.vp)
    HackyViewPager vp;
    @InjectView(R.id.indicator)
    CircleIndicator indicator;
    ArrayList<ImageView> imageViews;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    private ArrayList<String> uList;
    int currentPosition;
    private String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_photo_preview);
        ButterKnife.inject(this);
//        this.imagePath = getIntent().getStringExtra("position");
        uList = getIntent().getExtras().getStringArrayList("ulist");
        currentPosition = Integer.valueOf(getIntent().getStringExtra("position"));
        imageViews = new ArrayList();
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for (String url : uList) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(url, imageView, Options.getHDOptions(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    //在图片下载成功的时候
                    //使用PhotoViewAttacher对象对ImageView对象进行包装，使其能够缩放
                    PhotoViewAttacher mAttacher = new PhotoViewAttacher((ImageView) view);
                    //要刷新
                    mAttacher.update();
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
            imageViews.add(imageView);
        }
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(pagerAdapter);
        vp.setCurrentItem(currentPosition);
        indicator.setViewPager(vp);
    }


    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView(imageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            return imageViews.get(position);
        }
    };
}
