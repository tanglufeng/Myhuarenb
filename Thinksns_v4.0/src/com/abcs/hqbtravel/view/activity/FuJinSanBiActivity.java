package com.abcs.hqbtravel.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.adapter.ViewPagerAdapter;
import com.abcs.hqbtravel.biz.getFuJinSanBiBiz;
import com.abcs.hqbtravel.entity.FujinSanBi;
import com.abcs.hqbtravel.view.fragment.BiChiFragment;
import com.abcs.hqbtravel.view.fragment.BiMaiFragment;
import com.abcs.hqbtravel.view.fragment.BiWanFragment;
import com.abcs.hqbtravel.view.fragment.ShiBaJiaFragment;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class FuJinSanBiActivity extends FragmentActivity implements  View.OnClickListener{

    private ViewPager viewpager;
    private ViewPagerAdapter pagerAdapter;
    private TextView tv_resteratr,tv_jindian,tv_shop,tv_peples,tv_lng,tv_lat,tv_tips;
    private TextView img_back,tv_locatio_time;
    private ImageView img_abort;
    private int pageNo=1;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int current_position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_fu_jin_san_bi);

        cityId=getIntent().getStringExtra("cityId");
        lat=getIntent().getStringExtra("current_lat");
        lng=getIntent().getStringExtra("current_lng");

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        viewpager=(ViewPager) findViewById(R.id.viewpager);
        img_back=(TextView)findViewById(R.id.tv_back);
        tv_locatio_time=(TextView)findViewById(R.id.tv_locatio_time);
        img_abort=(ImageView) findViewById(R.id.img_abort);
        tv_shop=(TextView)findViewById(R.id.tv_shop);
        tv_peples=(TextView)findViewById(R.id.tv_peples);
        tv_tips=(TextView)findViewById(R.id.tv_tips);
        tv_lng=(TextView)findViewById(R.id.tv_lng);
        tv_lat=(TextView)findViewById(R.id.tv_lat);
        tv_jindian=(TextView)findViewById(R.id.tv_jindian);
        tv_resteratr=(TextView)findViewById(R.id.tv_resteratr);

        img_back.setOnClickListener(this);
        img_abort.setOnClickListener(this);
        tv_shop.setOnClickListener(this);
        tv_jindian.setOnClickListener(this);
        tv_resteratr.setOnClickListener(this);

        if(!TextUtils.isEmpty( MyApplication.getWenDu())){
            tv_tips.setText(MyApplication.getWenDu());
        }else {
            tv_tips.setVisibility(View.GONE);
        }


        pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.e("zds","viewpager_positon"+position);
                current_position=position;
                switch (position){
                    case 0:
                        tv_resteratr.setBackground(getResources().getDrawable(R.drawable.near_sele_left));
                        tv_jindian.setBackground(getResources().getDrawable(R.drawable.near_nosele_zhong));
                        tv_shop.setBackground(getResources().getDrawable(R.drawable.near_nosele_right));
                        break;
                    case 1:
                        tv_resteratr.setBackground(getResources().getDrawable(R.drawable.near_nosele_left));
                        tv_jindian.setBackground(getResources().getDrawable(R.drawable.near_sele_zhong));
                        tv_shop.setBackground(getResources().getDrawable(R.drawable.near_nosele_right));
                        break;
                    case 2:
                        tv_resteratr.setBackground(getResources().getDrawable(R.drawable.near_nosele_left));
                        tv_jindian.setBackground(getResources().getDrawable(R.drawable.near_nosele_zhong));
                        tv_shop.setBackground(getResources().getDrawable(R.drawable.near_sele_right));
                        break;
                    case 3:
                        tv_resteratr.setBackground(getResources().getDrawable(R.drawable.near_nosele_left));
                        tv_jindian.setBackground(getResources().getDrawable(R.drawable.near_nosele_zhong));
                        tv_shop.setBackground(getResources().getDrawable(R.drawable.near_nosele_right));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDatas();
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        if(!TextUtils.isEmpty(cityId)){
            getDatas();
            if(!TextUtils.isEmpty(lng)){
                tv_lng.setText("E "+lng);
            }
            if(!TextUtils.isEmpty(lat)){
                tv_lat.setText("N "+lat);
            }
        }

        if(!TextUtils.isEmpty(MyApplication.getCityName())){
            tv_peples.setText(MyApplication.getCityName());
        }else {
            tv_peples.setText("曼谷");
        }
    }

    private String cityId;
    private String lng;
    private String lat;
    //    private String cityId="7823";
    private Handler mhandle=new Handler();
    private void getDatas() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        getFuJinSanBiBiz.getInstance(this).getFuJinSanBiList(cityId,pageNo,lng,lat,new Response.Listener<FujinSanBi>(){
            @Override
            public void onResponse(FujinSanBi response) {
                if(response!=null){
                    ProgressDlgUtil.stopProgressDlg();

                    if(response.result==1){


                        if(!TextUtils.isEmpty(response.body.localTime)){

                            tv_locatio_time.setText(response.body.localTime);
                        }

                        pagerAdapter.getDatas().add(BiChiFragment.newInstance(cityId,response));
                        pagerAdapter.getDatas().add(BiWanFragment.newInstance(cityId,response));
                        pagerAdapter.getDatas().add(BiMaiFragment.newInstance(cityId,response));

                        if(response.body.adults!=null&&response.body.adults.size()>0){
                            pagerAdapter.getDatas().add(ShiBaJiaFragment.newInstance(cityId,response));
                            img_abort.setVisibility(View.VISIBLE);
                        }

                        viewpager.setAdapter(pagerAdapter);
                        viewpager.setCurrentItem(current_position);
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(FuJinSanBiActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                ProgressDlgUtil.stopProgressDlg();
            }
        }, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.img_abort:  //18  加
                viewpager.setCurrentItem(3);
                tv_resteratr.setBackground(getResources().getDrawable(R.drawable.near_nosele_left));
                tv_jindian.setBackground(getResources().getDrawable(R.drawable.near_nosele_zhong));
                tv_shop.setBackground(getResources().getDrawable(R.drawable.near_nosele_right));
                break;
            case R.id.tv_resteratr:
                viewpager.setCurrentItem(0);
                tv_resteratr.setBackground(getResources().getDrawable(R.drawable.near_sele_left));
                tv_jindian.setBackground(getResources().getDrawable(R.drawable.near_nosele_zhong));
                tv_shop.setBackground(getResources().getDrawable(R.drawable.near_nosele_right));
                break;
            case R.id.tv_jindian:
                viewpager.setCurrentItem(1);
                tv_resteratr.setBackground(getResources().getDrawable(R.drawable.near_nosele_left));
                tv_jindian.setBackground(getResources().getDrawable(R.drawable.near_sele_zhong));
                tv_shop.setBackground(getResources().getDrawable(R.drawable.near_nosele_right));
                break;
            case R.id.tv_shop:
                viewpager.setCurrentItem(2);
                tv_resteratr.setBackground(getResources().getDrawable(R.drawable.near_nosele_left));
                tv_jindian.setBackground(getResources().getDrawable(R.drawable.near_nosele_zhong));
                tv_shop.setBackground(getResources().getDrawable(R.drawable.near_sele_right));
                break;
        }
    }
}
