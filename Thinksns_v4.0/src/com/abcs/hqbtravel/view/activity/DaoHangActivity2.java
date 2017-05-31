package com.abcs.hqbtravel.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.hqbtravel.adapter.DaoHangAdapter2;
import com.abcs.hqbtravel.biz.DaoHangBiz;
import com.abcs.hqbtravel.donghua.JazzyHelper;
import com.abcs.hqbtravel.donghua.JazzyRecyclerViewScrollListener;
import com.abcs.hqbtravel.donghua.Utils;
import com.abcs.hqbtravel.entity.DaoHang2;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.view.CircleImageView;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Map;

public class DaoHangActivity2 extends Activity implements View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    View seperate;
    private EasyRecyclerView rvDaoHang;
    private ImageView img_back;
    private DaoHangAdapter2 adapter;
    private int pageNo=1;
    private boolean isRefresh = false;
    private String cityId;
    private View headerView;
    TextView jindian_numbers;
    CircleImageView jindian_logo;
    TextView tv_wendu;
    TextView tv_name,tv_progress;
    JazzyRecyclerViewScrollListener jazzyScrollListener;
    private static final String KEY_TRANSITION_EFFECT = "transition_effect";

    private Map<String, Integer> mEffectMap;
    private int mCurrentTransitionEffect = JazzyHelper.GROW;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mEffectMap = Utils.buildEffectMap(this);
        Utils.populateEffectMenu(menu, new ArrayList<>(mEffectMap.keySet()), this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String strEffect = item.getTitle().toString();
        Toast.makeText(this, strEffect, Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_daohang2);

        cityId=getIntent().getStringExtra("cityId");
        rvDaoHang=(EasyRecyclerView)findViewById(R.id.rv_daohang);
        img_back=(ImageView)findViewById(R.id.img_back);
        tv_progress=(TextView) findViewById(R.id.tv_progress);

        seperate=(View)findViewById(R.id.seperate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            seperate.setVisibility(View.VISIBLE);
        }
        rvDaoHang.setOnScrollListener(jazzyScrollListener=new JazzyRecyclerViewScrollListener());
        if (savedInstanceState != null) {
            mCurrentTransitionEffect = savedInstanceState.getInt(KEY_TRANSITION_EFFECT, JazzyHelper.GROW);
            setupJazziness(mCurrentTransitionEffect);
        }


        img_back.setOnClickListener(this);

        rvDaoHang.setLayoutManager(new LinearLayoutManager(this));
        rvDaoHang.setRefreshListener(this);
        adapter = new DaoHangAdapter2(this);

        headerView = View.inflate(this,R.layout.item_top_daohang2,null);
        jindian_numbers=(TextView) headerView.findViewById(R.id.tv_numbers);
         jindian_logo=(CircleImageView) headerView.findViewById(R.id.img_logo);
         tv_wendu=(TextView) headerView.findViewById(R.id.tv_wendu);
         tv_name=(TextView) headerView.findViewById(R.id.tv_name);

        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
        rvDaoHang.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                final DaoHang2.BodyEntity.ItemsEntity itemsEntity = adapter.getAllData().get(position);
//
                Intent it = new Intent(DaoHangActivity2.this, DaoHangDetialsActivity2.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("items",itemsEntity);
                it.putExtra("dahangid", itemsEntity.id);
                startActivity(it);
//
//              if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
//                  File file= new File(Environment.getExternalStorageDirectory()+"/"+"file"+"/",itemsEntity.id+".mp3");
//                  if(file.getPath()!=null){
//                      Log.i("mp3",file.getPath().toString());
//
//                  }else {
//                      new downloads().downAsynFile(itemsEntity.audioUrl,itemsEntity.id,uiProgressResponseListener);
////                      DownloadProUtil.showProgressDlg("正在下载", itemsEntity.audioUrl, itemsEntity.id, DaoHangActivity2.this, true, new Complete() {
////                          @Override
////                          public void complete() {
////
////                          }
////                      });
//                  }
//              }else {
//                  Log.i("抱歉！","SD 不存在！");
//              }
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
            getDaoHang();
        }
    }

    private Handler mhandle=new Handler();
    private String pageSize="10";
    private void getDaoHang() {

        DaoHangBiz.getInstance(this).getDaoHangList(cityId,pageNo+"",pageSize,new Response.Listener<DaoHang2>(){
            @Override
            public void onResponse(DaoHang2 response) {
                if(response!=null){
                    ProgressDlgUtil.stopProgressDlg();
                    if (isRefresh) {
                        adapter.clear();
                        isRefresh = false;
                    }

                    if(response.result==1){
                        if(response.body!=null){
                            if(!TextUtils.isEmpty(response.body.cityPhoto)){

                                MyApplication.imageLoader.displayImage(response.body.cityPhoto, jindian_logo, MyApplication.getListOptions());
                            }

//                            if(!TextUtils.isEmpty(response.body.pageCount+"")){
//
//                                jindian_numbers.setText(response.body.pageCount+"");
//                            }

                            if(!TextUtils.isEmpty(response.body.highEstTemperature)){
                                tv_wendu.setText(response.body.highEstTemperature+"");
                            }

                            if(!TextUtils.isEmpty(response.body.cityName)){
                                tv_name.setText(response.body.cityName+"");
                            }
                        }

                        if(response.body.items!=null){
                            if(response.body.items.size()>0){
                                jindian_numbers.setText(response.body.items.size()+"");
                                adapter.addAll(response.body.items);
                                adapter.notifyDataSetChanged();
                            }else {
                                if(adapter.getCount()>0){
                                    return;
                                }else {
                                    rvDaoHang.removeAllViews();
                                    adapter.notifyDataSetChanged();
                                }
                                Toast.makeText(DaoHangActivity2.this,"暂无数据！", Toast.LENGTH_LONG).show();
                            }
                        }

                        pageNo = response.body.next;
                        if (pageNo == -1) {
                            adapter.stopMore();
                            return;
                        }
                    }

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDlgUtil.stopProgressDlg();
//                Toast.makeText(DaoHangActivity2.this,,Toast.LENGTH_LONG).show();
            }
        }, null);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
//                overridePendingTransition(R.anim.exit_anim,R.anim.enter_anim);
                break;
        }
    }

    @Override
    public void onLoadMore() {
        if (pageNo == -1) {
            adapter.stopMore();
            return;
        }
        getDaoHang();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isRefresh = true;
        getDaoHang();
        mhandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                rvDaoHang.setRefreshing(false);
            }
        },2000);
    }
}
