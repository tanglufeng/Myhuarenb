package com.abcs.haiwaigou.local.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.adapter.CityListAdapter3;
import com.abcs.haiwaigou.local.beans.City2;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by xuke on 2016/12/10
 * 显示洲,国家,城市(图片)
 * CountryCityActivity4 -> local_activity_country_city4
 */

public class CountryCityActivity4 extends Activity {

    @InjectView(R.id.local_iv_back)
    ImageView localIvBack;
    @InjectView(R.id.local_tv_seek)
    TextView localTvSeek;
    @InjectView(R.id.local_lv)
    ListView localLv;
    CityListAdapter3 cityListAdapter3;
    Handler mHandler = new Handler();

    private ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_country_city4);
        ButterKnife.inject(this);
        aCache = ACache.get(this);

        if (TextUtils.isEmpty(aCache.getAsString("location4"))) {
            initData();
        } else {
            final String msg = aCache.getAsString("location4");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    City2 city = new Gson().fromJson(msg, City2.class);
                    if (city != null && city.result == 1) {

                        if (city.body.data != null && city.body.data.size() > 0) {
                            cityListAdapter3 = new CityListAdapter3(CountryCityActivity4.this, city.body.data);
                            localLv.setAdapter(cityListAdapter3);
                        }
                    }
                }
            });
        }
    }

    private void initData() {

        ProgressDlgUtil.showProgressDlg("loading...", this);

        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_area3_location, "version=v1.0", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                if (msg != null) {
                    ProgressDlgUtil.stopProgressDlg();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            City2 city = new Gson().fromJson(msg, City2.class);
                            if (city.result == 1) {
                                if (city.body.data != null && city.body.data.size() > 0) {
                                    cityListAdapter3 = new CityListAdapter3(CountryCityActivity4.this, city.body.data);
                                    localLv.setAdapter(cityListAdapter3);
                                }

                                if (TextUtils.isEmpty(aCache.getAsString("location4"))) {
                                    aCache.put("location4", msg);
                                }
                            }
                        }
                    });
                } else {
                    ProgressDlgUtil.stopProgressDlg();
                }
            }


        });
    }

    @OnClick({R.id.local_iv_back, R.id.local_tv_seek})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_iv_back:
                finish();
                break;
            case R.id.local_tv_seek:
                break;
        }
    }
}
