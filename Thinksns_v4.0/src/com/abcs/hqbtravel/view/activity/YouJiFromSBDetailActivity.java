package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.adapter.YouJiAdapter;
import com.abcs.hqbtravel.donghua.JazzyHelper;
import com.abcs.hqbtravel.donghua.JazzyRecyclerViewScrollListener;
import com.abcs.hqbtravel.donghua.Utils;
import com.abcs.hqbtravel.entity.YouJi;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Map;

public class YouJiFromSBDetailActivity extends Activity implements View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    private EasyRecyclerView rv_youji;
    private ImageView img_back;
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
        detialsId=getIntent().getStringExtra("detialsId");
        type=getIntent().getStringExtra("type");

        rv_youji=(EasyRecyclerView)findViewById(R.id.rv_youji);
        img_back=(ImageView)findViewById(R.id.img_back);

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
                Intent it = new Intent(YouJiFromSBDetailActivity.this, YouJiDetialsActivity.class);
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

        if(!TextUtils.isEmpty(detialsId)){
            getYouJi();
        }
    }

    private String detialsId,type,cityId;
//    private  int detialsId=11593;
    private int pageSize=10;

    Handler mHandler=new Handler();

    private void getYouJi() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);

        Log.i("zds_youjifromsb=url",Contonst.HOST+"/find/getNoteBypoi?pageSize=10&pageNo="+pageNo+"&type="+type+"+&id="+detialsId);
//        http://120.24.19.29:7075/find/getNoteBypoi?pageSize=10&id=35026&pageNo=1&type=3
        HttpRequest.sendGet(Contonst.HOST+"/find/getNoteBypoi", "pageSize=10&pageNo="+pageNo+"&type="+type+"+&id="+detialsId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        ProgressDlgUtil.stopProgressDlg();
                        if(!TextUtils.isEmpty(msg)){
                            Log.i("zds_youjifromsbdetisl",msg+"");
                            YouJi response=new Gson().fromJson(msg,YouJi.class);
                            if(response!=null){
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
                                            adapter.stopMore();
//                                            Toast.makeText(YouJiFromSBDetailActivity.this,"无数据！", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    pageNo = response.body.next;

                                    rv_youji.setRefreshing(false);
                                }
                            }
                        }
                    }
                });
            }
        });
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
}
