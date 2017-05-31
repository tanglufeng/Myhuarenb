package com.abcs.haiwaigou.local.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GaoErFuActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.local.evenbus.CityChange;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.hqbtravel.adapter.AddServerGridVAdapter;
import com.abcs.hqbtravel.entity.HuiYuanTeDian;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.ShengJiHuiYuanActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LocalTeDianFragment extends BaseFragment  {


    @InjectView(R.id.liner_huiyuantedian)
    LinearLayout linerHuiyuantedian;
    @InjectView(R.id.linner_tedian_datas)
    MyGridView linner_tedian_datas;
    @InjectView(R.id.linner_tedian)
    LinearLayout linner_tedian;
    @InjectView(R.id.tv_null)
    TextView tv_null;

    private Activity activity;
    ACache aCache;
    private View view;
    private AddServerGridVAdapter tedianAdapter;
    Handler mhandle = new Handler();

    public static LocalTeDianFragment newInstance(String cityId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cityId", cityId);
        LocalTeDianFragment countryFragment = new LocalTeDianFragment();
        countryFragment.setArguments(bundle);

        return countryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_travel_huiyuan_tedian, container,false);
        activity = getActivity();
        aCache = ACache.get(activity);
      /*  ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);*/

        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityId = bundle.getString("cityId");
            if(!TextUtils.isEmpty(aCache.getAsString("local_ddetian"))){
                getDeDianDatas(aCache.getAsString("local_ddetian"));
            }else {
                initDatas(cityId);
            }
        }
        return view;
    }

    String cityId;

    private void initDatas(final String city){
        if(!TextUtils.isEmpty(city)){
            //        http://www.huaqiaobang.com/mobile/index.php?act=vip_td&op=find_vip_td&city_id=55&type=2
            HttpRequest.sendGet(TLUrl.getInstance().getInstance().URL_travel_tedian,"act=vip_td&op=find_vip_td&city_id="+city+"&type=2",new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {

                    mhandle.post(new Runnable() {
                        @Override
                        public void run() {
                            if (msg == null) {
                                return ;
                            }

                            Log.i("zds", "tedian=" + msg);
                            Log.i("zds", "tedian  url=" + TLUrl.getInstance().getInstance().URL_travel_tedian+"?"+"act=vip_td&op=find_vip_td&city_id="+city+"&type=2");
                            getDeDianDatas(msg);
                            aCache.remove("local_ddetian");
                            aCache.put("local_ddetian",msg);
                        }
                    });
                }
            });
        }
    }


    private void getDeDianDatas(String msg) throws NullPointerException{
        final HuiYuanTeDian tedian=new Gson().fromJson(msg, HuiYuanTeDian.class);
        if(tedian!=null){
            if(tedian.state==1){
                if(tedian.vipTdList!=null&&tedian.vipTdList.size()>0){
                    linner_tedian.setVisibility(View.VISIBLE);
                    tv_null.setVisibility(View.GONE);


                    linner_tedian_datas.setAdapter(tedianAdapter=new AddServerGridVAdapter(activity,tedian.vipTdList));
                    tedianAdapter.notifyDataSetChanged();

                    linner_tedian_datas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            final HuiYuanTeDian.VipTdListBean bean=(HuiYuanTeDian.VipTdListBean) adapterView.getItemAtPosition(i);



                            if(Integer.valueOf(bean.jumpId)>0){   //专题
                                Intent intent= new Intent(activity, GaoErFuActivity.class);
                                intent.putExtra("words",bean.tdDesc+"");
                                intent.putExtra("title",bean.tdEnName+bean.tdCnName);   //"Golf Trip\n"+"您的高尔夫记忆"
                                intent.putExtra("text_position", 0);
                                intent.putExtra("picture", bean.tdDescImg+"");
                                intent.putExtra("special_id",bean.jumpId+"");
                                intent.putExtra("city_id",bean.cityId+"");
                                intent.putExtra("isMain",false);
                                intent.putExtra("isSale",false);
                                intent.putExtra("isWeek",false);
                                startActivity(intent);
                            }else {  //商品详情
                                Intent it=new Intent(activity,GoodsDetailActivity.class);
                                it.putExtra("sid",bean.goodsId);
                                it.putExtra("pic", bean.img);
                                it.putExtra("isYun",false);
                                startActivity(it);
                            }
                        }
                    });
                }
            }else {
                linner_tedian.setVisibility(View.GONE);
                tv_null.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCityChange(final CityChange cityChange){
        Log.d("zds", "initDatas: cityChange.city"+cityChange.city);

        initDatas(cityChange.city);
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.liner_huiyuantedian, R.id.linner_tedian})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liner_huiyuantedian: /*****会员特典******/
                if(MyApplication.getInstance().getMykey()==null){
                    Intent intent=new Intent(activity,WXEntryActivity.class);
                    intent.putExtra("isthome",true);
                    startActivity(intent);
                }else {
                    Intent irre=new Intent(activity,ShengJiHuiYuanActivity.class);
                    startActivity(irre);
                }
                break;
            case R.id.linner_tedian:
                break;
        }
    }
}
