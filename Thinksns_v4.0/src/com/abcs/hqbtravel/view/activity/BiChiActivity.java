package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.hqbtravel.adapter.BiChiAdapter;
import com.abcs.hqbtravel.adapter.MyListAdapter;
import com.abcs.hqbtravel.adapter.MyListRightAdapter;
import com.abcs.hqbtravel.adapter.TravelSXGridAdapter;
import com.abcs.hqbtravel.biz.BiChiBiz;
import com.abcs.hqbtravel.donghua.JazzyHelper;
import com.abcs.hqbtravel.donghua.JazzyRecyclerViewScrollListener;
import com.abcs.hqbtravel.donghua.Utils;
import com.abcs.hqbtravel.entity.BiChi;
import com.abcs.hqbtravel.entity.RestauransBean;
import com.abcs.hqbtravel.entity.ShaiXuanBean;
import com.abcs.hqbtravel.wedgt.ACache;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Map;


public class BiChiActivity extends Activity implements  View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener  {

    private EasyRecyclerView rvBiChi;
    View seperate;
    private TextView tv_location;
    public ImageView img_back,img_shangqu,img_meishi;
    GridView gridview_select;
    private BiChiAdapter adapter;
    private int pageNo=1;
    private boolean isRefresh = false;
    JazzyRecyclerViewScrollListener jazzyScrollListener;
    private static final String KEY_TRANSITION_EFFECT = "transition_effect";

    private Map<String, Integer> mEffectMap;
    private int mCurrentTransitionEffect = JazzyHelper.GROW;

    private ACache aCache;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mEffectMap = Utils.buildEffectMap(this);
        Utils.populateEffectMenu(menu, new ArrayList<>(mEffectMap.keySet()), this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String strEffect = item.getTitle().toString();
//        Toast.makeText(this, strEffect, Toast.LENGTH_SHORT).show();
        setupJazziness(mEffectMap.get(strEffect));
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TRANSITION_EFFECT, mCurrentTransitionEffect);
    }

    private void setupJazziness(int effect) {
        mCurrentTransitionEffect = effect;
        jazzyScrollListener.setTransitionEffect(mCurrentTransitionEffect);
    }


//    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_bi_chi);

        aCache=ACache.get(this);

        cityId=getIntent().getStringExtra("cityId");
        lat=getIntent().getStringExtra("current_lat");
        lng=getIntent().getStringExtra("current_lng");

        rvBiChi=(EasyRecyclerView)findViewById(R.id.rv_bichi);
        img_back=(ImageView)findViewById(R.id.img_back);
        img_shangqu=(ImageView)findViewById(R.id.img_shangqu);
        gridview_select = (GridView) findViewById(R.id.gridview_select);
        seperate=(View)findViewById(R.id.seperate);
        tv_location=(TextView)findViewById(R.id.tv_location);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            seperate.setVisibility(View.VISIBLE);
        }*/

        rvBiChi.setOnScrollListener(jazzyScrollListener=new JazzyRecyclerViewScrollListener());
        if (savedInstanceState != null) {
            mCurrentTransitionEffect = savedInstanceState.getInt(KEY_TRANSITION_EFFECT, JazzyHelper.GROW);
            setupJazziness(mCurrentTransitionEffect);
        }

        img_back.setOnClickListener(this);

        rvBiChi.setLayoutManager(new LinearLayoutManager(this));
        rvBiChi.setRefreshListener(this);
        rvBiChi.setAdapter(adapter = new BiChiAdapter(this));
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                RestauransBean itemsEntity = adapter.getAllData().get(position);
                Intent it = new Intent(BiChiActivity.this, BiChiDetialsActivity.class);
                it.putExtra("chiId", itemsEntity.id);
                it.putExtra("cityId", cityId);
                it.putExtra("chiphoto", itemsEntity.photo);
                startActivity(it);
            }
        });

        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });

        if(!TextUtils.isEmpty(cityId)){
            ProgressDlgUtil.showProgressDlg("Loading...", this);
            initSX();
            getBiChi();
        }
    }

    private TravelSXGridAdapter travelSXGridAdapter;

    private void initSX(){



       /* JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, "http://newapi.tuling.me/travel/find/getFilter?cate=2&cityId="+cityId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("sx",response.toString()+"");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("zds", error.getMessage());
            }
        });
        mRequestQueue.add(jr);*/


//        http://newapi.tuling.me/travel/find/getFilter?cate=2&cityId=54
        HttpRequest.sendGet("http://newapi.tuling.me/travel/find/getFilter","cate=2&cityId="+cityId,new HttpRevMsg(){
            //        HttpRequest.sendGet(TLUrl.getInstance().URL_cewsu,"act=test_cy&op=find_server",new HttpRevMsg(){
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("sx",msg+"");
                        if(!TextUtils.isEmpty(msg)){

                            final ShaiXuanBean bean=new Gson().fromJson(msg,ShaiXuanBean.class);

                            if(bean.result==1){
                                if(bean.body!=null&&bean.body.size()>0){
                                    travelSXGridAdapter=new TravelSXGridAdapter(BiChiActivity.this,bean.body);
                                    gridview_select.setAdapter(travelSXGridAdapter);

                                    gridview_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            ShaiXuanBean.BodyBean item=(ShaiXuanBean.BodyBean)parent.getItemAtPosition(position);
                                            if(item.filter!=null){

                                                if(item.filter.size()>0){
                                                    iiPop();

                                                    myAdapter= new MyListAdapter(BiChiActivity.this,item.filter);
                                                    listview.setAdapter(myAdapter);

                                                    myAdapter.setSelectedPosition(0);
                                                    myAdapter.notifyDataSetChanged();

                                                    myRifhtAdapter= new MyListRightAdapter(BiChiActivity.this);

                                                    ShaiXuanBean.BodyBean.FilterBean bodyBean= (ShaiXuanBean.BodyBean.FilterBean)myAdapter.getItem(0);

                                                    if(bodyBean!=null){
                                                        if(bodyBean.childName.size()>0){
                                                            rvList.setAdapter(myRifhtAdapter);
                                                            myRifhtAdapter.addAllData(bodyBean.childName);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    private void iiPop(){

        View itemView = View.inflate(this, R.layout.popup_sanbi_saixuan, null);
        listview=(ListView) itemView.findViewById(R.id.listview);
        rvList=(ListView) itemView.findViewById(R.id.listview_datas);

        popupWindow = new PopupWindow(itemView, Util.WIDTH, Util.HEIGHT*3/4);
        //触摸点击事件
        popupWindow.setTouchable(true);
        //聚集
        popupWindow.setFocusable(true);
        //设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow()
                        .getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        popupWindow.showAsDropDown(gridview_select);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myAdapter.setSelectedPosition(position);
                myAdapter.notifyDataSetChanged();

                ShaiXuanBean.BodyBean.FilterBean item=(ShaiXuanBean.BodyBean.FilterBean)parent.getItemAtPosition(position);
                if(item.childName!=null){
                    if(item.childName.size()>0){
                        rvList.setVisibility(View.VISIBLE);
                        rvList.setAdapter(myRifhtAdapter);
                        myRifhtAdapter.addAllData(item.childName);
                    }else {
                        rvList.setAdapter(null);
                        myRifhtAdapter.notifyDataSetChanged();
                        type=item.id;
                        getData();
                        adapter.clear();
                        popupWindow.dismiss();
                    }
                }
            }
        });

        rvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ShaiXuanBean.BodyBean.FilterBean.ChildNameBean bodyBean = (ShaiXuanBean.BodyBean.FilterBean.ChildNameBean)parent.getItemAtPosition(position);
                child_type = bodyBean.id;
                Log.i("zds","child_type:"+child_type);
                adapter.clear();
                getDataChild();
                isClickSX=true;
                popupWindow.dismiss();
            }
        });


    }



    private String cityId;
    private String lng;
    private String lat;
//    private String cityId="7823";
    private void getBiChi() {

        BiChiBiz.getInstance(this).getBiChiList(cityId,pageNo,lng,lat,new Response.Listener<BiChi>(){
            @Override
            public void onResponse(BiChi response) {
                if(response!=null){
//                    dialog.dismiss();
                    ProgressDlgUtil.stopProgressDlg();
                    if (isRefresh) {
                        adapter.clear();
                        isRefresh = false;
                    }

                    if(response.result==1){
                        if(!TextUtils.isEmpty(MyApplication.getLocation())){

                            tv_location.setText(MyApplication.getLocation()+"");
                        }
                        if(response.body.cates!=null){

                            if(response.body.cates.size()>0){
                                adapter.addAll(response.body.cates);
                            }else {
                                Toast.makeText(BiChiActivity.this,"暂无数据！", Toast.LENGTH_LONG).show();
                            }
                            adapter.notifyDataSetChanged();
                        }
                        pageNo = response.body.next;
                    }

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDlgUtil.stopProgressDlg();
            }
        }, null);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }

    private Handler handler = new Handler();

    private void getData() {
        pageNo =1;
        BiChiBiz.getInstance(this).getBiChiList(cityId,type+"",pageNo,lng,lat,new Response.Listener<BiChi>() {
            @Override
            public void onResponse(BiChi response) {
                if (response != null) {
                    ProgressDlgUtil.stopProgressDlg();
                    if (isRefresh) {
                        adapter.clear();
                        isRefresh = false;
                    }

                    if (response.result == 1) {
                        if (response.body.cates != null) {
                            if (response.body.cates.size() > 0) {
                                adapter.clear();
                                adapter.addAll(response.body.cates);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(BiChiActivity.this, "暂无数据", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(BiChiActivity.this, "暂无数据", Toast.LENGTH_LONG).show();
                        }
                        pageNo = response.body.next;
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BiChiActivity.this, "无数据", Toast.LENGTH_LONG).show();
                ProgressDlgUtil.stopProgressDlg();
            }
        }, null);

    }
    private void getDataChild() {
        pageNo =1;
        BiChiBiz.getInstance(this).getBiChiListChild(cityId,child_type+"",pageNo,lng,lat,new Response.Listener<BiChi>() {
            @Override
            public void onResponse(BiChi response) {
                if (response != null) {
                    ProgressDlgUtil.stopProgressDlg();
                    if (isRefresh) {
                        adapter.clear();
                        isRefresh = false;
                    }

                    if (response.result == 1) {
                        if (response.body.cates != null) {
                            if (response.body.cates.size() > 0) {
                                adapter.clear();
                                adapter.addAll(response.body.cates);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(BiChiActivity.this, "暂无数据", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(BiChiActivity.this, "暂无数据", Toast.LENGTH_LONG).show();
                        }
                        pageNo = response.body.next;
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BiChiActivity.this, "无数据", Toast.LENGTH_LONG).show();
                ProgressDlgUtil.stopProgressDlg();
            }
        }, null);

    }

    private ListView listview;
    private ListView rvList;
    private MyListAdapter myAdapter;
    private MyListRightAdapter myRifhtAdapter;

    private PopupWindow popupWindow;
    private  int child_type=0,type=0;

    private boolean isClickSX=false;
    @Override
    public void onLoadMore() {
        if (pageNo == -1) {
            adapter.stopMore();
            return;
        }
        getBiChi();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isRefresh = true;

        if(isClickSX){
            getData();
        }else {
            getBiChi();
        }
    }
}