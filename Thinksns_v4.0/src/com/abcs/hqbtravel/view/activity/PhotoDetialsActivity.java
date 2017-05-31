package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.LookPhotos;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoDetialsActivity extends Activity implements View.OnClickListener{

    private ViewPager viewPager;
    private TextView tv_numbers;
    private ImageView img_back;
    private ViewPagerAdapter adapter;
    private List<Map<String, Object>> data;
    private String url;
    private String posi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_photo_detials);


        url=getIntent().getStringExtra("url");
        posi=getIntent().getStringExtra("position");

        viewPager=(ViewPager) findViewById(R.id.viewPager);
        tv_numbers=(TextView) findViewById(R.id.tv_location);
        img_back=(ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(this);

        data = getData(MyApplication.banners);
        adapter = new ViewPagerAdapter(data);
        viewPager.setAdapter(adapter);
        tv_numbers.setText((Integer.valueOf(posi)+1)+"/"+MyApplication.banners.size());
        viewPager.setCurrentItem(Integer.valueOf(posi));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int page, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int page) {
                tv_numbers.setText((page+1)+"/"+MyApplication.banners.size());
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.img_back:
                finish();
                break;
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {

        List<Map<String,Object>> viewLists;

        public ViewPagerAdapter(List<Map<String,Object>> lists)
        {
            viewLists = lists;
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
//            x.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ((ViewPager) view).removeView(x);
        }

        @Override
        public Object instantiateItem(View view, int position)  //实例化Item
        {
            ImageView x=(ImageView)viewLists.get(position).get("view");
//            x.setScaleType(ImageView.ScaleType.CENTER_CROP);

            MyApplication.imageLoader.displayImage(viewLists.get(position).get("url").toString(), x, MyApplication.getListOptions());
            ((ViewPager) view).addView(x, 0);

            return viewLists.get(position).get("view");
        }

    }


    public List<Map<String, Object>> getData(List<LookPhotos.BodyBean> banners) {
        List<Map<String, Object>> mdata = new ArrayList<Map<String, Object>>();

        for(int i=0;i<banners.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", banners.get(i).logo);
            map.put("view", new ImageView(this));
            mdata.add(map);
        }
        return  mdata;
    }

}
