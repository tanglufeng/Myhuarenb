//package com.abcs.hqbtravel.view.activity;
//
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.abcs.hqbtravel.RollPager.RollPagerView;
//import com.abcs.hqbtravel.RollPager.adapter.LoopPagerAdapter;
//import com.abcs.hqbtravel.RollPager.hintview.IconHintView;
//import com.abcs.hqbtravel.adapter.FuJinJingDianAdapter;
//import com.abcs.hqbtravel.adapter.ResturantAdapter;
//import com.abcs.hqbtravel.adapter.ShopAdapter;
//import com.abcs.hqbtravel.biz.TravelBiz;
//import com.abcs.hqbtravel.entity.TravelResponse;
//import com.abcs.hqbtravel.wedgt.ACache;
//import com.abcs.hqbtravel.wedgt.MyGridView;
//import com.abcs.hqbtravel.wedgt.MyListView;
//import com.abcs.huaqiaobang.MyApplication;
//import com.abcs.huaqiaobang.view.CircleImageView;
//import com.abcs.sociax.android.R;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.location.Poi;
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends Activity implements View.OnClickListener{
//
//    public LocationClient mLocationClient = null;
//    public BDLocationListener myListener = new MyLocationListener();
//
//    TestLoopAdapter loopAdapter;
//    RollPagerView rollPagerView;
//
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private LinearLayout line_jindian;
//    private LinearLayout line_server;
//    private LinearLayout line_shop;
//    private LinearLayout line_restauants;
//
//    private boolean isChangeCity=false;
//    ImageView img_fuqu1;
//    CircleImageView img_fuqu1_avatar;
//
//    private EditText search;
//    TextView tv_fuqu1_isfamel;
//    TextView tv_fuqu1_name;
//    TextView tv_fuqu1_age;
//    TextView tv_house;
//    TextView tv_fuqu1_title;
//    TextView tv_fuqu1_location;
//    TextView tv_fuqu1_type;
//    TextView tv_fuqu1_dianpin;
//
//    TextView tv_shop;
//    TextView tv_restauants;
//
//    TextView tv_moreshop;
//    TextView tv_morerestauants;
//    TextView tv_moretourist;
//    TextView tv_moreserver;
//
//    TextView tv_touristcount;
//    TextView tv_city;
//
//    TextView tv_jingdiancount;
//    TextView tv_name_jingdian1;
//
//    RelativeLayout re1_first;
//    RelativeLayout re2_first;
//    RelativeLayout re3_first;
//    RelativeLayout re4_first;
//
//    LinearLayout re_fuqu1;
//    MyListView re_shop;
//    MyListView re_restauants;
//
//    RelativeLayout re2_daohang;
//    RelativeLayout re3_youji;
//
//    private MyGridView grid_fujin_jingtian;
//    private FuJinJingDianAdapter fuJinJingDianAdapter;
//
//    private ACache aCache;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
//        setContentView(R.layout.activity_main);
//
//
//        initView();
//        initLisenter();
//        TravelResponse response=new Gson().fromJson(aCache.getAsString(ACache.MAIN),TravelResponse.class);
//        if(response!=null){
//            initBanners(response.body.banners);
//            getTravelTourist(response.body.touristAttractions);
//            getTravelServer(response.body.services);
//            getShop(response.body.shops);
//            getRestaurant(response.body.restaurants);
//
//        }else {
//            if(!TextUtils.isEmpty(cityId)){
//                dialog.setMessage("...加载中...");
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();
//
//                getTravel();
//            }
//        }
//    }
//
//    private void initLisenter(){
//        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
//        initLocation();
//        mLocationClient.registerLocationListener( myListener );    //注册监听函数
//
//        mLocationClient.start();
//
//        search.setOnClickListener(this);
//        tv_city.setOnClickListener(this);
//        re1_first.setOnClickListener(this);
//        re2_first.setOnClickListener(this);
//        re3_first.setOnClickListener(this);
//        re4_first.setOnClickListener(this);
//
//        re2_daohang.setOnClickListener(this);
//        re3_youji.setOnClickListener(this);
//
//        tv_moreshop.setOnClickListener(this);
//        tv_morerestauants.setOnClickListener(this);
//
//        tv_moretourist.setOnClickListener(this);
//        tv_moreserver.setOnClickListener(this);
//    }
//
//    private Handler mhandle=new Handler();
//    private boolean isReshLayout=true;
//    private String cityId="50";
//    private void initView(){
//
//        cityId= MyApplication.getCityId();
//        if(TextUtils.isEmpty(cityId)){
//            cityId="50";   //模拟数据  正式发布时替换
//        }
//        dialog=new ProgressDialog(this);
//        aCache=ACache.get(this);
//        search=(EditText)findViewById(R.id.search);
//        search.setFocusable(false);
//        rollPagerView= (RollPagerView)findViewById(R.id.roll_pagerview);
//        grid_fujin_jingtian= (MyGridView) findViewById(R.id.grid_fujin_jingtian);
//
//        re1_first= (RelativeLayout)findViewById(R.id.rel_first);
//        re2_first= (RelativeLayout)findViewById(R.id.re2_first1);
//        re3_first= (RelativeLayout)findViewById(R.id.re3_first1);
//        re4_first= (RelativeLayout)findViewById(R.id.re4_first1);
//
//        re_fuqu1= (LinearLayout) findViewById(R.id.re_fuqu1);
//        re_shop= (MyListView) findViewById(R.id.re_shop);
//        re_restauants= (MyListView) findViewById(R.id.re_restauants);
//
//        line_jindian= (LinearLayout) findViewById(R.id.line_jindian);
//        line_server= (LinearLayout) findViewById(R.id.line_server);
//        line_shop= (LinearLayout) findViewById(R.id.line_shop);
//        line_restauants= (LinearLayout) findViewById(R.id.line_restauants);
//
//        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
//        //设置刷新时动画的颜色，可以设置4个
//        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getTravel();
//                mhandle.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                },2000);
//            }
//        });
//
//        re2_daohang= (RelativeLayout)findViewById(R.id.re2_daohang);
//        re3_youji= (RelativeLayout)findViewById(R.id.re3_youji);
//
//        tv_city= (TextView)findViewById(R.id.tv_city);
//        if(!TextUtils.isEmpty(MyApplication.getCityName())){
//            tv_city.setText(MyApplication.getCityName());
//        }else {
//            tv_city.setText("中国香港");
//        }
//
//        tv_moretourist= (TextView)findViewById(R.id.tv_moretourist);
//        tv_moreserver= (TextView)findViewById(R.id.tv_moreserver);
//
//        tv_moreshop= (TextView)findViewById(R.id.tv_moreshop);
//        tv_morerestauants= (TextView)findViewById(R.id.tv_morerestauants);
//
//        tv_shop= (TextView)findViewById(R.id.tv_shop);
//        tv_restauants= (TextView)findViewById(R.id.tv_restauants);
//
//        tv_touristcount= (TextView)findViewById(R.id.tv_touristcount);
//        tv_jingdiancount= (TextView)findViewById(R.id.tv_jingdiancount);
//
//        tv_name_jingdian1= (TextView)findViewById(R.id.tv_name_jingdian1);
//    }
//
//
//    private List<TravelResponse.BodyBean.TouristAttractionsBean> newTourists=new ArrayList<>();
//    private void getTravelTourist(List<TravelResponse.BodyBean.TouristAttractionsBean> touristAttractions){
//
//        if(touristAttractions.size()>0){
//            line_jindian.setVisibility(View.VISIBLE);
//            if(touristAttractions.size()>0&&touristAttractions.size()<5){
//                newTourists.clear();
//                newTourists.addAll(touristAttractions);
//                fuJinJingDianAdapter=new FuJinJingDianAdapter(this,newTourists);
//            }else {
//                newTourists.clear();
//                for(int i=0;i<4;i++){
//                    newTourists.add(touristAttractions.get(i));
//                }
//                fuJinJingDianAdapter=new FuJinJingDianAdapter(this,newTourists);
//            }
//            grid_fujin_jingtian.setAdapter(fuJinJingDianAdapter);
//            grid_fujin_jingtian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    TravelResponse.BodyBean.TouristAttractionsBean item=(TravelResponse.BodyBean.TouristAttractionsBean)fuJinJingDianAdapter.getItem(position);
//                    Intent it1=new Intent(MainActivity.this,BiWanDetialsActivity.class);
//                    it1.putExtra("bwanId",item.id);
//                    it1.putExtra("photo",item.photo);
//                    startActivity(it1);
//                }
//            });
//        }else {
//            line_jindian.setVisibility(View.GONE);
//        }
//    }
//
//    List<TravelResponse.BodyEntity.ServicesEntity> newservices=new ArrayList<>();
//    private void getTravelServer(final List<TravelResponse.BodyEntity.ServicesEntity> services){
//
//        if(services.size()>0){
//            line_server.setVisibility(View.VISIBLE);
//            if(services.size()>0&&services.size()<4){
//                re_fuqu1.removeAllViews();
//                newservices.clear();
//                newservices.addAll(services);
//                for(int i=0;i<newservices.size();i++){
//                    final TravelResponse.BodyEntity.ServicesEntity servicesEntity=newservices.get(i);
//                    View itemView=getLayoutInflater().inflate(R.layout.item_travle_zhu_li,null);
//                    ViewGroup parent = (ViewGroup) itemView.getParent();
//                    if (parent != null) {
//                        parent.removeAllViews();
//                    }
//
//                    img_fuqu1=(ImageView) itemView.findViewById(R.id.img_fuqu_logo);
//                    img_fuqu1_avatar=(CircleImageView) itemView.findViewById(R.id.img_fuqu1_avatar);
//                    tv_house=(TextView) itemView.findViewById(R.id.tv_house);
//                    tv_fuqu1_title=(TextView) itemView.findViewById(R.id.tv_fuqu1_title);
//                    tv_fuqu1_location=(TextView) itemView.findViewById(R.id.tv_fuqu1_location);
//                    tv_fuqu1_type=(TextView) itemView.findViewById(R.id.tv_fuqu1_type);
//                    tv_fuqu1_dianpin=(TextView) itemView.findViewById(R.id.tv_fuqu1_dianpin);
//                    tv_fuqu1_name=(TextView) itemView.findViewById(R.id.tv_fuqu1_name);
//                    tv_fuqu1_isfamel=(TextView) itemView.findViewById(R.id.tv_fuqu1_isfamel);
//                    tv_fuqu1_age=(TextView) itemView.findViewById(R.id.tv_fuqu1_age);
////                        tv_fuqu1_isvip=(TextView) itemView.findViewById(R.id.tv_fuqu1_isvip);
//                    MyApplication.imageLoader.displayImage(newservices.get(i).logo, img_fuqu1, MyApplication.getListOptions());
//                    MyApplication.imageLoader.displayImage(newservices.get(i).assistants.avator, img_fuqu1_avatar, MyApplication.getAvatorOptions());
//                    tv_fuqu1_type.setText(newservices.get(i).type);
//                    tv_fuqu1_title.setText(newservices.get(i).name);
//                    tv_fuqu1_name.setText(newservices.get(i).assistants.name);
//                    tv_fuqu1_age.setText(newservices.get(i).assistants.age+"");
//                    if(newservices.get(i).assistants.sex==0){    // 0  男  1  女
//                        tv_fuqu1_isfamel.setBackgroundResource(R.drawable.img_travel_sexm);
//                    }else{
//                        tv_fuqu1_isfamel.setBackgroundResource(R.drawable.img_travel_sexf);
//                    }
//                    tv_fuqu1_dianpin.setText(newservices.get(i).comments+"点评");
//                    tv_house.setText(newservices.get(i).price+"");
//
//                    itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent it=new Intent(MainActivity.this,ServerDatialsActivity.class);
//                            it.putExtra("serverId",servicesEntity.id);
//                            startActivity(it);
//                        }
//                    });
//                    re_fuqu1.addView(itemView);
//                }
//            }else {
//                newservices.clear();
//                re_fuqu1.removeAllViews();
//                for(int i=0;i<3;i++){
//                    newservices.add(services.get(i));
//                }
//                for(int i=0;i<newservices.size();i++){
//                    final TravelResponse.BodyEntity.ServicesEntity servicesEntity=newservices.get(i);
//
//                    View itemView=getLayoutInflater().inflate(R.layout.item_travle_zhu_li,null);
//                    ViewGroup parent = (ViewGroup) itemView.getParent();
//                    if (parent != null) {
//                        parent.removeAllViews();
//                    }
//
//                    img_fuqu1=(ImageView) itemView.findViewById(R.id.img_fuqu_logo);
//                    img_fuqu1_avatar=(CircleImageView) itemView.findViewById(R.id.img_fuqu1_avatar);
//                    tv_house=(TextView) itemView.findViewById(R.id.tv_house);
//                    tv_fuqu1_title=(TextView) itemView.findViewById(R.id.tv_fuqu1_title);
//                    tv_fuqu1_location=(TextView) itemView.findViewById(R.id.tv_fuqu1_location);
//                    tv_fuqu1_type=(TextView) itemView.findViewById(R.id.tv_fuqu1_type);
//                    tv_fuqu1_dianpin=(TextView) itemView.findViewById(R.id.tv_fuqu1_dianpin);
//                    tv_fuqu1_name=(TextView) itemView.findViewById(R.id.tv_fuqu1_name);
//                    tv_fuqu1_isfamel=(TextView) itemView.findViewById(R.id.tv_fuqu1_isfamel);
//                    tv_fuqu1_age=(TextView) itemView.findViewById(R.id.tv_fuqu1_age);
////                        tv_fuqu1_isvip=(TextView) itemView.findViewById(R.id.tv_fuqu1_isvip);
//                    MyApplication.imageLoader.displayImage(newservices.get(i).logo, img_fuqu1, MyApplication.getListOptions());
//                    MyApplication.imageLoader.displayImage(newservices.get(i).assistants.avator, img_fuqu1_avatar, MyApplication.getAvatorOptions());
//                    tv_fuqu1_type.setText(newservices.get(i).type);
//                    tv_fuqu1_title.setText(newservices.get(i).name);
//                    tv_fuqu1_name.setText(newservices.get(i).assistants.name);
//                    tv_fuqu1_age.setText(newservices.get(i).assistants.age+"");
//                    if(newservices.get(i).assistants.sex==0){    // 0  男  1  女
//                        tv_fuqu1_isfamel.setBackgroundResource(R.drawable.img_travel_sexm);
//                    }else{
//                        tv_fuqu1_isfamel.setBackgroundResource(R.drawable.img_travel_sexf);
//                    }
//                    tv_fuqu1_dianpin.setText(newservices.get(i).comments+"点评");
//                    tv_house.setText(newservices.get(i).price+"");
//
//                    itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent it=new Intent(MainActivity.this,ServerDatialsActivity.class);
//                            it.putExtra("serverId",servicesEntity.id);
//                            startActivity(it);
//                        }
//                    });
//                    re_fuqu1.addView(itemView);
//                }
//            }
//        }else {
//            line_server.setVisibility(View.GONE);
//        }
//    }
//
//    private ResturantAdapter resturantAdapter;
//    private String chiphoto;
//    List<TravelResponse.BodyEntity.RestaurantsEntity> newRestaList=new ArrayList<>();
//    private void getRestaurant(final List<TravelResponse.BodyEntity.RestaurantsEntity> RestaList){
//
//        if(RestaList.size()>0){
//            line_restauants.setVisibility(View.VISIBLE);
//            if(RestaList.size()>0&&RestaList.size()<4){
//                newRestaList.clear();
//                newRestaList.addAll(RestaList);
//                resturantAdapter=new ResturantAdapter(this,newRestaList);
//            }else {
//                newRestaList.clear();
//                for(int i=0;i<3;i++){
//                    newRestaList.add(RestaList.get(i));
//                }
//                resturantAdapter=new ResturantAdapter(this,newRestaList);
//            }
//            re_restauants.setAdapter(resturantAdapter);
//            re_restauants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    final TravelResponse.BodyEntity.RestaurantsEntity restaurantsEntity=resturantAdapter.getItem(position);
//                    chiphoto=restaurantsEntity.photo;
//                    Intent it=new Intent(MainActivity.this,BiChiDetialsActivity.class);
//                    it.putExtra("chiId", restaurantsEntity.id);
//                    it.putExtra("chiphoto", chiphoto);
//                    startActivity(it);
//                }
//            });
//        }else {
//            line_restauants.setVisibility(View.GONE);
//        }
//    }
//
//    private ShopAdapter shopAdapter;
//    private String photo;
//    List<TravelResponse.BodyEntity.ShopsEntity> newShopList=new ArrayList<>();
//    private void getShop(final List<TravelResponse.BodyEntity.ShopsEntity> ShopList){
//
//        if(ShopList.size()>0){
//            line_shop.setVisibility(View.VISIBLE);
//            if(ShopList.size()>0&&ShopList.size()<4){
//                newShopList.clear();
//                newShopList.addAll(ShopList);
//                 shopAdapter=new ShopAdapter(this,newShopList);
//
//        }else {
//                newShopList.clear();
//                for(int i=0;i<3;i++){
//                    newShopList.add(ShopList.get(i));
//                }
//                shopAdapter=new ShopAdapter(this,newShopList);
//            }
//            re_shop.setAdapter(shopAdapter);
//            re_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    final TravelResponse.BodyEntity.ShopsEntity shopsEntity=shopAdapter.getItem(position);
//                    photo=shopsEntity.photo;
//                    Intent it=new Intent(MainActivity.this,BiMaiDetialsActivity.class);
//                    it.putExtra("shopId", shopsEntity.id);
//                    it.putExtra("photo", photo);
//                    startActivity(it);
//                }
//            });
//        }else {
//            line_shop.setVisibility(View.GONE);
//        }
//    }
//
//    List<TravelResponse.BodyEntity.BannersEntity> newbanners=new ArrayList<>();
//    private void initBanners(List<TravelResponse.BodyEntity.BannersEntity> banners){
//        newbanners.clear();
//        if(banners.size()>0){
//            newbanners.addAll(banners);
//        }else {
//            newbanners.add(new TravelResponse.BodyEntity.BannersEntity(""));
//        }
//        rollPagerView.setHintPadding(0, 0, 0, 8);
//        rollPagerView.setPlayDelay(2000);
//        rollPagerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
//        rollPagerView.setAdapter(loopAdapter = new TestLoopAdapter(rollPagerView, newbanners));
//        rollPagerView.setHintView(new IconHintView(this, R.drawable.vp_now_yellow, R.drawable.vp_other, 20));
//        loopAdapter.notifyDataSetChanged();
//    }
//
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.tv_city:  /*****切换城市******/
//                Intent city=new Intent(this,CountryCityActivity.class);
//                startActivityForResult(city,0);
//                isChangeCity=true;
//                break;
//            case R.id.tv_moretourist:  /*****更多景点******/
//                Intent iw=new Intent(this,BiWanActivity2.class);
//                iw.putExtra("cityId",cityId);
//                startActivity(iw);
//                break;
//            case R.id.tv_moreshop:  /*****更多必买******/
//                Intent bimai=new Intent(this,BiMaiActivity.class);
//                bimai.putExtra("cityId",cityId);
//                bimai.putExtra("current_lat",current_lat);
//                bimai.putExtra("current_lng",current_lng);
//                startActivity(bimai);
//                break;
//            case R.id.tv_morerestauants:  /*****更多必吃******/
//                Intent bichi=new Intent(this,BiChiActivity.class);
//                bichi.putExtra("cityId",cityId);
//                bichi.putExtra("current_lat",current_lat);
//                bichi.putExtra("current_lng",current_lng);
//                startActivity(bichi);
//                break;
//            case R.id.tv_moreserver:  /*****更多服务******/
//                Intent ie=new Intent(this,ZhuLiActivity.class);
//                ie.putExtra("cityId",cityId);
//                startActivity(ie);
//                break;
//            case R.id.rel_first:  /*****旅游助理******/
//                Intent i=new Intent(this,ZhuLiActivity.class);
//                i.putExtra("cityId",cityId);
//                startActivity(i);
//            break;
//            case R.id.re2_first1:/*****必玩******/
//                Intent ir=new Intent(this,BiWanActivity2.class);
//                ir.putExtra("cityId",cityId);
//                startActivity(ir);
//            break;
//            case R.id.re3_first1:/*****必吃******/
//                Intent irw=new Intent(this,BiChiActivity.class);
//                irw.putExtra("cityId",cityId);
//                irw.putExtra("current_lat",current_lat);
//                irw.putExtra("current_lng",current_lng);
//                startActivity(irw);
//            break;
//            case R.id.re4_first1: /*****必买******/
//                Intent irq=new Intent(this,BiMaiActivity.class);
//                irq.putExtra("cityId",cityId);
//                irq.putExtra("current_lat",current_lat);
//                irq.putExtra("current_lng",current_lng);
//                startActivity(irq);
//            break;
//            case R.id.re2_daohang:  //语音导航
//                Intent itw4=new Intent(this,DaoHangActivity2.class);
//                itw4.putExtra("cityId",cityId);
//                startActivity(itw4);
////                overridePendingTransition(R.anim.enter_anim,R.anim.exit_anim);
//            break;
//            case R.id.re3_youji:  //精彩游记
//                Intent itq4=new Intent(this,YouJiActivity.class);
//                itq4.putExtra("cityId",cityId);
//                startActivity(itq4);
//            break;
//            case R.id.search:  //搜索景点 美食
//
//                Intent d=new Intent(this,SeachYouJiByTagActivity.class);
//                d.putExtra("cityId",cityId);
//                d.putExtra("type","ismeishi");
//                startActivity(d);
//            break;
//        }
//
//    }
//
//    private class TestLoopAdapter extends LoopPagerAdapter {
//
//        private List<TravelResponse.BodyEntity.BannersEntity> banners;
//
//        public TestLoopAdapter(RollPagerView viewPager,List<TravelResponse.BodyEntity.BannersEntity> banners) {
//            super(viewPager);
//            this.banners = banners;
//
//        }
//
//        @Override
//        public View getView(ViewGroup container, final int position) {
//            ImageView view = new ImageView(container.getContext());
//            MyApplication.imageLoader.displayImage(banners.get(position).logo, view, MyApplication.getListOptions());
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
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if(isChangeCity){
//            if(!TextUtils.isEmpty(cityId)){
//                dialog.setMessage("...加载中...");
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();
//
//                getTravel();
//            }
//
//            isChangeCity=false;
//        }
//    }
//
//    private ProgressDialog dialog;
//    public void getTravel() {
//
//        TravelBiz.getInstance(this).getTravel(cityId,new Response.Listener<TravelResponse>(){
//            @Override
//            public void onResponse(final TravelResponse response) {
//                if(response!=null){
//
//                    if(swipeRefreshLayout!=null&&swipeRefreshLayout.isRefreshing()){
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                    if(response.body.touristAttractions.size()==0&&response.body.services.size()==0&&response.body.shops.size()==0&&response.body.restaurants.size()==0){
//                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
//                        builder.setTitle("温馨提示");
//                        builder.setIcon(R.drawable.img_huaqiao_default);
//                        builder.setMessage("抱歉！该城市暂无数据！换个城市看看！");
//                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent city=new Intent(MainActivity.this,CountryCityActivity.class);
//                                startActivityForResult(city,0);
//                                isChangeCity=true;
//                            }
//                        }).create().show();
//                    }
//
//                    initBanners(response.body.banners);
//
//                    getTravelTourist(response.body.touristAttractions);
//                    Log.i("附近景点大小：",newTourists.size()+"");
//                    tv_jingdiancount.setText("附近热门景点("+response.body.touristAttractionCount+")");
//
//                    getTravelServer(response.body.services);
//                    Log.i("附近服务大小：",newservices.size()+"");
//                    tv_touristcount.setText("附近服务("+response.body.serviceCount+")");
//
//                    getShop(response.body.shops);
//                    Log.i("必买大小：",response.body.shopCount+"");
//                    tv_shop.setText("必买("+response.body.shopCount+")");
//
//                    getRestaurant(response.body.restaurants);
//                    Log.i("必吃大小：",response.body.shopCount+"");
//                    tv_restauants.setText("必吃("+response.body.restaurantCount+")");
//                    dialog.dismiss();
//                        aCache.remove(ACache.MAIN);
//                        aCache.put(ACache.MAIN,new Gson().toJson(response));
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_LONG).show();
//                dialog.dismiss();
//            }
//        }, null);
//    }
//    private void initLocation(){
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
//        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        mLocationClient.setLocOption(option);
//    }
//
//    private String current_lng;
//    private String current_lat;
//
//    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            current_lat= String.valueOf(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            current_lng= String.valueOf(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 单位：公里每小时
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 单位：米
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());// 单位度
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
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
//
////            Toast.makeText(MainActivity.this,sb.toString(),Toast.LENGTH_LONG).show();
//            Log.i("BaiduLocationApiDem", sb.toString());
//            Log.i("uuuu_lat", current_lat);
//            Log.i("uuuu_lng", current_lng);
//        }
//    }
//
////    private String cityId=7823+"";
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(data!=null&&resultCode==1&&requestCode==0){
//            String countryName=data.getStringExtra("countryName");
//            String cityName=data.getStringExtra("cityName");
//             cityId=data.getStringExtra("cityId");
//
//            tv_city.setText(countryName+cityName+"");
//            Log.i("countryName:  ",countryName);
//            Log.i("cityName:  ",cityName);
//            MyApplication.saveLocation(cityName);
//            MyApplication.saveCityId(cityId);
//            MyApplication.saveCityName(cityName);
//            Log.i("cityId:  ",cityId);
//
//        }
//    }
//}
