package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.hqbtravel.biz.ServerDetialsBiz;
import com.abcs.hqbtravel.entity.ServerDetail;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.view.CircleImageView;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServerDatialsActivity extends Activity implements View.OnClickListener{

    private ImageView img_logo;
    private ImageView img_back;
    private ImageView img_sex;
    private TextView tv_title;
    private TextView tv_others_price;
    private TextView tv_age;
    private TextView tv_server_xieyi;
    private TextView tv_viewpa_number;
    private TextView is_renzheng;
    private TextView tv_server_detials;
    private TextView tv_share_right;
    private ViewPager viewPager;
    private TextView tv_price;
    private TextView tv_naem;
    private TextView tv_zhuye;
    private LinearLayout liner_others_servers;
    private CircleImageView img_avatar;
    private CircleImageView img_avatar_pinjia;
    private ViewPagerAdapter adapter;
    private List<Map<String, Object>> data;


    private String serverId;
    private List<String> banners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_server_datials);


        initViews();
        setOnClick();
        if(!TextUtils.isEmpty(serverId)){
            loadDatas();
        }
    }

    ProgressDialog dialog;
    private void loadDatas(){
        dialog.setMessage("加载中...");
        dialog.show();
        ServerDetialsBiz.getInstance(this).getServerDetials(serverId,new Response.Listener<ServerDetail>(){
            @Override
            public void onResponse(ServerDetail response) {
                if(response!=null){
                    dialog.dismiss();
                    if(response.body.service.banners!=null&&response.body.service.banners.size()>0){
                        banners=response.body.service.banners;
                        data = getData(banners);
                        adapter = new ViewPagerAdapter(data);
                        viewPager.setAdapter(adapter);
                    }
                    tv_viewpa_number.setText("1/"+banners.size());

                    tv_price.setText(response.body.service.currency+response.body.service.price+"/"+response.body.service.chargeUnit);
                    tv_naem.setText(response.body.service.assistant.name+"");
                    tv_age.setText(response.body.service.assistant.age+"");
                    tv_server_detials.setText(response.body.service.desc+"");

                    /******其他服务********/
                if(response.body.service.others!=null&&response.body.service.others.size()>0){
                    for(int i=0;i<response.body.service.others.size();i++){
                        View itemView=getLayoutInflater().inflate(R.layout.item_detial_server_others,null);
                        ViewGroup parent = (ViewGroup) itemView.getParent();
                        if (parent != null) {
                            parent.removeAllViews();
                        }
                        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
                        tv_title=(TextView) itemView.findViewById(R.id.tv_title);
                        tv_others_price=(TextView) itemView.findViewById(R.id.tv_others_price);
                        Log.i("其他服务大小",""+response.body.service.others.size());
                        MyApplication.imageLoader.displayImage(response.body.service.others.get(i).logo, img_logo, MyApplication.getListOptions());
                        tv_title.setText(response.body.service.others.get(i).name+"");
//                        tv_others_price.setText(response.body.service.others.get(i).currency+response.body.service.others.get(i).price+"/"+response.body.service.others.get(i).chargeUnit);
                        tv_others_price.setText(response.body.service.others.get(i).price+"/小时");

                        liner_others_servers.addView(itemView);
                    }
                }
                    if(response.body.service.assistant.sex==0){  // 0 男  1  女
                        img_sex.setBackgroundResource(R.drawable.img_travel_sexm);
                    }else{
                        img_sex.setBackgroundResource(R.drawable.img_travel_sexf);
                    }
                    if(response.body.service.negotiate==true){  // true  显示双方协议  false 显示时间和地点
                        tv_server_xieyi.setText(response.body.service.time+"/"+response.body.service.site);
                    }else{
                        tv_server_xieyi.setText("双方协议");
                    }
                    MyApplication.imageLoader.displayImage(response.body.service.assistant.avator, img_avatar, MyApplication.getListOptions());

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ServerDatialsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }, null);
    }
    private void initViews(){
        dialog=new ProgressDialog(this);
        serverId=getIntent().getStringExtra("serverId");
        img_back=(ImageView) findViewById(R.id.img_back);
        liner_others_servers=(LinearLayout) findViewById(R.id.liner_others_servers);

        img_sex=(ImageView) findViewById(R.id.img_sex);
        tv_age=(TextView) findViewById(R.id.tv_age);
        tv_server_xieyi=(TextView) findViewById(R.id.tv_server_xieyi);
        tv_viewpa_number=(TextView) findViewById(R.id.tv_viewpa_number);
        is_renzheng=(TextView) findViewById(R.id.is_renzheng);
        tv_server_detials=(TextView) findViewById(R.id.tv_server_detials);
        tv_share_right=(TextView) findViewById(R.id.tv_share_right);
        tv_price=(TextView) findViewById(R.id.tv_price);
        tv_naem=(TextView) findViewById(R.id.tv_naem);
        tv_zhuye=(TextView) findViewById(R.id.tv_zhuye);
        viewPager=(ViewPager) findViewById(R.id.viewPager);
        img_avatar=(CircleImageView) findViewById(R.id.img_avatar);
        img_avatar_pinjia=(CircleImageView) findViewById(R.id.img_avatar_pinjia);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int page, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int page) {
                tv_viewpa_number.setText((page+1)+"/"+banners.size());
            }
        });
    }

    public List<Map<String, Object>> getData(List<String> banners) {
        List<Map<String, Object>> mdata = new ArrayList<Map<String, Object>>();

        for(int i=0;i<banners.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", banners.get(i));
            map.put("view", new ImageView(this));
            mdata.add(map);
        }
        return  mdata;
    }

    private void setOnClick(){
        img_back.setOnClickListener(this);
        tv_share_right.setOnClickListener(this);
        tv_zhuye.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_share_right:
                break;
            case R.id.tv_zhuye:
                break;
        }
    }
    public class ViewPagerAdapter extends PagerAdapter {

        List<Map<String,Object>> viewLists;

        public ViewPagerAdapter(List<Map<String,Object>> lists)
        {
            viewLists = lists;
        }

        @Override
        public int getCount() {    //获得size
            // TODO Auto-generated method stub
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View view, int position, Object object)      //销毁Item
        {
            ImageView x=(ImageView)viewLists.get(position).get("view");
            x.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ((ViewPager) view).removeView(x);
        }

        @Override
        public Object instantiateItem(View view, int position)  //实例化Item
        {
            ImageView x=(ImageView)viewLists.get(position).get("view");
            x.setScaleType(ImageView.ScaleType.FIT_CENTER);
            MyApplication.imageLoader.displayImage(viewLists.get(position).get("url").toString(), x, MyApplication.getListOptions());
            ((ViewPager) view).addView(x, 0);

            return viewLists.get(position).get("view");
        }

    }
}
