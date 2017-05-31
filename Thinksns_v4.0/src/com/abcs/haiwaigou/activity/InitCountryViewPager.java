package com.abcs.haiwaigou.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.view.XScrollView;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/21.
 */
public class InitCountryViewPager extends BaseActivity implements View.OnClickListener {
    ProgressDialog progressDialog;
    ProgressDlgUtil progressDlgUtil;
    public static ArrayList<Goods> dataList = new ArrayList<Goods>();
    String cid;
    private String country;
    public Handler handler = new Handler();
    private XScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_country_goods);
        cid = (String) getIntent().getSerializableExtra("cid");
        country = (String) getIntent().getSerializableExtra("country");
        findViewById(R.id.tljr_img_news_back).setOnClickListener(this);
        initDates();
        initScrollView();

    }


    private void initScrollView() {
        mScrollView = (XScrollView) findViewById(R.id.tljr_sy_sc);
        mScrollView.initWithContext(InitCountryViewPager.this);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setAutoLoadEnable(false);
        mScrollView.setRefreshTime(Util.getNowTime());

        mScrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {

            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoad();
                    }
                }, 2000);


                Log.i("zjz", "下拉刷新！！");
            }

            @Override
            public void onLoadMore() {
                System.out.println("加载更多");
            }
        });
    }

    private void onLoad() {
        initDates();
        mScrollView.stopRefresh();
        mScrollView.stopLoadMore();
    }

    public void initDates() {
        dataList.clear();
        ProgressDlgUtil.showProgressDlg("正在连接中...", this);
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_country_tuijian, "cid=" + cid, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("status") == 1) {
                                Log.i("zjz", "country_tuijian:连接成功");
                                JSONArray jsonArray = object.getJSONArray("msg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    final Goods g = new Goods();
                                    g.setTitle(object1.optString("title"));
                                    g.setMoney(object1.optDouble("money"));
                                    g.setPicarr(object1.optString("picarr"));
                                    g.setId(object1.optInt("id"));
                                    g.setSid(object1.optInt("sid"));
                                    dataList.add(g);

                                }
                                Intent intent = new Intent(InitCountryViewPager.this, CountryGoodsActivity.class);
                                intent.putExtra("cid", cid);
                                intent.putExtra("country", country);
                                InitCountryViewPager.this.startActivity(intent);
                                overridePendingTransition(R.anim.country_in_activity, R.anim.country_out_activity);
                                ProgressDlgUtil.stopProgressDlg();
                                finish();
//                                initView();
                                Log.i("zjz", "tuijian_goodlist=" + dataList.size());
                            } else {
                                Log.i("zjz", "country_tuijian:解析失败");
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tljr_img_news_back:
                finish();
                break;
        }
    }
}
