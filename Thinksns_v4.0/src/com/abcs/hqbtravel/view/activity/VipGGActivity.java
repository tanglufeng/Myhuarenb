package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abcs.hqbtravel.adapter.MyViewPaAdapter;
import com.abcs.hqbtravel.entity.VIPGG;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VipGGActivity extends Activity {

      private MyViewPaAdapter adapter;

    ViewPager viewpager;
    RelativeLayout relative_close;

    private VIPGG vipBean;

    private List<String>  bannerString=new ArrayList<>();

    public List<Map<String, Object>> getData(List<VIPGG.DatasBean> banners) {
        List<Map<String, Object>> mdata = new ArrayList<Map<String, Object>>();

        for(int i=0;i<banners.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", banners.get(i).advertImg);
            map.put("view", new ImageView(this));
            map.put("flag", banners.get(i).flag);
            mdata.add(map);

            bannerString.add(banners.get(i).advertImg);
        }
        return  mdata;
    }


private Handler mHandler=new Handler();

    private void initGGPop(String city_id){

//        http://www.huaqiaobang.com/mobile/index.php?act=advert&op=find_advert&city_id=0
        HttpRequest.sendGet(TLUrl.getInstance().URL_vip_isggao, "act=advert&op=find_advert&city_id="+city_id, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(!TextUtils.isEmpty(msg)){
                            Log.e("zds_isguanggao",msg+"");

                            VIPGG vipBean=new Gson().fromJson(msg, VIPGG.class);
                            if(vipBean!=null){
                                if(vipBean.state==1){
                                    if(vipBean.datas!=null&&vipBean.datas.size()>0){
                                        adapter = new MyViewPaAdapter(VipGGActivity.this,getData(vipBean.datas));
                                        viewpager.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }
                                }else {
                                    finish();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    private   String cityId=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_vip_guanggao);

         viewpager=(ViewPager)findViewById(R.id.viewpager);
         relative_close=(RelativeLayout)findViewById(R.id.relative_close);

        if(!TextUtils.isEmpty(MyApplication.getTravelCityId())){
            cityId=MyApplication.getTravelCityId();
        }else {
            cityId="55"; // 曼谷
        }
        initGGPop(cityId);
//         vipBean =(VIPGG)getIntent().getSerializableExtra("vips");
//
//        if(vipBean!=null){
//            if(vipBean.state==1){
//
//                 bannerString.clear();
//                 adapter = new MyViewPaAdapter(getData(vipBean.datas));
//                 viewpager.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//            }
//
//        }


        relative_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }
}
