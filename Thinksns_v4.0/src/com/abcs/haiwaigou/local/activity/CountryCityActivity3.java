package com.abcs.haiwaigou.local.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.adapter.CityListAdapter2;
import com.abcs.haiwaigou.local.beans.City1;
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
 * Created by Administrator on 2016/11/22.
 */

public class CountryCityActivity3 extends Activity {

    @InjectView(R.id.local_iv_back)
    ImageView localIvBack;
    @InjectView(R.id.local_tv_seek)
    TextView localTvSeek;
    @InjectView(R.id.et_loact_seek)
    EditText etLoactSeek;
    @InjectView(R.id.local_lv)
    ListView localLv;
    CityListAdapter2 cityListAdapter2;
    Handler mHandler=new Handler();

    private ACache aCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_country_city3);
        ButterKnife.inject(this);
        aCache=ACache.get(this);

        if(TextUtils.isEmpty(aCache.getAsString("location"))){
            initData();
        }else {
            final String msg=aCache.getAsString("location");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    City1 city=new Gson().fromJson(msg,City1.class);
                    if (city!=null&&city.result==1) {

                        if(city.body.data!=null&&city.body.data.size()>0){
                            cityListAdapter2 = new CityListAdapter2(CountryCityActivity3.this,city.body.data);
                            localLv.setAdapter(cityListAdapter2);
                        }
                    }
                }
            });
        }
    }

    private void initData() {

        ProgressDlgUtil.showProgressDlg("loading...",this);

        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_area2_location, "", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                if (msg!=null) {
                        ProgressDlgUtil.stopProgressDlg();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            City1 city=new Gson().fromJson(msg,City1.class);
                            if (city.result==1) {

                                if(city.body.data!=null&&city.body.data.size()>0){
                                    cityListAdapter2 = new CityListAdapter2(CountryCityActivity3.this,city.body.data);
                                    localLv.setAdapter(cityListAdapter2);
                                }

                                if(TextUtils.isEmpty(aCache.getAsString("location"))){

                                    aCache.put("location",msg);
                                }
                            }
                        }
                    });
                }else {
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
