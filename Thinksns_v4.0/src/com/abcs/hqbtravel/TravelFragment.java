package com.abcs.hqbtravel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GaoErFuActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.KefuActivity;
import com.abcs.haiwaigou.local.beans.City1;
import com.abcs.hqbtravel.adapter.AddServerGridVAdapter;
import com.abcs.hqbtravel.adapter.CeSuAdapter;
import com.abcs.hqbtravel.adapter.CityGridViewAdapter;
import com.abcs.hqbtravel.adapter.FuJinJingDianAdapter;
import com.abcs.hqbtravel.adapter.ResturantAdapter;
import com.abcs.hqbtravel.adapter.ShopAdapter;
import com.abcs.hqbtravel.biz.TravelBiz;
import com.abcs.hqbtravel.entity.HuiYuanTeDian;
import com.abcs.hqbtravel.entity.MyTickets;
import com.abcs.hqbtravel.entity.RestauransBean;
import com.abcs.hqbtravel.entity.ShopBean;
import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.hqbtravel.entity.TravelResponse;
import com.abcs.hqbtravel.view.activity.BiChiActivity;
import com.abcs.hqbtravel.view.activity.BiChiDetialsActivity;
import com.abcs.hqbtravel.view.activity.BiMaiActivity;
import com.abcs.hqbtravel.view.activity.BiMaiDetialsActivity;
import com.abcs.hqbtravel.view.activity.BiWanActivity2;
import com.abcs.hqbtravel.view.activity.BiWanDetialsActivity;
import com.abcs.hqbtravel.view.activity.CountryCityActivityChange;
import com.abcs.hqbtravel.view.activity.DaoHangActivity2;
import com.abcs.hqbtravel.view.activity.DaoHangDetialsActivity2;
import com.abcs.hqbtravel.view.activity.FuJinSanBiActivity;
import com.abcs.hqbtravel.view.activity.MainMapActivity;
import com.abcs.hqbtravel.view.activity.SeachYouJiByTagActivity;
import com.abcs.hqbtravel.view.activity.YouJiActivity;
import com.abcs.hqbtravel.view.activity.ZhuLiActivity;
import com.abcs.hqbtravel.wedgt.ACache;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.hqbtravel.wedgt.MyListView;
import com.abcs.hqbtravel.wedgt.SpeakDialog;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.ShengJiHuiYuanActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.MainScrollView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.ytbt.common.utils.ToastUtil;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.bean.CeSu;
import com.abcs.sociax.t4.android.fragment.FragmentSociax;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.abcs.sociax.android.R.id.tgt;
import static com.abcs.sociax.t4.android.video.ToastUtils.showToast;

public class TravelFragment extends FragmentSociax implements View.OnClickListener {

    private ActivityHome activity;
    private TextureMapView mapView;
    private MainScrollView scrollview;

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout line_jindian,linner_ttop,linner_yuyin,linner_tedian,liner_top_zhenxuan,liner_container;
    private LinearLayout line_shop;
    private LinearLayout line_restauants,liner_huiyuantedian;
    private RelativeLayout relative_gotomap;
    private MyGridView linner_tedian_datas;

    private boolean isChangeCity=false;
    ImageView gotomap,cesu;

    private EditText search;
    private ImageView img_main_logo,gogog;

    TextView tv_shop;
    TextView tv_restauants,tv_loca_wendu;

    TextView tv_moreshop,tv_top_zhenxuan;
    TextView tv_morerestauants;
    TextView tv_moretourist;
    TextView tv_moreserver;

    TextView tv_touristcount;
    TextView tv_city,tv_locaname;

    ImageView img_search_down,img_ddown_down;
    TextView bichi_count,biwan_count,bimai_count,youji_count,yuyin_count;
    TextView tv_jingdiancount;
    TextView tv_name_jingdian1,tv_peoples;

    RelativeLayout re1_first;
    RelativeLayout re2_first;
    RelativeLayout re3_first;
    RelativeLayout re4_first;

    LinearLayout re_fuqu1;
    private View lin_to_map;
    MyListView re_shop;
    MyListView re_restauants;

    RelativeLayout re2_daohang;
    RelativeLayout re3_youji,relati_chang;

    private Handler mhandle=new Handler();
    private MyListView grid_fujin_jingtian;
    private FuJinJingDianAdapter fuJinJingDianAdapter;

    private ACache aCache;
    private ListView listview_container;

    private List<String > titlrs=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        activity= (ActivityHome) getActivity();
        aCache= ACache.get(activity);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_travel;
    }

    private void initLocationDialog(double lat, double lng){

//        http://120.24.19.29:7075/find/getCityBylnglat?lng=22.123456&lat=114.231234

//            HttpRequest.sendGet(Contonst.HOST+"/find/getCityBylnglat", "lat=" + "13.7467543000" + "&lng=" + "100.5328421000", new HttpRevMsg() {
            HttpRequest.sendGet(Contonst.HOST+"/find/getCityBylnglat", "lat=" + lat + "&lng=" + lng, new HttpRevMsg() {

                @Override
                public void revMsg(final String msg) {
                    mhandle.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(!TextUtils.isEmpty(msg)){
                                    JSONObject mainObject = new JSONObject(msg);

                                    if(mainObject.optInt("result")==1){

                                        JSONObject bodyObject =mainObject.optJSONObject("body");
                                        if(bodyObject!=null){
//                                        "cityId":12871,"cityName":"测试内容6ic7"
                                            final String obcityId=bodyObject.optString("cityId");
                                            String address=bodyObject.optString("cityName");

                                            Log.i("zds", "address=" + address);

                                            View root_view=View.inflate(activity, R.layout.item_travel_location,null);
                                            ImageView img_close=(ImageView) root_view.findViewById(R.id.img);
                                            LinearLayout lon4=(LinearLayout) root_view.findViewById(R.id.lon4);
                                            RelativeLayout tyt=(RelativeLayout) root_view.findViewById(R.id.tyt);
                                            TextView tv_location_name=(TextView) root_view.findViewById(R.id.tv_location_name);
                                            TextView tv_wraning=(TextView) root_view.findViewById(R.id.tv_wraning);
                                            TextView tv_location_name2=(TextView) root_view.findViewById(R.id.tv_location_name2);
                                            TextView tv_location_name3=(TextView) root_view.findViewById(R.id.tv_location_name3);

                                            if(!TextUtils.isEmpty(address)){
                                                tv_location_name.setText("定位显示你在"+address+",你可以...");
                                                tv_location_name2.setText("切换到"+address);
                                                tv_wraning.setText("定位错了，我不在"+address+"!");
                                            }
                                            if(!TextUtils.isEmpty(MyApplication.getCityName())){
                                                tv_location_name3.setText("继续浏览"+ MyApplication.getCityName());
                                            }else {
                                                tv_location_name3.setText("继续浏览曼谷");
                                            }

                                            if(obcityId.equals(MyApplication.getTravelCityId())){
                                                return;
                                            }else {
                                                final PopupWindow popupWindow = new PopupWindow(root_view, Util.WIDTH * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                                                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                                                params.alpha = 0.5f;
                                                activity.getWindow().setAttributes(params);
                                                popupWindow.setTouchable(true);
                                                popupWindow.setOutsideTouchable(true);
                                                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        return false;
                                                    }
                                                });
                                                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                                    @Override
                                                    public void onDismiss() {
                                                        WindowManager.LayoutParams params = activity.getWindow()
                                                                .getAttributes();
                                                        params.alpha = 1f;
                                                        activity.getWindow().setAttributes(params);
                                                    }
                                                });
                                                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));
                                                popupWindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);

                                                tyt.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        popupWindow.dismiss();
                                                    }
                                                });
                                                lon4.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        popupWindow.dismiss();
                                                        Intent city=new Intent(activity,CountryCityActivityChange.class);
                                                        startActivityForResult(city,0);
                                                        isChangeCity=true;
                                                    }
                                                });

                                                tv_location_name2.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
//
                                                        popupWindow.dismiss();
                                                        if(!TextUtils.isEmpty(obcityId)){
                                                            cityId=obcityId;
                                                            getTravel();
                                                        }
//                                                        initZhenXuan();
//
//                                                        liner_top_zhenxuan.setVisibility(View.GONE);
//                                                        liner_container.setVisibility(View.GONE);
//
//                                                        tv_top_zhenxuan.setVisibility(View.VISIBLE);
//                                                        listview_container.setVisibility(View.VISIBLE);

                                                    }
                                                });
                                                tv_location_name3.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        popupWindow.dismiss();
                                                    }
                                                });
                                            }
                                        }
                                    }else {
                                        ToastUtil.showMessage("服务器开了一点小差！");
                                    }
                                }
//                                /****************原来的接口********************/
//                                Log.i("zds", "getGPS_msg=" + msg);
//                                if (mainObject.optString("status").equals("1")) {
//                                    JSONObject msgObj = mainObject.optJSONObject("msg");
//                                    if (msgObj != null) {
//                                        StringBuilder stringBuffer = new StringBuilder();
//                                        JSONObject googleObj = msgObj.optJSONObject("google");
//                                        if (googleObj != null) {
//                                            JSONObject googleResult = googleObj.optJSONObject("result");
//                                            if (googleResult != null) {
//                                                stringBuffer.append("Google：\n");
//                                                googleResult.optString("address");
//                                                googleResult.optString("city");
//                                                location_city=googleResult.optString("city");
//                                                googleResult.optString("country");
//                                                googleResult.optString("description");
//                                                googleResult.optString("district");
//                                                googleResult.optString("lat");
//                                                googleResult.optString("lng");
//                                                googleResult.optString("province");
//                                                googleResult.optString("type");
//                                                stringBuffer.append("当前位置：\n")
//                                                        .append("Country:" + googleResult.optString("country") + "\n")
//                                                        .append("Province:" + googleResult.optString("province") + "\n")
//                                                        .append("City:" + googleResult.optString("city") + "\n")
//                                                        .append("District:" + googleResult.optString("district") + "\n")
//                                                        .append("Address:" + googleResult.optString("address") + "\n");
//                                            }
//                                        } else {
//                                            stringBuffer.append("Google没有信息\n");
//                                        }
//                                        JSONObject baiduObj = msgObj.optJSONObject("baidu");
//                                        if (baiduObj != null) {
//                                            JSONObject baiduResult = baiduObj.optJSONObject("result");
//                                            if (baiduResult != null) {
//                                                stringBuffer.append("百度：\n");
//                                                baiduResult.optString("address");
//                                                baiduResult.optString("city");
//                                                location_city=baiduResult.optString("city");
//                                                baiduResult.optString("country");
//                                                baiduResult.optString("description");
//                                                baiduResult.optString("district");
//                                                baiduResult.optString("lat");
//                                                baiduResult.optString("lng");
//                                                baiduResult.optString("province");
//                                                baiduResult.optString("type");
//                                                stringBuffer.append("当前位置：\n")
//                                                        .append("Country:" + baiduResult.optString("country") + "\n")
//                                                        .append("Province:" + baiduResult.optString("province") + "\n")
//                                                        .append("City:" + baiduResult.optString("city") + "\n")
//                                                        .append("District:" + baiduResult.optString("district") + "\n")
//                                                        .append("Address:" + baiduResult.optString("address") + "\n");
//                                            }
//                                        } else {
//                                            stringBuffer.append("百度没有信息\n");
//                                        }

//                                        String addr = stringBuffer.toString();



//                                        new ShowMessageDialog(root_view, activity, Util.WIDTH * 4 / 5, addr, "当前定位");

//                                    }
//
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                ProgressDlgUtil.stopProgressDlg();
                            }
                        }
                    });
                }
            });
        
    }

//    public void changeCity(String cityName){
//        ProgressDlgUtil.showProgressDlg("loading...",activity);
////        http://120.24.19.29:7075/find/getCityIdByName?cityName=维也纳;
//
//        HttpRequest.sendGet("http://120.24.19.29:7075/find/getCityIdByName","cityName="+cityName,new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//
//                ProgressDlgUtil.stopProgressDlg();
//                mhandle.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(msg==null){
//                            return;
//                        }
//
//                        Log.i("zds", "msg=" + msg);
//                        if(!TextUtils.isEmpty(msg)){
//
//                                    try{
//                                        JSONObject jsonObject=  new JSONObject(msg);
//                                        if(jsonObject!=null){
//                                            if(jsonObject.optInt("result")==1){
//                                                JSONObject bodyObject=  jsonObject.optJSONObject("body");
//                                                String ccti=bodyObject.optString("cityId");
//                                                if(!TextUtils.isEmpty(ccti)){
//                                                    Log.i("zds", "ccti_cityId=" + ccti);
//                                                    cityId=ccti;
//                                                    getTravel();
//                                                }
//                                            }
//                                        }
//                                    }catch (JSONException e){
//                                        e.printStackTrace();
//                                    }
//                        }
//                    }
//                });
//            }
//        });
//    }

    private BaiduMap mBaiduMap;
    @Override
    public void initView() {
        cityId= MyApplication.getTravelCityId();
        if(TextUtils.isEmpty(cityId)){
            cityId="55";   //模拟数据  正式发布时替换   泰国曼谷
        }

        aCache= ACache.get(activity);
        search=(EditText)findViewById(R.id.search);
        search.setFocusable(false);

//        scroll_banner=(ScrollBanner) findViewById(R.id.scroll_banner);
//        tv_photo_numbers=(TextView) findViewById(R.id.tv_photo_numbers);
//        tv_photo1_numbers=(TextView) findViewById(R.id.tv_photo1_numbers);
//        tv_photo2_numbers=(TextView) findViewById(R.id.tv_photo2_numbers);
        gotomap=(ImageView) findViewById(R.id.gotomap);
        cesu=(ImageView) findViewById(R.id.cesu);

        gogog=(ImageView) findViewById(R.id.gogog);
        img_main_logo=(ImageView) findViewById(R.id.img_main_logo);

        grid_fujin_jingtian= (MyListView) findViewById(R.id.grid_fujin_jingtian);
        listview_container= (ListView) findViewById(R.id.listview_container);


        relati_chang= (RelativeLayout)findViewById(R.id.relati_chang);


        re1_first= (RelativeLayout)findViewById(R.id.rel_first);
        re2_first= (RelativeLayout)findViewById(R.id.re2_first1);
        re3_first= (RelativeLayout)findViewById(R.id.re3_first1);
        re4_first= (RelativeLayout)findViewById(R.id.re4_first1);


        re_fuqu1= (LinearLayout) findViewById(R.id.re_fuqu1);
        lin_to_map= (View) findViewById(R.id.lin_to_map);
        re_shop= (MyListView) findViewById(R.id.re_shop);
        re_restauants= (MyListView) findViewById(R.id.re_restauants);

        liner_huiyuantedian= (LinearLayout) findViewById(R.id.liner_huiyuantedian);
        relative_gotomap= (RelativeLayout) findViewById(R.id.relative_gotomap);
        tv_top_zhenxuan= (TextView) findViewById(R.id.tv_top_zhenxuan);
        linner_ttop= (LinearLayout) findViewById(R.id.linner_ttop);
        liner_top_zhenxuan= (LinearLayout) findViewById(R.id.liner_top_zhenxuan);
        liner_container= (LinearLayout) findViewById(R.id.liner_container);
        linner_yuyin= (LinearLayout) findViewById(R.id.linner_yuyin);
        linner_tedian_datas= (MyGridView) findViewById(R.id.linner_tedian_datas);
        linner_tedian= (LinearLayout) findViewById(R.id.linner_tedian);
        line_jindian= (LinearLayout) findViewById(R.id.line_jindian);
        line_shop= (LinearLayout) findViewById(R.id.line_shop);
        line_restauants= (LinearLayout) findViewById(R.id.line_restauants);

        scrollview = (MainScrollView) findViewById(R.id.scrollview);

        mapView = (TextureMapView) findViewById(R.id.mapView);

        mapView.showZoomControls(false);
        mapView.showScaleControl(false);
        mBaiduMap =mapView.getMap();
        UiSettings uiSettings= mBaiduMap.getUiSettings();
        uiSettings.setZoomGesturesEnabled(false);//设置是否允许缩放手势
        uiSettings.setScrollGesturesEnabled(false);//设置是否允许拖拽手势
//
//        View child = mapView.getChildAt(1);
//        // 隐藏百度logo和缩放控件ZoomControl
//        if (child instanceof ZoomControls) {
//            child.setVisibility(View.INVISIBLE);
//        }

        scrollview.setOnScroll(new MainScrollView.OnScroll() {
            @Override
            public void onScrollListener(int x, int y, int oldx, int oldy) {
                if(y>=50){
//                    linner_ttop.setBackgroundColor(getResources().getColor(R.color.white));
//                    linner_ttop.setAlpha(0.4f);
                    gotomap.setImageResource(R.drawable.img_kefus_select);
                    cesu.setImageResource(R.drawable.img_cesu_s);
                    img_search_down.setImageResource(R.drawable.img_sercher_do);
                    img_ddown_down.setImageResource(R.drawable.img_changcity_downs);
                    tv_city.setTextColor(getResources().getColor(R.color.gray04));
                    linner_ttop.setBackgroundResource(R.drawable.fuchuangbackground);
                }else {
                    gotomap.setImageResource(R.drawable.img_kefus);
                    cesu.setImageResource(R.drawable.img_cesu);
                    img_search_down.setImageResource(R.drawable.img_sercher);
                    img_ddown_down.setImageResource(R.drawable.img_changcity);
                    tv_city.setTextColor(getResources().getColor(R.color.white));
                    linner_ttop.setBackgroundColor(getResources().getColor(R.color.transparent));
                }

            }
        });



        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTravel();
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        re2_daohang= (RelativeLayout)findViewById(R.id.re2_daohang);
        re3_youji= (RelativeLayout)findViewById(R.id.re3_youji);

        tv_peoples= (TextView)findViewById(R.id.tv_peoples);


        bichi_count= (TextView)findViewById(R.id.bichi_count);
        biwan_count= (TextView)findViewById(R.id.biwan_count);
        bimai_count= (TextView)findViewById(R.id.bimai_count);
        youji_count= (TextView)findViewById(R.id.youji_count);
        yuyin_count= (TextView)findViewById(R.id.yuyin_count);

        tv_city= (TextView)findViewById(R.id.tv_city);
        img_search_down= (ImageView)findViewById(R.id.img_search_down);
        img_ddown_down= (ImageView)findViewById(tgt);
        tv_locaname= (TextView)findViewById(R.id.tv_locaname);

        if(!TextUtils.isEmpty(MyApplication.getCityName())){
            tv_city.setText(MyApplication.getCityName());
            tv_locaname.setText(MyApplication.getCityName());
        }else {
            tv_city.setText("曼谷");
        }

        tv_moretourist= (TextView)findViewById(R.id.tv_moretourist);
        tv_moreserver= (TextView)findViewById(R.id.tv_moreserver);

        tv_moreshop= (TextView)findViewById(R.id.tv_moreshop);
        tv_morerestauants= (TextView)findViewById(R.id.tv_morerestauants);

        tv_shop= (TextView)findViewById(R.id.tv_shop);
        tv_restauants= (TextView)findViewById(R.id.tv_restauants);
        tv_loca_wendu= (TextView)findViewById(R.id.tv_loca_wendu);

        tv_touristcount= (TextView)findViewById(R.id.tv_touristcount);
        tv_jingdiancount= (TextView)findViewById(R.id.tv_jingdiancount);
        tv_name_jingdian1= (TextView)findViewById(R.id.tv_name_jingdian1);
    }

    @Override
    public void initIntentData() {
    }

    @Override
    public void initListener() {
        initLocation();

        relati_chang.setOnClickListener(this);
        lin_to_map.setOnClickListener(this);

        liner_huiyuantedian.setOnClickListener(this);

        relative_gotomap.setOnClickListener(this);
        gogog.setOnClickListener(this);
        gotomap.setOnClickListener(this);
        cesu.setOnClickListener(this);

        search.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        re1_first.setOnClickListener(this);
        re2_first.setOnClickListener(this);
        re3_first.setOnClickListener(this);
        re4_first.setOnClickListener(this);

        re2_daohang.setOnClickListener(this);
        re3_youji.setOnClickListener(this);

        tv_moreshop.setOnClickListener(this);
        tv_morerestauants.setOnClickListener(this);

        tv_moretourist.setOnClickListener(this);
        tv_moreserver.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initWanSu();
        TravelResponse response=new Gson().fromJson(aCache.getAsString(ACache.MAIN),TravelResponse.class);
        if(response!=null){

            if(!TextUtils.isEmpty(MyApplication.getTravelCityId())){

                getTeDian(MyApplication.getTravelCityId());
//                getMaps();
//                initFuJinMap(MyApplication.getTravelCityId());

              /*  if(TextUtils.isEmpty(current_lat)&&TextUtils.isEmpty(current_lng)){
                    initFuJinMapLatLng(MyApplication.getTravelCityId());
                }*/

            }  // 保存cityId

            if(!TextUtils.isEmpty(response.body.travelNum)){
                tv_peoples.setText(response.body.travelNum+"");
            }else {
                tv_peoples.setText("26,790");
            }

            if(response.body.audios!=null){
                initYuYins(response.body.audios);
            }

            if(response.body.banners!=null&&response.body.banners.size()>0){
                initBanners(response.body.banners);
            }

            getTravelTourist(response.body.touristAttractions);
            getShop(response.body.shops);
            getRestaurant(response.body.restaurants);

            if(response.body.travelNoteCount>0){
                youji_count.setVisibility(View.VISIBLE);
                youji_count.setText(response.body.travelNoteCount+"");
            }
            if(response.body.audioCount>0){
                yuyin_count.setVisibility(View.VISIBLE);
                yuyin_count.setText(response.body.audioCount+"");
            }else{
                yuyin_count.setVisibility(View.INVISIBLE);
            }


            if(!TextUtils.isEmpty(response.body.highEstTemperature)){
                tv_loca_wendu.setText(response.body.highEstTemperature);
            }else {
                tv_loca_wendu.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(Util.preference.getString("tittle",""))){

                String []aeery= Util.preference.getString("tittle","").split(",");
                if(aeery!=null&&aeery.length>0){
                    for(int i=0;i<aeery.length;i++){
                        titlews.add(aeery[i]);
                    }
                }
            }

        }else {

            if (!ServerUtils.isConnect(activity)) {
                mhandle.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showToast("请检查您的网络");
                    }
                });
                return;
            }else {
                if(!TextUtils.isEmpty(cityId)){
                    ProgressDlgUtil.showProgressDlg("loading...",activity);
                    getTravel();
                    getTeDian(cityId);
                }
            }
        }

    }

    private  final List<CeSu> ceSuList=new ArrayList<>();

    private void initWanSu(){
        ceSuList.clear();
//        http://www.huaqiaobang.com/mobile/index.php?act=test_cy&op=find_server

        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php","act=test_cy&op=find_server",new HttpRevMsg(){
            @Override
            public void revMsg(final String msg) {

                mhandle.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("cesu",msg+"");
                        if(!TextUtils.isEmpty(msg)){

                            try {
                                JSONArray jsonArray=new JSONArray(msg);
                                Log.i("cesujsonArray",jsonArray.length()+"");
                                if(jsonArray!=null&&jsonArray.length()>0){
                                    for (int i=0;i<jsonArray.length();i++){
                                        final JSONObject object1 = jsonArray.getJSONObject(i);
                                        final CeSu ceSu=new CeSu();
                                        Log.i("cesuname",object1.optString("server_name")+"");
                                        Log.i("cesuurl",object1.optString("server_url")+"");

                                        ceSu.setId(object1.optString("id"));
                                        ceSu.setServerName(object1.optString("server_name"));
                                        ceSu.setServerUrl(object1.optString("server_url"));

                                        ceSuList.add(ceSu);
                                    }
                                    MyApplication.setCeSuList(ceSuList);
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
    }


private List<String > titlews=new ArrayList<>();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    private String cityId="55";
    private List<TouristAttractionsBean> newTourists=new ArrayList<>();
    private void getTravelTourist(List<TouristAttractionsBean> touristAttractions){

        if(touristAttractions!=null){

            if(touristAttractions.size()>0){
                line_jindian.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(MyApplication.getBiWanNum())){

                    tv_jingdiancount.setText("必玩("+ MyApplication.getBiWanNum()+")");
                    biwan_count.setVisibility(View.VISIBLE);
                    biwan_count.setText(MyApplication.getBiWanNum());
                }
                if(touristAttractions.size()>0&&touristAttractions.size()<7){
                    newTourists.clear();
                    newTourists.addAll(touristAttractions);
                    fuJinJingDianAdapter=new FuJinJingDianAdapter(activity,newTourists);
                }else {
                    newTourists.clear();
                    for(int i=0;i<6;i++){
                        newTourists.add(touristAttractions.get(i));
                    }
                    fuJinJingDianAdapter=new FuJinJingDianAdapter(activity,newTourists);
                }
                grid_fujin_jingtian.setAdapter(fuJinJingDianAdapter);
                grid_fujin_jingtian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TouristAttractionsBean item=(TouristAttractionsBean)fuJinJingDianAdapter.getItem(position);
                        Intent it1=new Intent(activity,BiWanDetialsActivity.class);
                        it1.putExtra("bwanId",item.id);
                        it1.putExtra("cityId", cityId);
                        it1.putExtra("photo",item.photo);
                        startActivity(it1);
                    }
                });
            }else {
                line_jindian.setVisibility(View.GONE);
            }
        }
    }

    List<Marker> myMarkers=new ArrayList<>();

    private void initFMap(List<RestauransBean> RestaList){
        if(myMarkers!=null&&myMarkers.size()>0){
            for (int i=0;i<myMarkers.size();i++){
                myMarkers.get(i).remove();
            }
        }

        
        for (int i=0;i<RestaList.size();i++){

            if(i<3){
                String lng=RestaList.get(i).lng;
                String lat=RestaList.get(i).lat;

                if(!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(lat)){

                    LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                    MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(point, 14);
                    mBaiduMap.setMapStatus(mapStatus);
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.bichis);
                    //            构建MarkerOption，用于在地图上添加Marker
                    MarkerOptions option = new MarkerOptions()
                            .position(point)
                            .icon(bitmap).zIndex(0).period(13);;
                    option.animateType(MarkerOptions.MarkerAnimateType.grow);
                    //            在地图上添加Marker，并显示
                    Marker marker=(Marker) mBaiduMap.addOverlay(option);
                    myMarkers.add(marker);
                }
            }
            }
    }
    private ResturantAdapter resturantAdapter;
    private String chiphoto;
    List<RestauransBean> newRestaList=new ArrayList<>();
    private void getRestaurant(final List<RestauransBean> RestaList){

        if(RestaList!=null){

            if(RestaList.size()>0){

                initFMap(RestaList);

                line_restauants.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(MyApplication.getBiChiNum())){

                    tv_restauants.setText("必吃("+ MyApplication.getBiChiNum()+")");
                    bichi_count.setVisibility(View.VISIBLE);
                    bichi_count.setText(MyApplication.getBiChiNum());
                }


                if(RestaList.size()>0&&RestaList.size()<7){
                    newRestaList.clear();
                    newRestaList.addAll(RestaList);
                    resturantAdapter=new ResturantAdapter(activity,newRestaList);

                }else {
                    newRestaList.clear();
                    for(int i=0;i<6;i++){
                        newRestaList.add(RestaList.get(i));
                    }
                    resturantAdapter=new ResturantAdapter(activity,newRestaList);
                }
                re_restauants.setAdapter(resturantAdapter);
                re_restauants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final RestauransBean restaurantsEntity=resturantAdapter.getItem(position);
                        chiphoto=restaurantsEntity.photo;
                        Intent it=new Intent(activity,BiChiDetialsActivity.class);
                        it.putExtra("chiId", restaurantsEntity.id);
                        it.putExtra("cityId", cityId);
                        it.putExtra("chiphoto", chiphoto);
                        startActivity(it);
                    }
                });
            }else {
                line_restauants.setVisibility(View.GONE);
            }
        }
    }

    private ShopAdapter shopAdapter;
    private String photo;
    List<ShopBean> newShopList=new ArrayList<>();
    private void getShop(final List<ShopBean> ShopList){

        if(ShopList!=null){

            if(ShopList.size()>0){
                line_shop.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(MyApplication.getBiMaiNum())){

                    tv_shop.setText("必买("+ MyApplication.getBiMaiNum()+")");
                    bimai_count.setVisibility(View.VISIBLE);
                    bimai_count.setText(MyApplication.getBiMaiNum());
                }

                if(ShopList.size()>0&&ShopList.size()<7){
                    newShopList.clear();
                    newShopList.addAll(ShopList);
                    shopAdapter=new ShopAdapter(activity,newShopList);

                }else {
                    newShopList.clear();
                    for(int i=0;i<6;i++){
                        newShopList.add(ShopList.get(i));
                    }
                    shopAdapter=new ShopAdapter(activity,newShopList);
                }
                re_shop.setAdapter(shopAdapter);
                re_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final ShopBean shopsEntity=shopAdapter.getItem(position);
                        photo=shopsEntity.photo;
                        Intent it=new Intent(activity,BiMaiDetialsActivity.class);
                        it.putExtra("shopId", shopsEntity.id);
                        it.putExtra("cityId", cityId);
                        it.putExtra("photo", photo);
                        startActivity(it);
                    }
                });
            }else {
                line_shop.setVisibility(View.GONE);
            }
        }
    }

    private void initBanners(List<TravelResponse.BodyBean.BannersBean> banners){

        img_main_logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        MyApplication.imageLoader.displayImage(banners.get(0).logo, img_main_logo, MyApplication.options);
    }

    public void initYuYins(List<TravelResponse.BodyBean.AudiosBean> bean){

        linner_yuyin.removeAllViews();
        if(bean!=null&&bean.size() > 0) {
            linner_yuyin.setVisibility(View.VISIBLE);
            for(int i=0;i<bean.size();i++){
                final TravelResponse.BodyBean.AudiosBean data=bean.get(i);

                View itemView=View.inflate(activity, R.layout.item_travle_tickets,null);

                ViewGroup parent = (ViewGroup) itemView.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                itemView.setLayoutParams(new ViewGroup.LayoutParams(MyApplication.getWidth()/3, ViewGroup.LayoutParams.MATCH_PARENT));

                ImageView img_logo=(ImageView) itemView.findViewById(R.id.img_logo);

                MyApplication.imageLoader.displayImage(data.logo, img_logo, MyApplication.options);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                       Intent it = new Intent(activity, DaoHangDetialsActivity2.class);
                                        it.putExtra("dahangid", data.id);
                                        startActivity(it);

                    }
                });

                linner_yuyin.addView(itemView);
            }
        }else {
            linner_yuyin.setVisibility(View.GONE);
        }
    }

    private  AddServerGridVAdapter tedianAdapter;
    private void getTeDian(final String cityId){

        Log.i("zds", "tedian cityId =" + cityId);
//        http://www.huaqiaobang.com/mobile/index.php?act=vip_td&op=find_vip_td&city_id=55&type=2
        HttpRequest.sendGet(TLUrl.getInstance().getInstance().URL_travel_tedian,"act=vip_td&op=find_vip_td&city_id="+cityId+"&type=2",new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                mhandle.post(new Runnable() {
                    @Override
                    public void run() {
                        if (msg == null) {
                            return ;
                        }

                        Log.i("zds", "tedian=" + msg);
                        Log.i("zds", "tedian  url=" + TLUrl.getInstance().getInstance().URL_travel_tedian+"?"+"act=vip_td&op=find_vip_td&city_id="+cityId+"&type=2");

                        final HuiYuanTeDian tedian=new Gson().fromJson(msg, HuiYuanTeDian.class);

                        if(tedian!=null){

                            if(tedian.state==1){

                                if(tedian.vipTdList!=null&&tedian.vipTdList.size()>0){
                                    linner_tedian.setVisibility(View.VISIBLE);

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
                            }
                        }
                    }
                });
            }
        });
    }


    SpeakDialog speakDialog=null;


    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private void checkPermission(){

        int haslocationPermission  = ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_COARSE_LOCATION");

        if(haslocationPermission != PackageManager.PERMISSION_GRANTED){   //   如果已有权限，else 会执行。否则，requestPermissions被执行来弹出请求授权对话框
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,"android.permission.ACCESS_COARSE_LOCATION")){
                showMessageOKCancel("你需要允许定位才可以使用！",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity,new String[] {"android.permission.ACCESS_COARSE_LOCATION"},REQUEST_CODE_ASK_PERMISSIONS);
                    }
                });
                return ;
            }

            ActivityCompat.requestPermissions(activity,new String[] {"android.permission.ACCESS_COARSE_LOCATION"},REQUEST_CODE_ASK_PERMISSIONS);

            Log.i("zds","checkPermission1 location==");

            return ;
        }else {
            Log.i("zds","checkPermission2 location==");
            insertDummyContact();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void insertDummyContact(){
        Intent er=new Intent(activity,MainMapActivity.class);
        if(!TextUtils.isEmpty(current_lat)&&!TextUtils.isEmpty(current_lng)){
            er.putExtra("cityId",cityId);
            er.putExtra("current_lat",current_lat);
            er.putExtra("current_lng",current_lng);
            startActivity(er);
        }else {
           return;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {   // 已经授权了
                    // Permission Granted
                    insertDummyContact();

                    Log.i("zds","location==Granted");
                } else {
                    // Permission Denied
                    Log.i("zds","location==Denied");
//                    Toast.makeText(activity, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
//                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void initCeSuPop(){

        final View root_view=View.inflate(activity, R.layout.item_travel_cesu,null);
        final TextView tv_compelete=(TextView) root_view.findViewById(R.id.tv_compelete);
        final ListView listview_cesu=(ListView) root_view.findViewById(R.id.listview_cesu);
        final RelativeLayout close=(RelativeLayout) root_view.findViewById(R.id.tyt);

        final List<CeSu> data_cesu=MyApplication.getCeSuList();

        if(data_cesu!=null&&data_cesu.size()>0){
           final  List<CeSu> data_ce=new ArrayList<>();
            data_ce.clear();
            final CeSuAdapter adapter=new CeSuAdapter(activity);

          new Thread(new Runnable() {
              @Override
              public void run() {
                  for(int i=0;i<data_cesu.size();i++){
                      CeSu da=new CeSu();
                      da.setServerName(data_cesu.get(i).getServerName());
                      da.setServerUrl(data_cesu.get(i).getServerUrl());
                      String to_time=getCurrentW(data_cesu.get(i).getServerUrl(),System.currentTimeMillis());
                      Log.i("zds","to_time=="+to_time);
                      da.setCurrWanSu(to_time+"  ms");
                      data_ce.add(da);
                  }

                  activity.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          tv_compelete.setText("测速完成，你可以...");
                          adapter.addDatas(data_ce);
                          listview_cesu.setAdapter(adapter);

                          adapter.setSelectedPosition(0);

                          final PopupWindow popupWindow = new PopupWindow(root_view, Util.WIDTH * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                          WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                          params.alpha = 0.5f;
                          activity.getWindow().setAttributes(params);
                          popupWindow.setTouchable(true);
                          popupWindow.setOutsideTouchable(true);
                          popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                              @Override
                              public boolean onTouch(View v, MotionEvent event) {
                                  return false;
                              }
                          });
                          popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                              @Override
                              public void onDismiss() {
                                  WindowManager.LayoutParams params = activity.getWindow()
                                          .getAttributes();
                                  params.alpha = 1f;
                                  activity.getWindow().setAttributes(params);
                              }
                          });
                          popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));
                          popupWindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);

                          listview_cesu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                  CeSu dabe=(CeSu)adapterView.getItemAtPosition(i);


                                  adapter.setSelectedPosition(i);
                                  adapter.notifyDataSetChanged();
                                  String basU_hua=dabe.getServerUrl().substring(dabe.getServerUrl().lastIndexOf("/")+1,dabe.getServerUrl().length());

                                  Log.i("zdstra","basU_base=="+dabe.getServerUrl());
                                  Log.i("zdstra","basU_hua=="+basU_hua);

                                  TLUrl.URL_BASE=dabe.getServerUrl();
                                  TLUrl.URL_huayouhui=basU_hua;
                                  TLUrl.getInstance().isChange=true;

                                  Log.i("zdstra","basU_base2=="+TLUrl.getInstance().getUrl());
                                  Log.i("zdstra","basU_hua2=="+TLUrl.getInstance().getHuaUrl());
                                  MyApplication.saveCurrentHost(dabe.getServerUrl());
                                  popupWindow.dismiss();

                              }
                          });

                          close.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  popupWindow.dismiss();
                              }
                          });


                      }
                  });
              }
          }).start();


        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_city:  /*****切换城市******/
            case R.id.relati_chang:  /*****切换城市******/
//                Intent city=new Intent(activity,CountryCityActivityChange.class);
//                startActivityForResult(city,0);

//                changeDial();

                if(speakDialog==null){
                    speakDialog= new SpeakDialog(activity);
                }
                speakDialog.show();

                speakDialog.setGetCity(new SpeakDialog.GetCity() {
                    @Override
                    public void setCity(City1.BodyBean.DataBean.CitysBean citysBean) {

                        Log.i("zds_ss",citysBean.cityId+""+"    "+citysBean.cateName+"");
                        cityId=citysBean.cityId+"";
                        tv_city.setText(citysBean.cateName+"");
                        tv_locaname.setText(citysBean.cateName+"");

                        getTravel();
                        getTeDian(cityId);
                    }
                });

//
//                cityId=MyApplication.getTravelCityId();
//                tv_city.setText(MyApplication.getCityName()+"");
//                tv_locaname.setText(MyApplication.getCityName()+"");
//
//                getTravel();

//                isChangeCity=true;
                break;
            case R.id.gotomap:  /*****客服******/
                startActivity(new Intent(activity, KefuActivity.class));
                break;
            case R.id.cesu:  /*****测网速******/
                initCeSuPop();
                break;
            case R.id.lin_to_map:  /*****点击去地图******/
                checkPermission();

                break;
            case R.id.gogog:  /*****附近三必******/
//                isChangeCity=false;
                Intent c=new Intent(activity,FuJinSanBiActivity.class);
                c.putExtra("cityId",cityId);
                if(!TextUtils.isEmpty(MyApplication.my_current_lat)&&!TextUtils.isEmpty(MyApplication.my_current_lng)){
                    c.putExtra("current_lat", MyApplication.my_current_lat);
                    c.putExtra("current_lng", MyApplication.my_current_lng);
                }else {
                    c.putExtra("current_lat",current_lat);
                    c.putExtra("current_lng",current_lng);
                }
                startActivity(c);
                break;
            case R.id.tv_moretourist:  /*****更多景点******/
//                isChangeCity=false;
                Intent iw=new Intent(activity,BiWanActivity2.class);
                iw.putExtra("cityId",cityId);
                if(!TextUtils.isEmpty(MyApplication.my_current_lat)&&!TextUtils.isEmpty(MyApplication.my_current_lng)){
                    iw.putExtra("current_lat", MyApplication.my_current_lat);
                    iw.putExtra("current_lng", MyApplication.my_current_lng);
                }else {
                    iw.putExtra("current_lat",current_lat);
                    iw.putExtra("current_lng",current_lng);
                }
                startActivity(iw);
                break;
            case R.id.tv_moreshop:  /*****更多必买******/
//                isChangeCity=false;
                Intent bimai=new Intent(activity,BiMaiActivity.class);
                bimai.putExtra("cityId",cityId);
                if(!TextUtils.isEmpty(MyApplication.my_current_lat)&&!TextUtils.isEmpty(MyApplication.my_current_lng)){
                    bimai.putExtra("current_lat", MyApplication.my_current_lat);
                    bimai.putExtra("current_lng", MyApplication.my_current_lng);
                }else {
                    bimai.putExtra("current_lat",current_lat);
                    bimai.putExtra("current_lng",current_lng);
                }
                startActivity(bimai);
                break;
            case R.id.tv_morerestauants:  /*****更多必吃******/
//                isChangeCity=false;
                Intent bichi=new Intent(activity,BiChiActivity.class);
                bichi.putExtra("cityId",cityId);
                if(!TextUtils.isEmpty(MyApplication.my_current_lat)&&!TextUtils.isEmpty(MyApplication.my_current_lng)){
                    bichi.putExtra("current_lat", MyApplication.my_current_lat);
                    bichi.putExtra("current_lng", MyApplication.my_current_lng);
                }else {
                    bichi.putExtra("current_lat",current_lat);
                    bichi.putExtra("current_lng",current_lng);
                }
                startActivity(bichi);
                break;
            case R.id.tv_moreserver:  /*****更多服务******/
//                isChangeCity=false;
                Intent ie=new Intent(activity,ZhuLiActivity.class);
                ie.putExtra("cityId",cityId);
                startActivity(ie);
                break;
            case R.id.liner_huiyuantedian:  /*****会员特典******/
//                isChangeCity=false;
                if(MyApplication.getInstance().getMykey()==null){
                    startActivity(new Intent(activity,WXEntryActivity.class));
                }else {
                    Intent irre=new Intent(activity,ShengJiHuiYuanActivity.class);
                    startActivity(irre);
                }
                break;
            case R.id.rel_first:  /*****旅游助理******/
//                isChangeCity=false;
                Intent i=new Intent(activity,ZhuLiActivity.class);
                i.putExtra("cityId",cityId);
                startActivity(i);
                break;
            case R.id.re2_first1:/*****必玩******/
//                isChangeCity=false;
                Intent ir=new Intent(activity,BiWanActivity2.class);
                ir.putExtra("cityId",cityId);
                if(!TextUtils.isEmpty(MyApplication.my_current_lat)&&!TextUtils.isEmpty(MyApplication.my_current_lng)){
                    ir.putExtra("current_lat", MyApplication.my_current_lat);
                    ir.putExtra("current_lng", MyApplication.my_current_lng);
                }else {
                    ir.putExtra("current_lat",current_lat);
                    ir.putExtra("current_lng",current_lng);
                }
                startActivity(ir);
                break;
            case R.id.re3_first1:/*****必吃******/
//                isChangeCity=false;
                Intent irw=new Intent(activity,BiChiActivity.class);
                irw.putExtra("cityId",cityId);
                if(!TextUtils.isEmpty(MyApplication.my_current_lat)&&!TextUtils.isEmpty(MyApplication.my_current_lng)){
                    irw.putExtra("current_lat", MyApplication.my_current_lat);
                    irw.putExtra("current_lng", MyApplication.my_current_lng);
                }else {
                    irw.putExtra("current_lat",current_lat);
                    irw.putExtra("current_lng",current_lng);
                }
                startActivity(irw);
                break;
            case R.id.re4_first1: /*****必买******/
//                isChangeCity=false;
                Intent irq=new Intent(activity,BiMaiActivity.class);
                irq.putExtra("cityId",cityId);
                if(!TextUtils.isEmpty(MyApplication.my_current_lat)&&!TextUtils.isEmpty(MyApplication.my_current_lng)){
                    irq.putExtra("current_lat", MyApplication.my_current_lat);
                    irq.putExtra("current_lng", MyApplication.my_current_lng);
                }else {
                    irq.putExtra("current_lat",current_lat);
                    irq.putExtra("current_lng",current_lng);
                }
                startActivity(irq);
                break;
            case R.id.re2_daohang:  //语音导航
//                isChangeCity=false;
                Intent itw4=new Intent(activity,DaoHangActivity2.class);
                itw4.putExtra("cityId",cityId);
                startActivity(itw4);
                break;
            case R.id.re3_youji:  //精彩游记
//                isChangeCity=false;
                Intent itq4=new Intent(activity,YouJiActivity.class);
                itq4.putExtra("cityId",cityId);
                startActivity(itq4);
                break;
            case R.id.search:  //搜索景点 美食
//                isChangeCity=false;
                Intent d=new Intent(activity,SeachYouJiByTagActivity.class);
                d.putExtra("cityId",cityId);
                d.putExtra("type","ismeishi");
                startActivity(d);
                break;
//            case R.id.img_main_logo1:  //查看更多的banner
//                if(ticketses.get(0)!=null){
//                    MyTickets myTickets=ticketses.get(0);
//                    Intent it=new Intent(activity,GoodsDetailActivity.class);
//
//                    it.putExtra("sid",myTickets.product_id);
//                    it.putExtra("pic", myTickets.img);
//                    it.putExtra("isYun",false);
//                    startActivity(it);
//                }
//
//                break;
//            case R.id.img_main_logo2:
//                if(ticketses.get(1)!=null){
//                    MyTickets myTickets=ticketses.get(1);
//                    Intent it=new Intent(activity,GoodsDetailActivity.class);
//
//                    it.putExtra("sid",myTickets.product_id);
//                    it.putExtra("pic", myTickets.img);
//                    it.putExtra("isYun",false);
//                    startActivity(it);
//                }
//                break;
//            case R.id.img_main_logo1:
//            case R.id.img_main_logo2:
//            case R.id.img_main_logo3:
//                /*********原来的************/
//                isChangeCity=false;
//                Intent de=new Intent(activity,LookPhotosActivity.class);
//                de.putExtra("cityId",cityId);
//                startActivity(de);

//                if(ticketses.get(2)!=null){
//                    MyTickets myTickets=ticketses.get(2);
//                    Intent it=new Intent(activity,GoodsDetailActivity.class);
//
//                    it.putExtra("sid",myTickets.product_id);
//                    it.putExtra("pic", myTickets.img);
//                    it.putExtra("isYun",false);
//                    startActivity(it);
//                }
//                break;
        }

    }

//    private class TestLoopAdapter extends LoopPagerAdapter {
//
//        private List<TravelResponse.BodyBean.BannersBean> banners;
//
//        public TestLoopAdapter(RollPagerView viewPager,List<TravelResponse.BodyBean.BannersBean> banners) {
//            super(viewPager);
//            this.banners = banners;
//
//        }
//
//        @Override
//        public View getView(ViewGroup container, final int position) {
//            ImageView view = new ImageView(container.getContext());
//            MyApplication.imageLoader.displayImage(banners.get(position).logo, view, MyApplication.options);
//            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            return view;
//        }
//
//        @Override
//        public int getRealCount() {
//            return banners.size();
//        }
//
//    }



    CityListAdapter2 cityListAdapter2;

    private   ListView localLv;
    private ImageView localIvBack;
    private AlertDialog doalog;
    Dialog builder=null;

    private void changeDial(){

        if(builder==null){
            builder=new Dialog(activity, R.style.dialog_changecity);
            View view=View.inflate(activity, R.layout.local_activity_country_city3,null);

             localIvBack=(ImageView) view.findViewById(R.id.local_iv_back);
            localLv=(ListView) view.findViewById(R.id.local_lv);

            builder.setContentView(view);

//            if(TextUtils.isEmpty(aCache.getAsString("location_tr"))){
                initDataDialogs();
//            }else {
//                final String msg=aCache.getAsString("location_tr");
//                mhandle.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        City1 city=new Gson().fromJson(msg,City1.class);
//                        if (city.result==1) {
//
//                            if(city.body.data!=null&&city.body.data.size()>0){
//                                cityListAdapter2 = new CityListAdapter2(activity,city.body.data);
//                                localLv.setAdapter(cityListAdapter2);
//                                cityListAdapter2.notifyDataSetChanged();
//                            }
//                        }
//                    }
//                });
//            }

        }

//       if(doalog==null){
//           doalog=builder.create();
//           doalog.setCanceledOnTouchOutside(false);
//       }

        builder.show();



//        windowDeploy();

        localIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doalog.dismiss();
            }
        });

    }


    // 设置窗口显示
    public void windowDeploy() {

        Window win = doalog.getWindow(); // 得到对话框
//        win.setWindowAnimations(R.style.dialog_changecity); //
//        win.setWindowAnimations(R.style.popupDialog); //
        win.getDecorView().setPadding(0, 0, 0, 0); // 宽度占满，因为style里面本身带有padding
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.FILL_PARENT;
        doalog.getWindow().setAttributes(lp);

    }
    private void initDataDialogs() {

        ProgressDlgUtil.showProgressDlg("loading...",activity);

        HttpRequest.sendGet(Contonst.LOCAL_CHANGECITY, "", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                if (msg!=null) {
                    ProgressDlgUtil.stopProgressDlg();

                    mhandle.post(new Runnable() {
                        @Override
                        public void run() {
                            City1 city=new Gson().fromJson(msg,City1.class);
                            if (city!=null&&city.result==1) {

                                if(city.body.data!=null&&city.body.data.size()>0){
                                    cityListAdapter2 = new CityListAdapter2(activity,city.body.data);
                                    localLv.setAdapter(cityListAdapter2);
                                }

                                if(TextUtils.isEmpty(aCache.getAsString("location_tr"))){

                                    aCache.put("location_tr",msg);
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




    public class CityListAdapter2 extends BaseAdapter {
        private Activity mcontext;
        private LayoutInflater inflater;
        private List<City1.BodyBean.DataBean> cityList;

        public CityListAdapter2(Activity mcontext, List<City1.BodyBean.DataBean> cityList) {
            this.mcontext = mcontext;
            inflater = LayoutInflater.from(mcontext);
            this.cityList = cityList;
        }

        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public City1.BodyBean.DataBean getItem(int position) {
            return cityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        CityGridViewAdapter mGridViewAdapter;
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder=null;
            if(convertView==null){
                convertView=inflater.inflate(R.layout.local_activity_country_city3_item,parent,false);
                holder=new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            if(!TextUtils.isEmpty(cityList.get(position).areaCnName)){
                holder.tv_acer.setText(cityList.get(position).areaCnName);
            }

            if(cityList.get(position).citys!=null&&cityList.get(position).citys.size()>0){

                mGridViewAdapter=new CityGridViewAdapter(mcontext,cityList.get(position).citys);
                holder.cityGridView.setAdapter(mGridViewAdapter);

            }

            holder.cityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    City1.BodyBean.DataBean.CitysBean citysBean=(City1.BodyBean.DataBean.CitysBean)adapterView.getItemAtPosition(i);

//                    Intent intent = new Intent();
////                intent.putExtra("countryName", holder.t_country_name.getText().toString());
////                intent.putExtra("countryId",foreignCity.getCountryEnName());
//                    intent.putExtra("cityName", citysBean.cateName+"");
////                intent.putExtra("cityName", cities.get(position).getCate_name());
//                    intent.putExtra("cityId", citysBean.cityId+"");
//                    intent.putExtra("cityphoto", citysBean.ossUrl+"");
//                    mcontext.setResult(1, intent);
//                    mcontext.finish();

                    cityId=citysBean.cityId+"";
                    tv_city.setText(citysBean.cateName+"");
                    tv_locaname.setText(citysBean.cateName+"");

                    doalog.dismiss();
                    getTravel();

                    MyApplication.saveTravelCityId(cityId);
                    MyApplication.saveCityName(citysBean.cateName+"");



                }
            });

            return convertView;
        }
    }
    public class  ViewHolder{

        private TextView tv_acer;
        private MyGridView cityGridView;
        public ViewHolder(View view) {
            tv_acer=(TextView) view.findViewById(R.id.area_tv);
            cityGridView= (MyGridView) view.findViewById(R.id.local_gv);
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mMapView.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();

        if(isChangeCity){
            if(!TextUtils.isEmpty(cityId)){
                Log.e("zdscityId",cityId+"");
                getTravel();
            }

            isChangeCity=false;
        }
    }

    private   List<MyTickets> ticketses=new ArrayList<>();
    public void getTravel() {


        ProgressDlgUtil.showProgressDlg("loading...",activity);

        TravelBiz.getInstance(activity).getTravel(cityId,new Response.Listener<TravelResponse>(){
            @Override
            public void onResponse(final TravelResponse response) {
                if(response!=null){

                    if(swipeRefreshLayout!=null&&swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }


                    if(response.result==1){


                        if(!TextUtils.isEmpty(response.body.travelNum)){
                            tv_peoples.setText(response.body.travelNum+"");
                        }else {
                            tv_peoples.setText("26,790");
                        }

                        if(!TextUtils.isEmpty(response.body.highEstTemperature)){

                            tv_loca_wendu.setText(response.body.highEstTemperature);
                            MyApplication.saveWenDu(response.body.highEstTemperature+"");
                        }else {
                            tv_loca_wendu.setVisibility(View.GONE);
                        }

                        if(response.body.touristAttractions!=null&&response.body.shops!=null&&response.body.restaurants!=null){

                            if(response.body.touristAttractions.size()==0&&response.body.shops.size()==0&&response.body.restaurants.size()==0){
                                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                                builder.setTitle("温馨提示");
                                builder.setIcon(R.drawable.img_huaqiao_default);
                                builder.setMessage("抱歉！该城市暂无数据！换个城市看看！");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent city=new Intent(activity,CountryCityActivityChange.class);
                                        startActivityForResult(city,0);
                                        isChangeCity=true;
                                    }
                                }).create().show();
                            }
                        }

                        if(response.body.banners!=null&&response.body.banners.size()>0){
                            initBanners(response.body.banners);
                        }
                        if(response.body.audios!=null){
                            initYuYins(response.body.audios);
                        }

                        if(response.body.touristAttractions!=null&&response.body.touristAttractions.size()>0){

                            getTravelTourist(response.body.touristAttractions);
                        }

                        if(response.body.touristAttractionCount>0){

                            tv_jingdiancount.setText("必玩("+response.body.touristAttractionCount+")");
                            biwan_count.setVisibility(View.VISIBLE);
                            biwan_count.setText(response.body.touristAttractionCount+"");
                            MyApplication.saveBiWanNum(response.body.touristAttractionCount+"");
                        }
                        if(response.body.shops!=null&&response.body.shops.size()>0){
                            getShop(response.body.shops);
                        }

                        if(response.body.shopCount>0){

                            tv_shop.setText("必买("+response.body.shopCount+")");
                            bimai_count.setVisibility(View.VISIBLE);
                            bimai_count.setText(response.body.shopCount+"");
                            MyApplication.saveBiMaiNum(response.body.shopCount+"");
                        }
                        if(response.body.travelNoteCount>0){
                            youji_count.setVisibility(View.VISIBLE);
                            youji_count.setText(response.body.travelNoteCount+"");
                        }
                        if(response.body.audioCount>0){
                            yuyin_count.setVisibility(View.VISIBLE);
                            yuyin_count.setText(response.body.audioCount+"");
                        }else{
                            yuyin_count.setVisibility(View.INVISIBLE);
                        }

                        if(response.body.restaurants!=null&&response.body.restaurants.size()>0){

                            getRestaurant(response.body.restaurants);
                        }

                        if(response.body.restaurantCount>0){

                            tv_restauants.setText("必吃("+response.body.restaurantCount+")");
                            bichi_count.setVisibility(View.VISIBLE);
                            bichi_count.setText(response.body.restaurantCount+"");
                            MyApplication.saveBiChiNum(response.body.restaurantCount+"");
                        }

                        MyApplication.saveTravelCityId(cityId);
                        ProgressDlgUtil.stopProgressDlg();
                        aCache.remove(ACache.MAIN);
                        aCache.put(ACache.MAIN,new Gson().toJson(response));

                        if(titlrs!=null&&titlrs.size()>0){
                            for (int i=0;i<titlrs.size();i++){
                                mStringBuffer.append(titlrs.get(i)+",");
                            }

                            Util.preference.edit().putString("tittle", mStringBuffer.toString()).commit();

                        }

                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity,error.toString(),Toast.LENGTH_LONG).show();
                ProgressDlgUtil.stopProgressDlg();
            }
        }, null);
    }

    private StringBuffer mStringBuffer=new StringBuffer();


    private StringBuilder mBuilder=new StringBuilder();
    private StringBuilder mBuilder_wan=new StringBuilder();
    private StringBuilder mBuilder_mai=new StringBuilder();

    private void initmaiMap(List<ShopBean> ShopList){
        for (int i=0;i<ShopList.size();i++){
            titlrs.add(ShopList.get(i).name);
            String lng=ShopList.get(i).lng;
            String lat=ShopList.get(i).lat;

            if(!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(lat)){

                mBuilder_mai.append(lng+","+lat+"|");
            }
        }
    }


    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private void initLocation(){
        mLocationClient = new LocationClient(getActivity());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();

    }


    private String current_lng;
    private String current_lat;
    double latitude  ;//获取经度
    double longitude  ;//获取纬度

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

                 latitude = location.getLatitude();//获取经度
                 longitude = location.getLongitude();//获取纬度
                current_lat = String.valueOf(latitude);
                current_lng = String.valueOf(longitude);

                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                 latitude = location.getLatitude();//获取经度
                 longitude = location.getLongitude();//获取纬度
                current_lat = String.valueOf(latitude);
                current_lng = String.valueOf(longitude);

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                 latitude = location.getLatitude();//获取经度
                 longitude = location.getLongitude();//获取纬度
                current_lat = String.valueOf(latitude);
                current_lng = String.valueOf(longitude);

                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }

//
//            /////////////////////////////////////
//            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                double latitude = location.getLatitude();//获取经度
//                double longitude = location.getLongitude();//获取纬度
//                current_lat=String .valueOf(latitude);
//                current_lng=String .valueOf(longitude);
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                double latitude = location.getLatitude();//获取经度
//                double longitude = location.getLongitude();//获取纬度
//                current_lat=String .valueOf(latitude);
//                current_lng=String .valueOf(longitude);
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                double latitude = location.getLatitude();//获取经度
//                double longitude = location.getLongitude();//获取纬度
//                current_lat=String .valueOf(latitude);
//                current_lng=String .valueOf(longitude);
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//            sb.append("\nlocationdescribe : ");
//            sb.append(location.getLocationDescribe());// 位置语义化信息
//            List<Poi> list = location.getPoiList();// POI数据
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }

//            Toast.makeText(activity,sb.toString(),Toast.LENGTH_LONG).show();
                Log.i("BaiduLocationApiDem", sb.toString());
                Log.i("uuuu_lat", latitude + "");
                Log.i("uuuu_lng", longitude + "");

                MyApplication.my_current_lat=current_lat;
                MyApplication.my_current_lng=current_lng;

                initLocationDialog(latitude,longitude);

                mLocationClient.stop();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null&&resultCode==1&&requestCode==0){
//            String countryName=data.getStringExtra("countryName");
            String cityName=data.getStringExtra("cityName");
            cityId=data.getStringExtra("cityId");
           String cityphoto=data.getStringExtra("cityphoto");
//            if(!TextUtils.isEmpty(cityphoto)){
//                MyApplication.imageLoader.displayImage(cityphoto, mapView, MyApplication.options);
//            }


            tv_city.setText(cityName+"");
            tv_locaname.setText(cityName+"");
//            initFuJinMap(cityId);
            initFuJinMapLatLng(cityId);
            MyApplication.saveTravelCityId(cityId);
            MyApplication.saveCityName(cityName);

        }

    }

    private void  initFuJinMapLatLng(String cityId){
//
//        墨尔本 -37.971237,144.4926947
//        悉尼 -33.8474012,150.6510966
//        曼谷 13.7251089,100.3522356
//        维也纳 48.2208282,16.2396343
//        萨尔兹堡 47.8027309,13.039348
//        清迈 18.7719014,98.8862655
//        普吉 7.9668486,98.2195005
//        芭提雅 12.8868533,100.8525479

//        MyApplication.my_current_lng=current_lng;
//        MyApplication.my_current_lat=current_lat;

        if(cityId.equals("41")){
           current_lat="48.2208282";
            current_lng="16.2396343";
//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.img_city41, mapView, Options.getHDOptions());
        }else if(cityId.equals("54")){
           current_lat="18.7719014";
            current_lng="98.8862655";

//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.img_city54, mapView, Options.getHDOptions());
        }else if(cityId.equals("55")){
            current_lat="13.7251089";
            current_lng="100.3522356";
//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.img_city55, mapView, Options.getHDOptions());
        }else if(cityId.equals("68")){
           current_lat="-37.971237";
            current_lng="144.4926947";

//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.img_city68, mapView, Options.getHDOptions());
        }else if(cityId.equals("6575")){
           current_lat="47.8027309";
            current_lng="13.039348";

//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.img_city6575, mapView, Options.getHDOptions());
        }else if(cityId.equals("8804")){
           current_lat="12.8868533";
            current_lng="100.8525479";

//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.img_city8804, mapView, Options.getHDOptions());
        }else if(cityId.equals("8845")){
            current_lat="7.9668486";
            current_lng="98.2195005";

//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.img_city8845, mapView, Options.getHDOptions());
        }else if(cityId.equals("70")){  //悉尼
           current_lat="-33.8474012";
            current_lng="150.6510966";

//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.img_city8845, mapView, Options.getHDOptions());
        }
    }

    public String getCurrentW(String uRLss,long start){

//        InputStream is=null;
        try {
            URL url = new URL(uRLss);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");// 设置请求的方式
            urlConnection.setReadTimeout(5000);// 设置超时的时间
            urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
            urlConnection.setDoInput(true);
            urlConnection.connect();
//            // 设置请求的头
//            urlConnection
//                    .setRequestProperty("User-Agent",
//                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            // 获取响应的状态码 404 200 505 302

            Log.i("zds","start1="+start);

            if (urlConnection.getResponseCode() == 200) {
//                // 获取响应的输入流对象
//                is = urlConnection.getInputStream();
//                // 释放资源
//                is.close();
                Log.i("zds","start2="+start);
                Log.i("zds","end="+System.currentTimeMillis());

                long to=System.currentTimeMillis()-start;

                Log.i("zds","to="+to);

                return to+"";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //百度静态地图
//    http://api.map.baidu.com/staticimage?center=116.403874,39.914889&width=400&height=300&zoom=11&markers=116.288891,40.004261|116.487812,40.017524|116.525756,39.967111|116.536105,39.872374|116.442968,39.797022|116.270494,39.851993|116.275093,39.935251|116.383177,39.923743&markerStyles=l,A|m,B|l,C|l,D|m,E|,|l,G|m,H
    /**
     * 通过GET方式发送的请求
     * @param lng
     * @param lat
     * 坐标格式：lng<经度>，lat<纬度>，例如116.43213,38.76623。
     */
/*
    public void getMap(double lng, double lat,String str) throws IOException {

        Log.i("zds_map",lng+"===   "+lat+"=====   "+str);
        InputStream is=null;
        try {
            // 设置请求的地址 通过URLEncoder.encode(String sd, String enc)
            // 使用指定的编码机制将字符串转换为 application/x-www-form-urlencoded 格式
            // 根据地址创建URL对象(网络访问的url)                                                                            &markerStyles=l,A|m,B|l,C|l,D|m,E|,|l,G|m,H
            String spec="http://api.map.baidu.com/staticimage?center="+lng+","+lat+"&width=700&height=320&zoom=16&markers="+str+"&markerStyles=l,A|l";
            URL url = new URL(spec);
            // url.openConnection()打开网络链接
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");// 设置请求的方式
            urlConnection.setReadTimeout(5000);// 设置超时的时间
            urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
            urlConnection.setDoInput(true);
            urlConnection.connect();
//            // 设置请求的头
//            urlConnection
//                    .setRequestProperty("User-Agent",
//                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            // 获取响应的状态码 404 200 505 302
            if (urlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                is = urlConnection.getInputStream();

                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mMapView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        mapView.setImageBitmap(bitmap);
                    }
                });

                // 释放资源
                is.close();
//                // 创建字节输出流对象
//                ByteArrayOutputStream os = new ByteArrayOutputStream();
//                // 定义读取的长度
//                int len = 0;
//                // 定义缓冲区
//                byte buffer[] = new byte[1024];
//                // 按照缓冲区的大小，循环读取
//                while ((len = is.read(buffer)) != -1) {
//                    // 根据读取的长度写入到os对象中
//                    os.write(buffer, 0, len);
//                }
//                os.close();
                // 返回字符串
//                String result = new String(os.toByteArray());
//                System.out.println("***************" + result
//                        + "******************");

            } else {
                System.out.println("------------------链接失败-----------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

}
