package com.abcs.hqbtravel.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.hqbtravel.biz.getFuJinSanBiBiz;
import com.abcs.hqbtravel.entity.FujinSanBi;
import com.abcs.hqbtravel.entity.RestauransBean;
import com.abcs.hqbtravel.entity.ShopBean;
import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMapActivity extends BaseActivity implements  View.OnClickListener {

    private ImageView img_back;
    private String lng;
    private String lat;
    private String cityId;

    private EditText ed_sercher;
    private MapView mapView;
    //地图首次坐标定位
    private boolean isFirst = true;
    private double latitude, longitude; //定位详细经纬度
    private String address = "";    //定位详细地址
    private boolean isLocate = false;
    private BaiduMap mMap;
    private LinearLayout liner_neard;
    private RelativeLayout relative_chi,relative_wan,relative_mai,relative_all;

    private LinearLayout tv_chi,tv_wan,tv_mai;
    private TextView tv_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main_map);

        cityId=getIntent().getStringExtra("cityId");
        lng=getIntent().getStringExtra("current_lng");
        lat=getIntent().getStringExtra("current_lat");

        tv_all=(TextView)findViewById(R.id.tv_all);
        tv_chi=(LinearLayout)findViewById(R.id.tt1);
        tv_wan=(LinearLayout)findViewById(R.id.tt2);
        tv_mai=(LinearLayout)findViewById(R.id.tt3);

        liner_neard=(LinearLayout)findViewById(R.id.liner_neard);

        relative_chi=(RelativeLayout)findViewById(R.id.relative_chi);
        relative_wan=(RelativeLayout)findViewById(R.id.relative_wan);
        relative_mai=(RelativeLayout)findViewById(R.id.relative_mai);
        relative_all=(RelativeLayout)findViewById(R.id.relative_all);

        relative_all.setOnClickListener(this);
        relative_chi.setOnClickListener(this);
        relative_wan.setOnClickListener(this);
        relative_mai.setOnClickListener(this);

        ed_sercher = (EditText) findViewById(R.id.ed_sercher);
        mapView = (MapView) findViewById(R.id.bmapView);
        mMap=mapView.getMap();
        img_back=(ImageView)findViewById(R.id.back);

        img_back.setOnClickListener(this);

        if(!TextUtils.isEmpty(cityId)) {
//        if(!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(lat)&&!TextUtils.isEmpty(cityId)) {
//            latitude = Double.parseDouble(lat);
//            longitude = Double.parseDouble(lng);
            getDatas();
//            initMap(longitude,latitude);
        }else {
            lng= MyApplication.my_current_lng;
            lat= MyApplication.my_current_lat;
        }

                /*输入框监听事件*/
        ed_sercher.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				/*判断是否是“GO”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
					/*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    loadingDatas();
                    return true;
                }
                return false;
            }
        });

    }

    private void loadingDatas(){
//        pageNo=1;
//        ser_context=ed_sercher.getText().toString().trim();
        showToast("该功能暂未开启！敬请期待！");
    }

    private List<RestauransBean> restauransBeen=new ArrayList<>();
    private List<TouristAttractionsBean> touristAttractions=new ArrayList<>();
    private List<ShopBean> shopBeen=new ArrayList<>();

    private List<Marker> markers=new ArrayList<>();

    private Map<String,List<RestauransBean>> maps_chi=new HashMap<>();
    private Map<String,List<TouristAttractionsBean>> maps_wan=new HashMap<>();
    private Map<String,List<ShopBean>> maps_mai=new HashMap<>();

    private int pageNo=1;
    private void getDatas() {

        ProgressDlgUtil.showProgressDlg("loading...",this);

        getFuJinSanBiBiz.getInstance(this).getFuJinSanBiList(cityId,pageNo,lng,lat,new Response.Listener<FujinSanBi>(){
            @Override
            public void onResponse(FujinSanBi response) {
                if(response!=null){
                    ProgressDlgUtil.stopProgressDlg();
                    maps_chi.clear();
                    maps_wan.clear();
                    maps_mai.clear();

                    if(response.result==1){
                        if(response.body.restaurants!=null&&response.body.restaurants.size()>0){
                            getBiChiData(response.body.restaurants);
                            maps_chi.put("chi",response.body.restaurants);

                            for (int i=0;i<response.body.restaurants.size();i++){
                                String lng=response.body.restaurants.get(i).lng;
                                String lat=response.body.restaurants.get(i).lat;

                                if(!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(lat)){
                                    LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                                    MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(point, 14);
                                    mMap.setMapStatus(mapStatus);
                                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                                            .fromResource(R.drawable.bichis);
                                    //            构建MarkerOption，用于在地图上添加Marker
                                    MarkerOptions option = new MarkerOptions()
                                            .position(point)
                                            .icon(bitmap).zIndex(0).period(14);;
                                    option.animateType(MarkerOptions.MarkerAnimateType.grow);
                                    //            在地图上添加Marker，并显示
                                    Marker marker=(Marker) mMap.addOverlay(option);
                                    markers.add(marker);
                                }
                            }
                        }

                        if(response.body.touristAttractions!=null&&response.body.touristAttractions.size()>0){
                            getBiWanData(response.body.touristAttractions);

                            maps_wan.put("wan",response.body.touristAttractions);
                            for (int i=0;i<response.body.touristAttractions.size();i++){
                                String lng=response.body.touristAttractions.get(i).lng;
                                String lat=response.body.touristAttractions.get(i).lat;

                                if(!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(lat)){
                                    LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                                    MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(point, 14);
                                    mMap.setMapStatus(mapStatus);
                                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                                            .fromResource(R.drawable.biwans);
                                    //            构建MarkerOption，用于在地图上添加Marker
//                                    OverlayOptions option = new MarkerOptions()
//                                            .position(point)
//                                            .icon(bitmap);
                                    MarkerOptions option = new MarkerOptions()
                                            .position(point)
                                            .icon(bitmap).zIndex(0).period(14);;
                                    option.animateType(MarkerOptions.MarkerAnimateType.grow);
                                    //            在地图上添加Marker，并显示
                                    Marker marker=(Marker) mMap.addOverlay(option);
                                    markers.add(marker);
                                }
                            }
                        }

                        if(response.body.shops!=null&&response.body.shops.size()>0){
                            getBiMaiData(response.body.shops);
                            maps_mai.put("mai",response.body.shops);

                            for (int i=0;i<response.body.shops.size();i++){
                                String lng=response.body.shops.get(i).lng;
                                String lat=response.body.shops.get(i).lat;

                                if(!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(lat)){
                                    LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                                    MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(point, 14);
                                    mMap.setMapStatus(mapStatus);
                                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                                            .fromResource(R.drawable.bimais);
                                    //            构建MarkerOption，用于在地图上添加Marker
                                    MarkerOptions option = new MarkerOptions()
                                            .position(point)
                                            .icon(bitmap).zIndex(0).period(14);;
                                    option.animateType(MarkerOptions.MarkerAnimateType.grow);
                                    //            在地图上添加Marker，并显示
                                    Marker marker=(Marker) mMap.addOverlay(option);
                                    markers.add(marker);
                                }
                            }
                        }


                        if(!TextUtils.isEmpty(MyApplication.my_current_lat)&&!TextUtils.isEmpty(MyApplication.my_current_lng)){
                            LatLng point = new LatLng(Double.valueOf(MyApplication.my_current_lat), Double.valueOf(MyApplication.my_current_lng));
                            if(point!=null){
                                MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(point, 14);
                                mMap.setMapStatus(mapStatus);
                                BitmapDescriptor bitmap = BitmapDescriptorFactory
                                        .fromResource(R.drawable.my_curre);
//            构建MarkerOption，用于在地图上添加Marker
                                MarkerOptions option = new MarkerOptions()
                                        .position(point)
                                        .icon(bitmap).zIndex(0).period(14);;
                                option.animateType(MarkerOptions.MarkerAnimateType.grow);
//            在地图上添加Marker，并显示
                                mMap.addOverlay(option);
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(FuJinSanBiActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                ProgressDlgUtil.stopProgressDlg();
            }
        }, null);
    }


    List<TouristAttractionsBean> biwan=new ArrayList<>();
    public void getBiWanData(List<TouristAttractionsBean> oldbiwan){

        biwan.clear();
        liner_neard.removeAllViews();
        if(oldbiwan!=null&&oldbiwan.size() > 0) {

            biwan.addAll(oldbiwan);
            for(int i=0;i<biwan.size();i++){
//                if(i<2){
                    final TouristAttractionsBean data=biwan.get(i);

                    View itemView=getLayoutInflater().inflate(R.layout.item_travle_bichi,null);

                    ViewGroup parent = (ViewGroup) itemView.getParent();
                    if (parent != null) {
                        parent.removeAllViews();
                    }
                    itemView.setLayoutParams(new ViewGroup.LayoutParams(MyApplication.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));

                List<ImageView> imgs=new ArrayList<>();

                RelativeLayout re_instron=(RelativeLayout) itemView.findViewById(R.id.re_instron);
                ImageView img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
                TextView tv_location=(TextView) itemView.findViewById(R.id.tv_location);
//                TextView bg_line=(TextView) itemView.findViewById(R.id.bg_line);
                TextView tv_dis=(TextView) itemView.findViewById(R.id.tv_dis);
                TextView tv_pin_number=(TextView) itemView.findViewById(R.id.tv_pin_number);
                TextView tv_type_names=(TextView) itemView.findViewById(R.id.tv_type_names);
                TextView tv_type_distances=(TextView) itemView.findViewById(R.id.tv_type_distances);
                TextView tv_pinfen=(TextView) itemView.findViewById(R.id.tv_pinfen);
                TextView tv_instrution=(TextView) itemView.findViewById(R.id.tv_instrution);
                TextView tv_price=(TextView) itemView.findViewById(R.id.tv_price);
                TextView tv_en_name=(TextView) itemView.findViewById(R.id.tv_en_name);

                ImageView img1=(ImageView) itemView.findViewById(R.id.img1);
                ImageView img2=(ImageView) itemView.findViewById(R.id.img2);
                ImageView img3=(ImageView) itemView.findViewById(R.id.img3);
                ImageView img4=(ImageView) itemView.findViewById(R.id.img4);
                ImageView img5=(ImageView) itemView.findViewById(R.id.img5);

                imgs.add(img1);
                imgs.add(img2);
                imgs.add(img3);
                imgs.add(img4);
                imgs.add(img5);
//                bg_line.setVisibility(View.GONE);
                MyApplication.imageLoader.displayImage(data.photo, img_logo, MyApplication.getListOptions());
                tv_location.setText(data.name+"");
                if(!TextUtils.isEmpty(data.introduction)){

                    re_instron.setVisibility(View.VISIBLE);
                    tv_instrution.setVisibility(View.VISIBLE);
                    tv_instrution.setText(data.introduction);
                }else {
                    tv_instrution.setVisibility(View.GONE);
                }

                tv_dis.setText(data.been+"");
                tv_pin_number.setText(data.commentCount+"");
                tv_pinfen.setText(data.star+"");
                if(!TextUtils.isEmpty(data.distance)){
                    tv_type_distances.setVisibility(View.VISIBLE);
                    tv_type_distances.setText(data.distance+"");
                }else {
                    tv_type_distances.setVisibility(View.GONE);
                }

                if(!TextUtils.isEmpty(data.cate)){
                    tv_type_names.setText(data.cate);
                }

                if (!TextUtils.isEmpty(data.price)){
                    tv_price.setText("¥ "+data.price);
                }else{
                    tv_price.setText("");
                }

                for(int l=0;l<imgs.size();l++){
                    if(l<5){
                        imgs.get(l).setImageResource(R.drawable.img_travel_startnoselect);
                    }
                }

                String str1 = (data.star+"").substring(0, (data.star+"").indexOf(".")) ;

                if(!TextUtils.isEmpty(str1)){
                    int s1 = Integer.parseInt(str1);
                    for(int k=0;k<s1;k++){
                        if(k<5){
                            imgs.get(k).setImageResource(R.drawable.img_travel_startselrct);
                        }
                    }

                }

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it=new Intent(MainMapActivity.this,BiWanDetialsActivity.class);

                            it.putExtra("cityId", cityId);
                            it.putExtra("bwanId", data.id);
                            it.putExtra("photo", data.photo);

                            startActivity(it);
                        }
                    });
                    liner_neard.addView(itemView);
            }
        }
    }

    List<ShopBean> bimai=new ArrayList<>();
    public void getBiMaiData(List<ShopBean> oldbimai){

        bimai.clear();
        liner_neard.removeAllViews();
        if(oldbimai!=null&&oldbimai.size() > 0) {

            bimai.addAll(oldbimai);
            for(int i=0;i<bimai.size();i++){
//                if(i<2){
                    final ShopBean data=bimai.get(i);

                    View itemView=getLayoutInflater().inflate(R.layout.item_travle_bichi,null);

                    ViewGroup parent = (ViewGroup) itemView.getParent();
                    if (parent != null) {
                        parent.removeAllViews();
                    }
                    itemView.setLayoutParams(new ViewGroup.LayoutParams(MyApplication.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));

                List<ImageView> imgs=new ArrayList<>();

                RelativeLayout re_instron=(RelativeLayout) itemView.findViewById(R.id.re_instron);
                ImageView img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
                TextView tv_location=(TextView) itemView.findViewById(R.id.tv_location);
//                TextView bg_line=(TextView) itemView.findViewById(bg_line);
                TextView tv_dis=(TextView) itemView.findViewById(R.id.tv_dis);
                TextView tv_type_names=(TextView) itemView.findViewById(R.id.tv_type_names);
                TextView tv_type_distances=(TextView) itemView.findViewById(R.id.tv_type_distances);
                TextView tv_pinfen=(TextView) itemView.findViewById(R.id.tv_pinfen);
                TextView tv_instrution=(TextView) itemView.findViewById(R.id.tv_instrution);
                TextView tv_price=(TextView) itemView.findViewById(R.id.tv_price);
                TextView tv_en_name=(TextView) itemView.findViewById(R.id.tv_en_name);

                ImageView img1=(ImageView) itemView.findViewById(R.id.img1);
                ImageView img2=(ImageView) itemView.findViewById(R.id.img2);
                ImageView img3=(ImageView) itemView.findViewById(R.id.img3);
                ImageView img4=(ImageView) itemView.findViewById(R.id.img4);
                ImageView img5=(ImageView) itemView.findViewById(R.id.img5);

                imgs.add(img1);
                imgs.add(img2);
                imgs.add(img3);
                imgs.add(img4);
                imgs.add(img5);

//                bg_line.setVisibility(View.GONE);
                MyApplication.imageLoader.displayImage(data.photo, img_logo, MyApplication.getListOptions());
                tv_location.setText(data.name+"");
                if(!TextUtils.isEmpty(data.introduction)){

                    re_instron.setVisibility(View.VISIBLE);
                    tv_instrution.setVisibility(View.VISIBLE);
                    tv_instrution.setText(data.introduction);
                }else {
                    tv_instrution.setVisibility(View.GONE);
                }

                tv_dis.setText(data.been+"");
                tv_pinfen.setText(data.star+"");
                if(!TextUtils.isEmpty(data.distance)){
                    tv_type_distances.setVisibility(View.VISIBLE);
                    tv_type_distances.setText(data.distance+"");
                }else {
                    tv_type_distances.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(data.price)){
                    tv_price.setText("¥ "+data.price);
                }else{
                    tv_price.setText("");
                }

                if(!TextUtils.isEmpty(data.cate)){
                    tv_type_names.setText(data.cate);
                }

                for(int l=0;l<imgs.size();l++){
                    if(l<5){
                        imgs.get(l).setImageResource(R.drawable.img_travel_startnoselect);
                    }
                }

                String str1 = (data.star+"").substring(0, (data.star+"").indexOf(".")) ;

                if(!TextUtils.isEmpty(str1)){
                    int s1 = Integer.parseInt(str1);
                    for(int k=0;k<s1;k++){
                        if(k<5){
                            imgs.get(k).setImageResource(R.drawable.img_travel_startselrct);
                        }
                    }

                }

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it=new Intent(MainMapActivity.this,BiMaiDetialsActivity.class);

                            it.putExtra("cityId", cityId);
                            it.putExtra("shopId", data.id);
                            it.putExtra("photo", data.photo);

                            startActivity(it);
                        }
                    });
                    liner_neard.addView(itemView);
            }
        }
    }
    List<RestauransBean> bichi=new ArrayList<>();
    public void getBiChiData(List<RestauransBean> oldbichi){


        bichi.clear();
        liner_neard.removeAllViews();
        if(oldbichi!=null&&oldbichi.size() > 0) {

            bichi.addAll(oldbichi);
            for(int i=0;i<bichi.size();i++){
//                if(i<2){
                    final RestauransBean data=bichi.get(i);

                    View itemView=getLayoutInflater().inflate(R.layout.item_travle_bichi,null);

                    ViewGroup parent = (ViewGroup) itemView.getParent();
                    if (parent != null) {
                        parent.removeAllViews();
                    }
                    itemView.setLayoutParams(new ViewGroup.LayoutParams(MyApplication.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));

                List<ImageView> imgs=new ArrayList<>();

                RelativeLayout re_instron=(RelativeLayout) itemView.findViewById(R.id.re_instron);
                ImageView img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
//                TextView bg_line=(TextView) itemView.findViewById(bg_line);
                TextView tv_location=(TextView) itemView.findViewById(R.id.tv_location);
                TextView tv_dis=(TextView) itemView.findViewById(R.id.tv_dis);
                TextView tv_type_names=(TextView) itemView.findViewById(R.id.tv_type_names);
                TextView tv_type_distances=(TextView) itemView.findViewById(R.id.tv_type_distances);
                TextView tv_pinfen=(TextView) itemView.findViewById(R.id.tv_pinfen);
                TextView tv_instrution=(TextView) itemView.findViewById(R.id.tv_instrution);
                TextView tv_price=(TextView) itemView.findViewById(R.id.tv_price);
                TextView tv_en_name=(TextView) itemView.findViewById(R.id.tv_en_name);

                ImageView img1=(ImageView) itemView.findViewById(R.id.img1);
                ImageView img2=(ImageView) itemView.findViewById(R.id.img2);
                ImageView img3=(ImageView) itemView.findViewById(R.id.img3);
                ImageView img4=(ImageView) itemView.findViewById(R.id.img4);
                ImageView img5=(ImageView) itemView.findViewById(R.id.img5);

                imgs.add(img1);
                imgs.add(img2);
                imgs.add(img3);
                imgs.add(img4);
                imgs.add(img5);

//                bg_line.setVisibility(View.GONE);
                MyApplication.imageLoader.displayImage(data.photo, img_logo, MyApplication.getListOptions());
                tv_location.setText(data.name+"");
                if(!TextUtils.isEmpty(data.introduction)){

                    re_instron.setVisibility(View.VISIBLE);
                    tv_instrution.setVisibility(View.VISIBLE);
                    tv_instrution.setText(data.introduction);
                }else {
                    tv_instrution.setVisibility(View.GONE);
                }

                tv_dis.setText(data.been+"");
                tv_pinfen.setText(data.star+"");
                if(!TextUtils.isEmpty(data.distance)){
                    tv_type_distances.setVisibility(View.VISIBLE);
                    tv_type_distances.setText(data.distance+"");
                }else {
                    tv_type_distances.setVisibility(View.GONE);
                }

                if(!TextUtils.isEmpty(data.cate)){
                    tv_type_names.setText(data.cate);
                }
                if (!TextUtils.isEmpty(data.price)){
                    tv_price.setText("¥ "+data.price);
                }else{
                    tv_price.setText("");
                }


                if(!TextUtils.isEmpty(data.english_name)){
                    tv_en_name.setText(data.english_name);
                }

                for(int l=0;l<imgs.size();l++){
                    if(l<5){
                        imgs.get(l).setImageResource(R.drawable.img_travel_startnoselect);
                    }
                }

                String str1 = (data.star+"").substring(0, (data.star+"").indexOf(".")) ;

                if(!TextUtils.isEmpty(str1)){
                    int s1 = Integer.parseInt(str1);
                    for(int k=0;k<s1;k++){
                        if(k<5){
                            imgs.get(k).setImageResource(R.drawable.img_travel_startselrct);
                        }
                    }

                }

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it=new Intent(MainMapActivity.this,BiChiDetialsActivity.class);

                            it.putExtra("cityId", cityId);
                            it.putExtra("chiId", data.id);
                            it.putExtra("chiphoto", data.photo);

                            startActivity(it);
                        }
                    });
                    liner_neard.addView(itemView);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        mMapView.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mMapView.onPause();
        mapView.onPause();
    }

    private Handler mhandle=new Handler();
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.relative_all:  /******查看全部******/
                tv_all.setBackground(getResources().getDrawable(R.drawable.bg_btn));
                tv_chi.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));
                tv_wan.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));
                tv_mai.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));

                if(markers!=null&&markers.size()>0){
                    for (int i=0;i<markers.size();i++){
                        markers.get(i).remove();
                    }

                    markers.clear();
                }

                getDatas();
                break;
            case R.id.relative_chi:
                tv_all.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));
                tv_chi.setBackground(getResources().getDrawable(R.drawable.bg_btn));
                tv_wan.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));
                tv_mai.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));

                ProgressDlgUtil.showProgressDlg("loading...",this);
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                    }
                }, 2000);
                if(markers!=null&&markers.size()>0){
                    for (int i=0;i<markers.size();i++){
                        markers.get(i).remove();
                    }
                    markers.clear();
                }

                restauransBeen= maps_chi.get("chi");
                getBiChiData(restauransBeen);
                if(restauransBeen!=null&&restauransBeen.size()>0){
                    for (int i=0;i<restauransBeen.size();i++){
                        String lng=restauransBeen.get(i).lng;
                        String lat=restauransBeen.get(i).lat;

                        if(!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(lat)){
                            LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                            MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(point, 14);
                            mMap.setMapStatus(mapStatus);
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.drawable.bichis);
                            //            构建MarkerOption，用于在地图上添加Marker
                            MarkerOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmap).zIndex(0).period(14);;
                            option.animateType(MarkerOptions.MarkerAnimateType.grow);
                            //            在地图上添加Marker，并显示
                            Marker marker=(Marker) mMap.addOverlay(option);
                            markers.add(marker);
                        }
                    }
                }

                break;
            case R.id.relative_wan:
                tv_all.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));
                tv_chi.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));
                tv_wan.setBackground(getResources().getDrawable(R.drawable.bg_btn));
                tv_mai.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));

                ProgressDlgUtil.showProgressDlg("loading...",this);
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                    }
                }, 2000);

                if(markers!=null&&markers.size()>0){
                    for (int i=0;i<markers.size();i++){
                        markers.get(i).remove();
                    }
                    markers.clear();
                }

                touristAttractions= maps_wan.get("wan");
                getBiWanData(touristAttractions);

                if(touristAttractions!=null&&touristAttractions.size()>0){
                    for (int i=0;i<touristAttractions.size();i++){
                        String lng=touristAttractions.get(i).lng;
                        String lat=touristAttractions.get(i).lat;

                        if(!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(lat)){
                            LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                            MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(point, 14);
                            mMap.setMapStatus(mapStatus);
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.drawable.biwans);
                            //            构建MarkerOption，用于在地图上添加Marker
                            MarkerOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmap).zIndex(0).period(14);;
                            option.animateType(MarkerOptions.MarkerAnimateType.grow);
                            //            在地图上添加Marker，并显示
                            Marker marker=(Marker) mMap.addOverlay(option);
                            markers.add(marker);
                        }
                    }
                }

                break;
            case R.id.relative_mai:
                tv_all.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));
                tv_chi.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));
                tv_wan.setBackground(getResources().getDrawable(R.drawable.bg_btn_select));
                tv_mai.setBackground(getResources().getDrawable(R.drawable.bg_btn));

                ProgressDlgUtil.showProgressDlg("loading...",this);
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                    }
                }, 2000);
                if(markers!=null&&markers.size()>0){
                    for (int i=0;i<markers.size();i++){
                        markers.get(i).remove();
                    }
                    markers.clear();
                }

                shopBeen= maps_mai.get("mai");
                getBiMaiData(shopBeen);
                if(shopBeen!=null&&shopBeen.size()>0){
                    for (int i=0;i<shopBeen.size();i++){
                        String lng=shopBeen.get(i).lng;
                        String lat=shopBeen.get(i).lat;

                        if(!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(lat)){
                            LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                            MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(point, 14);
                            mMap.setMapStatus(mapStatus);
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.drawable.bimais);
                            //            构建MarkerOption，用于在地图上添加Marker
                            MarkerOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmap).zIndex(0).period(14);;
                            option.animateType(MarkerOptions.MarkerAnimateType.grow);
                            //            在地图上添加Marker，并显示
                            Marker marker=(Marker) mMap.addOverlay(option);
                            markers.add(marker);
                        }
                    }
                }

                break;
        }
    }

    private void initMap(double lng,double lat) {
   //定义Maker坐标点
//        LatLng point = new LatLng(214.1462175, 114.400244);
        LatLng point = new LatLng(lat, lng);

        MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(point, 14);
        mMap.setMapStatus(mapStatus);

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.img_dibiao);
//            构建MarkerOption，用于在地图上添加Marker
        MarkerOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap).zIndex(0).period(14);;
        option.animateType(MarkerOptions.MarkerAnimateType.grow);
//            在地图上添加Marker，并显示
        mMap.addOverlay(option);

    }
}
