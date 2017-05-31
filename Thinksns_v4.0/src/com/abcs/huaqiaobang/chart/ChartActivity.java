package com.abcs.huaqiaobang.chart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.alibaba.fastjson.JSONObject;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;


public class ChartActivity extends BaseActivity implements OnClickListener {

    ViewPager viewpager;
    ChartChangeBar Chartchangebar;
    RealTimeView realtimeview;
    WeSeeView weseeview;
    FutureAskView futureaskview;
    private TextView txt_islogin, hu, hupercent, huchange, sz, szpercent, szchange,
            zhong, zhongpercent, zhongchange, chuang, chuangpercent, chuangchange;
    private String TAG = "ChartActivity";
    private ArrayList<View> list = new ArrayList<View>();
    public static String YunZhiToken = "";
    public static boolean islive = false;
    public boolean iskaishi = true;
    public View hushenview;
    public View zhongchuanview;
    private ViewFlipper zx_header_viewFlipper;
    private ReceiveBroadCast receiveBroadCast;  //广播实例
    private ImageView navigation;
    private TextView tv_chart;
    private TextView tv_kefu;
    private TextView tv_ask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tljr_activity_chart);
        islive = true;
//		getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.tljr_bj_tp));
        overridePendingTransition(R.anim.vp_top_in_activity, R.anim.vp_top_out_activity);
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        findView();
//		reflush();


        // 注册广播接收
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("Tip_ChartActivity");    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(receiveBroadCast, filter);

    }

    private void findView() {
//		map = new HashMap<String, OneGu>();
        findViewById(R.id.tljr_img_futures_join_back).setOnClickListener(this);
//		inheader();
        viewpager = (ViewPager) findViewById(R.id.tljr_vp);
        realtimeview = new RealTimeView(this, handler);
        weseeview = new WeSeeView(this);
//		WeSeeView kefu = new WeSeeView(this,TLUrl.getInstance().URL_customer_service,TLUrl.getInstance().URL_customer_service_send);
        futureaskview = new FutureAskView(this);
        list.add(realtimeview.getView());
//		list.add(kefu.getView());
        list.add(futureaskview.getView());
        list.add(weseeview.getView());

        viewpager.setAdapter(pagerAdapter);
        viewpager.setOnPageChangeListener(PagerChangeL);
        navigation = (ImageView) findViewById(R.id.im);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.WIDTH / 3, Util.px2dip(this, 3));
        navigation.setLayoutParams(params);
        tv_kefu = (TextView) findViewById(R.id.tv1);
        tv_kefu.setOnClickListener(this);
        tv_chart = (TextView) findViewById(R.id.tv2);
        tv_ask = (TextView) findViewById(R.id.tv3);
        tv_chart.setOnClickListener(this);

//        Chartchangebar = new ChartChangeBar(viewpager, this);
    }

    private PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView(list.get(position));
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }
    };
    private int currentIndex;
    private OnPageChangeListener PagerChangeL = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            Log.d("ChartChangeBar", "on PageChangL");
//	    	   Chartchangebar.onPageSelected(position);
            if (position == 0) {
                tv_kefu.setTextColor(Color.parseColor("#ff0000"));
                tv_chart.setTextColor(Color.parseColor("#000000"));
                tv_ask.setTextColor(Color.parseColor("#000000"));
            } else if (position == 1) {
                tv_ask.setTextColor(Color.parseColor("#ff0000"));
                tv_kefu.setTextColor(Color.parseColor("#000000"));
                tv_chart.setTextColor(Color.parseColor("#000000"));
            } else if (position == 2) {
                tv_kefu.setTextColor(Color.parseColor("#000000"));
                tv_ask.setTextColor(Color.parseColor("#000000"));
                tv_chart.setTextColor(Color.parseColor("#ff0000"));
            }

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int arg2) {
            // TODO Auto-generated method stub

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) navigation
                    .getLayoutParams();
            currentIndex = position;

            if (currentIndex == position) {
                lp.leftMargin = (int) (positionOffset * (Util.WIDTH * 1.0 / list.size()) + currentIndex
                        * (Util.WIDTH / list.size()));
            } else {
                lp.leftMargin = (int) (-(1 - positionOffset)
                        * (Util.WIDTH * 1.0 / list.size()) + currentIndex
                        * (Util.WIDTH / list.size()));
            }
            navigation.setLayoutParams(lp);


        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }
    };

//	private OnClickListener onclick = new OnClickListener() {
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			switch (arg0.getId()) {
//			case R.id.tljr_img_futures_join_back:
//				finish();
//				overridePendingTransition(0, R.anim.vp_bottom_out_activity);
//				break;
//
//			default:
//				break;
//			}
//		}
//	};

//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
////             if(viewpager.getCurrentItem()==1){
//// 				finish();
//// 				overridePendingTransition(R.anim.vp_top_in_activity, R.anim.vp_bottom_out_activity);
////     		}else{
//     			finish();
//     			overridePendingTransition(R.anim.vp_top_in_activity, R.anim.vp_bottom_out_activity);
////     		}
//            return false;
//        }else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }

    //获取云之讯token
    public static void getYunZhiToken(String uid, String nickName, String mobile, String avatar) {
        Log.d("ChartActivity", "get Yun uid :" + uid + "   nick :" + nickName + "   avatar :" + avatar);
        try {
            //&mobile=   &avatar=
            String param = "uid=" + uid
                    + "&nickname=" + nickName + "&mobile=13800000000" + "&avatar=" + avatar;
            Log.i("tga", "chartact===" + param);
            HttpRequest.sendPost(TLUrl.getInstance().URL_goods_base+"/HQChat/rest/user/login", param, new HttpRevMsg() {
                @Override
                public void revMsg(String msg) {
                    // TODO Auto-generated method stub
                    Log.d("ChartActivity", "match :" + msg);
                    if (!msg.equals("error")) {
                        JSONObject js = JSONObject.parseObject(msg);
                        if (js.getInteger("status") == 1) {
                            Log.d("ChartActivity", "match :" + js.getString("msg"));
                            JSONObject oj = js.getJSONObject("msg");
                            Constent.preference.edit().putString("YunZhiToken", oj.getString("token"));
                            YunZhiToken = oj.getString("token");
                            MyApplication.getInstance().self.setSSID(oj.getString("token"));
                            MyApplication.getInstance().self.setIsadmin(oj.getBooleanValue("isadmin"));
//                    		MyApplication.getInstance().getMainActivity().mHandler.sendEmptyMessage(97);
                        }
                    }
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }

        ;
    };

//	@Override
//	protected void onPause() {
//		overridePendingTransition(R.anim.vp_top_in_activity, R.anim.vp_top_out_activity);
//	};


    @Override
    protected void onResume() {
//		realtimeview.handler.postDelayed(realtimeview.run, 5000);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
//		realtimeview.handler.removeCallbacks(realtimeview.run);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
//		TooltipMgr.getInstance().onResume();
        islive = false;
        unregisterReceiver(receiveBroadCast);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tljr_img_futures_join_back:
                finish();
                overridePendingTransition(0, R.anim.vp_bottom_out_activity);
                break;

            case R.id.tv1:

                viewpager.setCurrentItem(0);
                break;
            case R.id.tv2:
                viewpager.setCurrentItem(2);
                break;
            case R.id.tv3:
                viewpager.setCurrentItem(1);
                break;

            default:
                break;
        }
    }

    public class ReceiveBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //得到广播中得到的数据，并显示出来
            if (intent.getStringExtra("data").equals("updateFuture")) {
                futureaskview.adapter.notifyDataSetChanged();
            }
        }
    }

}
