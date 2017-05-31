package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.FromPopToVip;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FromPopBuildHuiYuanActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.tljr_per_btn_lfanhui)
    RelativeLayout rlBack;
    @InjectView(R.id.relative_quanyi)
    RelativeLayout relative_quanyi;
    @InjectView(R.id.relative_huodong)
    RelativeLayout relative_huodong;
    @InjectView(R.id.relative_know)
    RelativeLayout relative_know;
    @InjectView(R.id.relative_pay)
    RelativeLayout relative_pay;
    @InjectView(R.id.imge_kai_type)
    ImageView imge_kai_type;
    @InjectView(R.id.img_notice)
    ImageView img_notice;
    @InjectView(R.id.img_quanyi)
    ImageView img_quanyi;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.tv_quan_yi)
    TextView tv_quan_yi;
    @InjectView(R.id.tv_duodong)
    TextView tv_duodong;
    @InjectView(R.id.tv_xuzhi)
    TextView tv_xuzhi;
    @InjectView(R.id.tv_price)
    TextView tv_price;
    @InjectView(R.id.tv_numbers)
    TextView tv_numbers;
    @InjectView(R.id.tv_t_type)
    TextView tv_t_type;
    @InjectView(R.id.tv_t_day)  //  天
    TextView tv_t_day;
    @InjectView(R.id.tv_t_hour)  //  时
    TextView tv_t_hour;
    @InjectView(R.id.tv_t_min)  //  分
    TextView tv_t_min;
    @InjectView(R.id.tv_t_second)  //  秒
    TextView tv_t_second;

    @InjectView(R.id.id_swipe_ly)
    SwipeRefreshLayout id_swipe_ly;


    private boolean isLookHuoDong=false;
    private boolean isLookQuanYi=false;
    private boolean isLookNotice=false;

    String int_day2=null, int_hour2=null, int_minute2=null, int_second2=null;

    private int mDay = 0;
    private int mHour = 0;
    private int mMin = 0;
    private int mSecond = 0;// 天 ,小时,分钟,秒
    private String huod_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_hui_yuan_pop);
        ButterKnife.inject(this);

        huod_id=getIntent().getStringExtra("huod_id");
        setOnLick();
        if(!TextUtils.isEmpty(huod_id)){

            initVips(huod_id);
        }
    }

    private int buyState=0;
    private long end_time,start_time,time_distance;
    private String bugMsg;
    private  Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                computeTime();

                if(mDay==0){
                    int_day2="00";
                }else if(mDay<10){
                    int_day2="0"+mDay;
                }else {
                    int_day2=""+mDay;
                }

                if(mHour==0){
                    int_hour2="00";
                }else if(mHour<10){
                    int_hour2="0"+mHour;
                }else {
                    int_hour2=""+mHour;
                }

                if(mMin==0){
                    int_minute2="00";
                }else if(mMin<10){
                    int_minute2="0"+mMin;
                }else {
                    int_minute2=""+mMin;
                }

                if(mSecond==0){
                    int_second2="00";
                }else if(mSecond<10){
                    int_second2="0"+mSecond;
                }else {
                    int_second2=""+mSecond;
                }

//                Log.i("zds2",int_day2+"天"+int_hour2+"时"+int_minute2+"分"+int_second2+"秒");

                tv_t_day.setText(int_day2);
                tv_t_hour.setText(int_hour2);
                tv_t_min.setText(int_minute2);
                tv_t_second.setText(int_second2);

                if(mDay==0&&mHour==0&&mMin==0&&mSecond==0){
                    isRun=false;
                }

            }
        }
    };

    private boolean isRun = true;

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                }
            }
        }
    }

    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    FromPopToVip.VipCardBean vipCard;
    FromPopToVip.VipYygBean vipYyg;

    private void  initVips(String huod_id){

        ProgressDlgUtil.showProgressDlg("",this);
//        http://www.huaqiaobang.com/mobile/index.php?act=vip&op=vip_yyg_detail&key=939f6c2c1ad7199187be733cc714955a&id=1  id  活动的id
        HttpRequest.sendPost(TLUrl.getInstance().URL_vip_huodong_detials + MyApplication.getInstance().getMykey()+"&id="+huod_id,null , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                if(msg==null){
                    return;
                }
                ProgressDlgUtil.stopProgressDlg();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("zds_buile",msg+"");
                        if(!TextUtils.isEmpty(msg)){
                            FromPopToVip bean= new Gson().fromJson(msg,FromPopToVip.class);
                            if(bean!=null){

                                if(bean.state==1){
                                    if(bean.buyState==-1){  //  -1  不能买
                                        buyState=bean.buyState;
                                    }
                                    if(!TextUtils.isEmpty(bean.buyMsg)){
                                        bugMsg=bean.buyMsg;
                                    }

                                    if(bean.vipCard!=null){
                                       vipCard= bean.vipCard;
                                    }

                                    if(bean.vipYyg!=null){
                                         vipYyg=bean.vipYyg;

                                        if(bean.vipYyg.sum>=0){
                                            tv_numbers.setText(bean.vipYyg.sum+"");
                                        }else {
                                        }

                                        if(!TextUtils.isEmpty(bean.vipYyg.endTime)){
                                            end_time=Long.valueOf(bean.vipYyg.endTime);
                                        }
                                        if(!TextUtils.isEmpty(bean.vipYyg.startTime)){
                                            start_time=Long.valueOf(bean.vipYyg.startTime);
                                        }


                                            Date date=new Date();
                                            long current_time=date.getTime();

                                            if((start_time-current_time)>0){
                                                time_distance=start_time-current_time;  // 距离活动开始时间
                                                tv_t_type.setText("距离抢购开始还剩");
                                            }else {
                                                if(end_time>0&&current_time>0){
                                                    time_distance=end_time-current_time;   // 距离活动结束时间
                                                    tv_t_type.setText("距离抢购结束还剩");
                                                }
//                                                if(end_time>0&&start_time>0){
//                                                    time_distance=end_time-start_time;   // 距离活动结束时间
//                                                    tv_t_type.setText("距离抢购结束还剩");
//                                                }
                                              }

                                        initTime();

                                        if(!TextUtils.isEmpty(bean.vipYyg.levelId)){

                                            if(bean.vipYyg.levelId.equals("1")){
                                                imge_kai_type.setImageResource(R.drawable.huiyuan_bg);
                                                tv_title.setText(bean.vipYyg.levelName+"");
                                                tv_price.setText("￥2"+"/年 | 购买");    //￥ 10000/年 | 充值
                                            }else if(bean.vipYyg.levelId.equals("2")){
                                                imge_kai_type.setImageResource(R.drawable.img_yinh_jin_bg);
                                                tv_title.setText(bean.vipYyg.levelName+"");
                                                tv_price.setText("￥2"+"/年 | 购买");    //￥ 10000/年 | 充值
                                            }else if(bean.vipYyg.levelId.equals("3")){
                                                imge_kai_type.setImageResource(R.drawable.img_yinh_baijin_bg);
                                                tv_title.setText(bean.vipYyg.levelName+"");
                                                tv_price.setText("￥2"+"/年 | 购买");    //￥ 10000/年 | 充值
                                            }else if(bean.vipYyg.levelId.equals("4")){
                                                imge_kai_type.setImageResource(R.drawable.img_yinh_hei_bg);
                                                tv_title.setText(bean.vipYyg.levelName+"");
                                                tv_price.setText("￥2"+"/年 | 购买");    //￥ 10000/年 | 充值
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        }
                });
            }
        });
    }


    private void initTime(){
//        Date time_ends = new Date("2016/12/31 24:00:00");  // 将结束时间设定为国际标准时间
//        long time_end = time_ends.getTime();  //获得结束时间到1970/01/01之间的毫秒数
//        Date time_nows = new Date();  // 获取当前时间
//        long time_now = time_nows.getTime();  //获取当前时间到1970/01/01之间的毫秒数
//        long time_distance = time_end - time_now;  // 结束时间减去当前时间
        double int_day, int_hour, int_minute, int_second;
        if(time_distance >= 0){
            // 天时分秒换算
            int_day = Math.floor(time_distance/86400000);  //计算有多少天，向下取整
            time_distance -= int_day * 86400000;  //间隔时间减去天数
            int_hour = Math.floor(time_distance/3600000);  //计算剩余时间含有多少小时，向下取整
            time_distance -= int_hour * 3600000;  //再减去整数小时
            int_minute = Math.floor(time_distance/60000);  //计算剩余时间含有多少分钟，向下取整
            time_distance -= int_minute * 60000;  //再减去整数分钟
            int_second = Math.floor(time_distance/1000);  //计算剩余时间含有多少秒
//                // 时分秒为单数时、前面加零站位
//                if(int_day < 10)
//                    int_day2 = "0" +(int)int_day ;
//                if(int_hour < 10)
//                     int_hour2 = "0" + (int)int_hour;
//                if(int_minute < 10)
//                    int_minute2 = "0" + (int)int_minute;
//                if(int_second < 10)
//                    int_second2 ="0" + (int)int_second;

            if(int_day>0){
                mDay=(int)int_day;

            }
            if(int_hour>0){
                mHour=(int)int_hour;
            }
            if(int_minute>0){
                mMin=(int)int_minute;
            }
            if(int_second>0){
                mSecond=(int)int_second;
            }
//
//            // 显示时间
//            Log.i("zds1",int_day+"天"+int_hour+"时"+int_minute+"分"+int_second+"秒");
            startRun();
        }
    }


    private boolean isflush=false;

    @Override
    protected void onResume() {
        super.onResume();
        if(isflush){
            if(!TextUtils.isEmpty(huod_id)){
                initVips(huod_id);
            }
        }
    }

    private void setOnLick(){
        rlBack.setOnClickListener(this);
        relative_quanyi.setOnClickListener(this);
        relative_huodong.setOnClickListener(this);
        relative_know.setOnClickListener(this);
        relative_pay.setOnClickListener(this);

//        //设置刷新时动画的颜色，可以设置4个
//        id_swipe_ly.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
//
        id_swipe_ly.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if(!TextUtils.isEmpty(huod_id)){
//                    initVips(huod_id);
//                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        id_swipe_ly.setRefreshing(false);
                    }
                },500);
            }
        });


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tljr_per_btn_lfanhui:   // 返回
                finish();
                break;
            case R.id.relative_quanyi:   // 会员权益
                if(!isLookQuanYi){
                    if(!TextUtils.isEmpty(vipCard.introduce)){
                        tv_quan_yi.setVisibility(View.VISIBLE);
                        tv_quan_yi.setText(vipCard.introduce);
                    }else {
                        tv_quan_yi.setVisibility(View.GONE);
                    }
                    isLookQuanYi=true;
                    img_quanyi.setImageResource(R.drawable.img_up);
                }else {
                    tv_quan_yi.setVisibility(View.GONE);
                    img_quanyi.setImageResource(R.drawable.img_yinh_back);
                    isLookQuanYi=false;
                }

                break;
            case R.id.relative_huodong:   // 活动详情
                if(!isLookHuoDong){
                                  /*活动详情*/
                    if(!TextUtils.isEmpty(vipYyg.descs)){
                        tv_duodong.setVisibility(View.VISIBLE);
                        tv_duodong.setText(vipYyg.descs);
                    }else {
                        tv_duodong.setVisibility(View.GONE);
                    }
                    isLookHuoDong=true;
                    img_quanyi.setImageResource(R.drawable.img_up);
                }else {
                    tv_duodong.setVisibility(View.GONE);
                    img_quanyi.setImageResource(R.drawable.img_yinh_back);
                    isLookHuoDong=false;
                }
                break;
            case R.id.relative_know:   // 用户须知
                if(!isLookNotice){

                    if(!TextUtils.isEmpty(vipCard.notice)){
                        tv_xuzhi.setVisibility(View.VISIBLE);
                        tv_xuzhi.setText(vipCard.notice);
                    }else {
                        tv_xuzhi.setVisibility(View.GONE);
                    }
                    isLookNotice=true;
                    img_notice.setImageResource(R.drawable.img_up);
                }else {
                    img_notice.setImageResource(R.drawable.img_yinh_back);
                    isLookNotice=false;
                    tv_xuzhi.setVisibility(View.GONE);
                }

                break;
            case R.id.relative_pay:   // 会员升级  充值

                if(buyState==-1){
                    if(!TextUtils.isEmpty(bugMsg)){
                        showToast(bugMsg);
                    }
                }else {

                    Intent iw=new Intent (this,HuiYuanPayActivity2.class);
                    iw.putExtra("title",vipCard.levelName+"");
                    iw.putExtra("levelid",vipCard.levelId+"");
                    iw.putExtra("huoid",vipYyg.id+"");
                    iw.putExtra("money","2");
                    iw.putExtra("isfrompop",true);
                    startActivity(iw);

                    isflush=true;
                }

                break;
        }
    }
}
