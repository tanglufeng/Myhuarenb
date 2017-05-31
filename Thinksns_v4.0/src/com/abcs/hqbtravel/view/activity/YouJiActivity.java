package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.abcs.hqbtravel.adapter.YouJiAdapter;
import com.abcs.hqbtravel.biz.YouJiListBiz;
import com.abcs.hqbtravel.donghua.JazzyHelper;
import com.abcs.hqbtravel.donghua.JazzyRecyclerViewScrollListener;
import com.abcs.hqbtravel.donghua.Utils;
import com.abcs.hqbtravel.entity.YouJi;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Map;

public class YouJiActivity extends Activity implements View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    View seperate;
    private EasyRecyclerView rv_youji;
    private ImageView img_back;
//    private TextView tv_nodata;
    private YouJiAdapter adapter;
    private int pageNo=1;
    private boolean isRefresh = false;
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
        setContentView(R.layout.activity_youji);

        cityId=getIntent().getStringExtra("cityId");

        seperate=(View)findViewById(R.id.seperate);
        rv_youji=(EasyRecyclerView)findViewById(R.id.rv_youji);
        img_back=(ImageView)findViewById(R.id.img_back);
//        tv_nodata=(TextView) findViewById(R.id.tv_nodata);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            seperate.setVisibility(View.VISIBLE);
        }
        rv_youji.setOnScrollListener(jazzyScrollListener=new JazzyRecyclerViewScrollListener());
        if (savedInstanceState != null) {
            mCurrentTransitionEffect = savedInstanceState.getInt(KEY_TRANSITION_EFFECT, JazzyHelper.GROW);
            setupJazziness(mCurrentTransitionEffect);
        }

        img_back.setOnClickListener(this);

        rv_youji.setLayoutManager(new LinearLayoutManager(this));
        rv_youji.setRefreshListener(this);
        rv_youji.setAdapter(adapter = new YouJiAdapter(this));
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                YouJi.BodyEntity.ItemsEntity itemsEntity = adapter.getAllData().get(position);
                Intent it = new Intent(YouJiActivity.this, YouJiDetialsActivity.class);
                it.putExtra("youjiId", itemsEntity.id);
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
            getYouJi();
        }
    }

    private String cityId;
//    private  int cityId=11593;
    private int pageSize=10;

    private void getYouJi() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);

        YouJiListBiz.getInstance(this).getYouJiList(cityId,pageSize,pageNo,ser_context,new Response.Listener<YouJi>(){
            @Override
            public void onResponse(YouJi response) {
                if(response!=null){
                    ProgressDlgUtil.stopProgressDlg();
                    if (isRefresh) {
                        adapter.clear();
                        isRefresh = false;
                    }

                    if(response.result==1){
                        if(response.body.items!=null){
                            if(response.body.items.size()>0){
                                adapter.addAll(response.body.items);
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(YouJiActivity.this,"无数据！", Toast.LENGTH_LONG).show();
                            }
                        }
                        pageNo = response.body.next;

                        rv_youji.setRefreshing(false);
                    }

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(YouJiActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                ProgressDlgUtil.stopProgressDlg();
            }
        }, null);

//
//        YouJi youJi=new Gson().fromJson(KEY_JSON,YouJi.class);
//                      if(youJi!=null){
//                            dialog.dismiss();
//                            if (isRefresh) {
//                                adapter.clear();
//                                isRefresh = false;
//                            }
//                            adapter.addAll(youJi.body.items);
//                            adapter.notifyDataSetChanged();
//                            pageNo = youJi.body.next;
//                        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    public void onLoadMore() {
        if (pageNo == -1) {
            adapter.stopMore();
            return;
        }
        getYouJi();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isRefresh = true;
        getYouJi();
    }

    public void onseacher(View view){
        Intent tt=new Intent(this,SeachYouJiByTagActivity.class);
        tt.putExtra("cityId",cityId);
//        tt.putExtra("pageSize",pageSize+"");
//        tt.putExtra("pageNo","1");
        tt.putExtra("type","youji");
        startActivity(tt);
//        startActivityForResult(tt,YOU_JI_REQUEST_CODE);
    }

    private String ser_context=null;
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==YOU_JI_REQUEST_CODE&&resultCode==YOU_JI_REPONSE_CODE&&data!=null){
//            ser_context=data.getStringExtra("ser_context");
////            YouJi items=(YouJi) data.getBundleExtra("bundle").getSerializable("respone");
//              YouJi items=new Gson().fromJson(HQBApplication.KEY_YOUJI_SARCHER,YouJi.class);
//            Log.i("json:","内容    "+ser_context);
//            Log.i("json:","YouJi搜索返回1    "+HQBApplication.KEY_YOUJI_SARCHER);
//            Log.i("json:","YouJi搜索返回2    "+new Gson().toJson(items));
//
//                 adapter.clear();
//                 adapter.notifyDataSetChanged();
//
//                if(items!=null&&items.body.items!=null&&items.body.items.size()>0){
//                    tv_nodata.setVisibility(View.GONE);
//                    adapter.addAll(items.body.items);
//                    adapter.notifyDataSetChanged();
//                }else {
//                    tv_nodata.setVisibility(View.VISIBLE);
//                }
//        }
//    }
//
//    public static final int YOU_JI_REQUEST_CODE = 1020;
//    public static final int YOU_JI_REPONSE_CODE = 1021;
}
