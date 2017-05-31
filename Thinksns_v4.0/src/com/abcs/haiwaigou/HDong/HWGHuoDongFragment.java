package com.abcs.haiwaigou.HDong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.HuoDongAdapter;
import com.abcs.haiwaigou.model.HuoDong;
import com.abcs.haiwaigou.utils.mCountDownTimer;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.tljr.news.NewsManager;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.common.utils.LogUtil;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.activity.widget.EmptyLayout;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HWGHuoDongFragment extends Fragment implements  RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener  {

    @InjectView(R.id.rv_hdong)
    EasyRecyclerView rvHuoDong;
    @InjectView(R.id.error_layout)
    EmptyLayout empty_layout;
    ActivityHome activity;
    View view;
    HuoDongAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ActivityHome) getActivity();
        view = activity.getLayoutInflater().inflate(R.layout.fragemt_h_dong, null);
        ButterKnife.inject(this, view);

        intRe();
    }

    TextView tv_t_hour; // 时
    TextView tv_t_min; // 分
    TextView tv_t_second,tv_activity_price,tv_activity_nexttime;// 秒
    ImageView iv_activity_qiangguo;// logo
    RelativeLayout re_qianggou;

    LinearLayout liner_activity;

    TextView tv_weekday_new,tv_activity_list_o,tv_activity_paihang,tv_activity_paihang_des,tv_activity_jingxuan,tv_activity_jingxuan_des;
    TextView tv_activity_remen,tv_activity_remen_des,tv_activity_guojiaguan,tv_activity_guojiaguan_des;

    ImageView iv_activty_list_o,iv_activty_list_paihang,iv_activty_list_jingxuan,iv_activty_list_remen,iv_activty_list_guojiaguan,iv_activty_list_f;

    TextView[] titles=new TextView[5];
    TextView[] times=new TextView[3];
    TextView[] des_s=new TextView[5];
    ImageView[] logos=new ImageView[6];

    View top_view;

    private void intRe(){

        empty_layout.setNoDataContent(getResources().getString(R.string.empty_content));
        empty_layout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDates();
            }
        });

        rvHuoDong.setLayoutManager(new LinearLayoutManager(activity));
        rvHuoDong.setRefreshListener(this);
        adapter = new HuoDongAdapter(activity);
        top_view = View.inflate(activity, R.layout.item_hdong_text, null);
        top_view.setVisibility(View.GONE);

        liner_activity=(LinearLayout) top_view.findViewById(R.id.liner_activity); // 活动

         tv_t_hour=(TextView) top_view.findViewById(R.id.tv_t_hour); // 时
         tv_t_min=(TextView) top_view.findViewById(R.id.tv_t_min); // 分
         tv_t_second=(TextView) top_view.findViewById(R.id.tv_t_second);// 秒
        tv_activity_price=(TextView) top_view.findViewById(R.id.tv_activity_price);// 价格
        tv_activity_nexttime=(TextView) top_view.findViewById(R.id.tv_activity_nexttime);// 下一场
        tv_weekday_new=(TextView) top_view.findViewById(R.id.tv_weekday_new);// 每周上新
        tv_activity_list_o=(TextView) top_view.findViewById(R.id.tv_activity_list_o);// 每周上新 描述
        iv_activty_list_o=(ImageView) top_view.findViewById(R.id.iv_activty_list_o);// 每周上新 logo
        tv_activity_paihang=(TextView) top_view.findViewById(R.id.tv_activity_paihang);// 排行榜
        iv_activty_list_paihang=(ImageView) top_view.findViewById(R.id.iv_activty_list_paihang);// 排行榜 logo
        tv_activity_paihang_des=(TextView) top_view.findViewById(R.id.tv_activity_paihang_des);// 排行榜 描述
        tv_activity_jingxuan=(TextView) top_view.findViewById(R.id.tv_activity_jingxuan);// 精选活动
        iv_activty_list_jingxuan=(ImageView) top_view.findViewById(R.id.iv_activty_list_jingxuan);// 精选活动 logo
        tv_activity_jingxuan_des=(TextView) top_view.findViewById(R.id.tv_activity_jingxuan_des);// 精选活动 描述
        tv_activity_remen=(TextView) top_view.findViewById(R.id.tv_activity_remen);// 热门品牌
        iv_activty_list_remen=(ImageView) top_view.findViewById(R.id.iv_activty_list_remen);// 热门品牌 logo
        tv_activity_remen_des=(TextView) top_view.findViewById(R.id.tv_activity_remen_des);// 热门品牌 描述
        tv_activity_guojiaguan=(TextView) top_view.findViewById(R.id.tv_activity_guojiaguan);// 国家馆
        iv_activty_list_guojiaguan=(ImageView) top_view.findViewById(R.id.iv_activty_list_guojiaguan);// 国家馆 logo
        iv_activty_list_f=(ImageView) top_view.findViewById(R.id.iv_activty_list_f);// big logo
        tv_activity_guojiaguan_des=(TextView) top_view.findViewById(R.id.tv_activity_guojiaguan_des);// 国家馆 描述

        times[0]=tv_t_hour;
        times[1]=tv_t_min;
        times[2]=tv_t_second;

        titles[0]=tv_weekday_new;
        titles[1]=tv_activity_paihang;
        titles[2]=tv_activity_jingxuan;
        titles[3]=tv_activity_remen;
        titles[4]=tv_activity_guojiaguan;

        des_s[0]=tv_activity_list_o;
        des_s[1]=tv_activity_paihang_des;
        des_s[2]=tv_activity_jingxuan_des;
        des_s[3]=tv_activity_remen_des;
        des_s[4]=tv_activity_guojiaguan_des;

        logos[0]=iv_activty_list_o;
        logos[1]=iv_activty_list_paihang;
        logos[2]=iv_activty_list_jingxuan;
        logos[3]=iv_activty_list_remen;
        logos[4]=iv_activty_list_guojiaguan;
        logos[5]=iv_activty_list_f;

        re_qianggou=(RelativeLayout) top_view.findViewById(R.id.re_qianggou);// logo
        iv_activity_qiangguo=(ImageView) top_view.findViewById(R.id.iv_activity_qiangguo);// logo


        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return top_view;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });

        rvHuoDong.setAdapter(adapter);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDates();
    }

    private int page=1;
    private boolean isRefresh = false;
    boolean isFirst = true;

    Handler handler=new Handler();
    mCountDownTimer countDownTimer;


    private void getDates() {


        empty_layout.setErrorType(EmptyLayout.NETWORK_LOADING);
        adapter.clear();
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", "act=activity&op=new_index", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                            handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rvHuoDong.setRefreshing(false);
                                adapter.stopMore();
                            }
                        },300);

                            LogUtil.e("huodong", "msg==" + msg);
                            if (!TextUtils.isEmpty(msg)) {
                                rvHuoDong.setVisibility(View.VISIBLE);
                                empty_layout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                                if (isRefresh) {
                                    adapter.clear();
                                    isRefresh = false;
                                }

                                final HuoDong bean = new Gson().fromJson(msg, HuoDong.class);

                                if (bean.state == 1) {
                                    top_view.setVisibility(View.VISIBLE);
                                    page=-1;
                                    if (bean.datas != null) {

                                        if(bean.datas.flashSale!=null){ // 抢购

                                            re_qianggou.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    Intent iy=new Intent (activity,HDongQiangGouActivity.class);
                                                    iy.putExtra("time",bean.datas.flashSale.now_time);
                                                    activity.startActivity(iy);
                                                }
                                            });

                                            if(!TextUtils.isEmpty(bean.datas.flashSale.goodsImage)){
                                                MyApplication.imageLoader.displayImage(bean.datas.flashSale.goodsImage,iv_activity_qiangguo,MyApplication.getListOptions());
                                            }
                                            if(!TextUtils.isEmpty(bean.datas.flashSale.goodsPrice)){
                                                tv_activity_price.setText(bean.datas.flashSale.goodsPrice);
                                            }

                                            if(bean.datas.activityList!=null){

                                                if(bean.datas.activityList.size()>0){

                                                    liner_activity.setVisibility(View.VISIBLE);
                                                    for(int i=0;i<bean.datas.activityList.size();i++){
                                                        final HuoDong.DatasEntry.ActivityListEntry entey=bean.datas.activityList.get(i);
                                                        if(entey!=null&&i<5){
                                                            MyApplication.imageLoader.displayImage(entey.img,logos[i],MyApplication.getListOptions());
                                                            titles[i].setText(entey.title);
                                                            des_s[i].setText(entey.descs);
                                                        }

                                                        if(entey!=null&&i==5){
                                                            MyApplication.imageLoader.displayImage(entey.img,logos[5],MyApplication.getListOptions());
                                                        }

                                                        logos[i].setOnClickListener(new View.OnClickListener(){
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent iy=new Intent (activity,ReXiaoActivity.class);
                                                                iy.putExtra("refId",entey.refId);
                                                                iy.putExtra("refImg",entey.ref_img);
                                                                activity.startActivity(iy);
                                                            }
                                                        } );



                                                    }
                                                }else {
                                                    liner_activity.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            if(bean.datas.flashSale.nextTime>0){
                                                tv_activity_nexttime.setText("下一场 "+NewsManager.getDateOnlyHour(bean.datas.flashSale.nextTime*1000)+" 开始");

                                                if (isFirst) {
                                                    countDownTimer = new mCountDownTimer(bean.datas.flashSale.nextTime*1000-(new Date().getTime()), 1000, times);
                                                    countDownTimer.start();
                                                }
                                            }
                                        }else {
                                        }
                                        if (bean.datas.choicenessActivity.size() > 0) {
                                            adapter.addAll(bean.datas.choicenessActivity);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            adapter.stopMore();
                                        }
                                    }
                                }
                            }else {
                                rvHuoDong.setVisibility(View.GONE);
                                empty_layout.setErrorType(EmptyLayout.NETWORK_ERROR);
                            }
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();

        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    public void onRefresh() {
        isFirst = false;
        page = 1;
        isRefresh = true;
        getDates();
    }

    @Override
    public void onLoadMore() {
        if (page == -1) {
            adapter.stopMore();
            return;
        }
        getDates();
    }
}