package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.hqbtravel.wedgt.MyListView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.adapter.VipQuanYiAdapter;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.model.VIP_CENTER;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class YuanHuiCenterActivity extends BaseActivity implements  View.OnClickListener {

    @InjectView(R.id.tljr_per_btn_lfanhui)
    RelativeLayout rlBack;
    @InjectView(R.id.mylistview)
    MyListView mylistview;
    @InjectView(R.id.liner_chongzhi)
    RelativeLayout liner_chongzhi;
    @InjectView(R.id.relati_shengji)
    RelativeLayout relati_shengji;
    @InjectView(R.id.relati_myorder)
    RelativeLayout relati_myorder;
    @InjectView(R.id.relati_yaoqing)
    RelativeLayout relati_yaoqing;
    @InjectView(R.id.img_huiyuan_tou)
    ImageView img_huiyuan_tou;
    @InjectView(R.id.text_addtime)
    TextView text_addtime;
    @InjectView(R.id.credit)
    TextView credit;
    @InjectView(R.id.current_quan_yi)
    TextView current_quan_yi;
    @InjectView(R.id.money)
    TextView money;
    @InjectView(R.id.sheng)
    TextView sheng;
    @InjectView(R.id.total_vip_money)
    TextView total_vip_money;
    @InjectView(R.id.tv_chuan_yaunhui)
    TextView tv_chuan_yaunhui;
    @InjectView(R.id.id_swipe_ly)
    SwipeRefreshLayout id_swipe_ly;

    private VipQuanYiAdapter vip_adapter;



    private String levelId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuan_hui_center);
        ButterKnife.inject(this);

        MyApplication.isFlashVip=false;
        setOnListener();
        EventBus.getDefault().register(this);
        inidatas();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isFinishActivity(String msg){
        Log.i("zds", "isFinishActivity: "+msg);
        if(!TextUtils.isEmpty(msg)&&msg.equals("finish_activity")){
            list.clear();
            inidatas();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    Handler handler=new Handler();
    List<HashMap<String, VIP_CENTER.IntroduceArrBean>> list=new ArrayList<HashMap<String,VIP_CENTER.IntroduceArrBean>>();
    private void inidatas(){
        ProgressDlgUtil.showProgressDlg("loading...",this);

        User self = MyApplication.getInstance().self;
        if (self != null) {
            ImageLoader.getInstance().displayImage(self.getAvatarUrl(), img_huiyuan_tou, Options.getCircleListOptions());
        }else {
            Intent intent = new  Intent(this, WXEntryActivity.class);
            intent.putExtra("isthome",true);
            startActivity(intent);
        }


        HttpRequest.sendPost(TLUrl.getInstance().URL_vips_center,"&key=" + MyApplication.getInstance().getMykey() , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                if(msg==null){
                    return;
                }
                ProgressDlgUtil.stopProgressDlg();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);
                        if(!TextUtils.isEmpty(msg)){

                            VIP_CENTER vipBean=new Gson().fromJson(msg, VIP_CENTER.class);

                            if(vipBean.code==200){

                                if(vipBean.datas!=null){



                                    if(!TextUtils.isEmpty(vipBean.datas.addTime)){
                                        if(!vipBean.datas.addTime.equals("0")){
                                            addTime=Util.format1.format(Long.valueOf(vipBean.datas.addTime)*1000);
                                        }

                                    }
                                    if(!TextUtils.isEmpty(vipBean.datas.introduce)){
                                        current_quan_yi.setText(vipBean.datas.introduce);
                                        current_quan_yi.setVisibility(View.VISIBLE);
                                    }else {
                                        current_quan_yi.setVisibility(View.GONE);
                                    }

                                    if(!TextUtils.isEmpty(vipBean.datas.endTime)){
                                        if(!vipBean.datas.endTime.equals("0")){
                                            endTime=Util.format1.format(Long.valueOf(vipBean.datas.endTime)*1000);
                                        }
                                    }

                                    if(!TextUtils.isEmpty(addTime)&&!TextUtils.isEmpty(endTime)){
                                        text_addtime.setText(addTime+"至"+endTime);
                                    }else {
                                        text_addtime.setVisibility(View.GONE);
                                    }

                                    if(!TextUtils.isEmpty(vipBean.datas.totalVipMoney)){
                                        total_vip_money.setText("("+vipBean.datas.totalVipMoney+")");
                                    }
                                    if(!TextUtils.isEmpty(vipBean.datas.money)){
                                        money.setText("("+vipBean.datas.money+")");
                                    }
                                    if(!TextUtils.isEmpty(vipBean.datas.credit)){
                                        credit.setText("("+vipBean.datas.credit+")");
                                    }

                                    if(!TextUtils.isEmpty(vipBean.datas.levelName)){
                                        tv_chuan_yaunhui.setText(vipBean.datas.levelName);
                                    }
                                    if(!TextUtils.isEmpty(vipBean.datas.levelId)){
                                        levelId=vipBean.datas.levelId;
                                        if(vipBean.datas.levelId.equals("4")){
                                            sheng.setText("已是最高会员");
                                        }
                                    }


                                    if(vipBean.introduceArr!=null){
                                        Log.e("zds_troduce_arr",vipBean.introduceArr.size()+"");
                                        if(vipBean.introduceArr.size()>0){

                                            mylistview.setVisibility(View.VISIBLE);
                                            for(int i = 0; i <vipBean.introduceArr.size(); i++){
                                                HashMap<String, VIP_CENTER.IntroduceArrBean> item = new HashMap<String, VIP_CENTER.IntroduceArrBean>();
                                                item.put("key", vipBean.introduceArr.get(i));
                                                list.add(item);
                                            }

                                            Log.e("zds_listy",list.size()+"");

                                            vip_adapter=new VipQuanYiAdapter(YuanHuiCenterActivity.this,list);
                                            mylistview.setAdapter(vip_adapter);
                                            vip_adapter.notifyDataSetChanged();

                                        }else {
                                            mylistview.setVisibility(View.GONE);
                                        }
                                    }else {
                                        mylistview.setVisibility(View.GONE);
                                    }

                                }
                            }

                        }
                    }
                });
            }
        });


    }

    private String addTime;
    private String endTime;

    private Handler mHandler=new Handler();

    private void setOnListener() {
        rlBack.setOnClickListener(this);
        liner_chongzhi.setOnClickListener(this);
        relati_shengji.setOnClickListener(this);
        relati_myorder.setOnClickListener(this);
        relati_yaoqing.setOnClickListener(this);

        //设置刷新时动画的颜色，可以设置4个
        id_swipe_ly.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        id_swipe_ly.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                inidatas();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        id_swipe_ly.setRefreshing(false);
                    }
                },1500);
            }
        });


    }
    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.tljr_per_btn_lfanhui:
                finish();
                break;
            case R.id.liner_chongzhi:  /****会员充值******/

                intent = new Intent(this, HuiYuanPayActivity.class);
                startActivity(intent);
                break;
            case R.id.relati_shengji:
                intent = new Intent(this, ShengJiHuiYuanActivity.class);
                startActivity(intent);

                break;
            case R.id.relati_myorder:
//                intent = new Intent(this, ShengJiHuiYuanActivity.class);
//                startActivity(intent);
                break;
            case R.id.relati_yaoqing:
//                intent = new Intent(this, ShengJiHuiYuanActivity.class);
//                startActivity(intent);
                break;
        }
    }

}
