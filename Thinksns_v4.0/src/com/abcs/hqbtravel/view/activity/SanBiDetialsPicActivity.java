package com.abcs.hqbtravel.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.abcs.haiwaigou.activity.PhotoPreview_BiDetialsActivity;
import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.adapter.SanBiDetialsAdapter;
import com.abcs.hqbtravel.entity.SanBiDetialsPic;
import com.abcs.hqbtravel.wedgt.SpaceItemDecoration;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;

public class SanBiDetialsPicActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener ,RecyclerArrayAdapter.OnLoadMoreListener, View.OnClickListener {


    private EasyRecyclerView rv_dhdetials;
    private ImageView imageView;
    private String detialsId;
    private String type;
    private SanBiDetialsAdapter adapter;

    private Handler mhandle = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_san_bi_detials_pic);

        detialsId = getIntent().getStringExtra("detialsId");
        type = getIntent().getStringExtra("type");
        Log.i("xuke111","detialsId="+detialsId +",type="+type);

        initData();
        initViews();
        initListen();
    }

    private ArrayList<String> Urllist = new ArrayList<>();
    private SanBiDetialsPic sanBiDetialsPic;
    private int pageNo=1;
    private boolean isRefresh = false;

    //请求网络加载数据
    private void initData() {
        Log.i("xuke111", "加载三必图片的url:" + Contonst.HOST + "/getImgBytype" + "?" + "detailId=" + detialsId + "&pageNo=1" + "&pageSize=10" + "&type=" + type);
        HttpRequest.sendGet(Contonst.HOST + "/getImgBytype", "detailId=" + detialsId + "&pageNo="+pageNo + "&pageSize=10" + "&type=" + type, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                mhandle.post(new Runnable() {
                    @Override
                    public void run() {
                        sanBiDetialsPic = new Gson().fromJson(msg, SanBiDetialsPic.class);
                        if (sanBiDetialsPic != null) {
                            ProgressDlgUtil.stopProgressDlg();

                            if (isRefresh) {
                                adapter.clear();
                                isRefresh = false;
                            }

                            if (sanBiDetialsPic.result == 1) {
                                if (sanBiDetialsPic.body.items != null && sanBiDetialsPic.body.items.size() > 0) {   //否则没数据
                                    adapter.addAll(sanBiDetialsPic.body.items);
                                    adapter.notifyDataSetChanged();

                                    Urllist.clear();
                                    for (int i = 0 ;i<sanBiDetialsPic.body.items.size();i++){
                                        Urllist.add(sanBiDetialsPic.body.items.get(i).oss_url);
                                    }
                                }
                            }
                        }

                    }
                });
            }
        });

    }

    private void initViews() {
        imageView = (ImageView) findViewById(R.id.img_back);
        rv_dhdetials = (EasyRecyclerView) findViewById(R.id.rv_dhdetials);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rv_dhdetials.addItemDecoration(new SpaceItemDecoration(5));   //item间隔
        rv_dhdetials.setLayoutManager(manager);
        rv_dhdetials.setRefreshListener(this);  //刷新

        adapter = new SanBiDetialsAdapter(this);
        rv_dhdetials.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("position", position+"");
                intent.putExtra("ulist", Urllist);
                intent.setClass(SanBiDetialsPicActivity.this, PhotoPreview_BiDetialsActivity.class);
                SanBiDetialsPicActivity.this.startActivity(intent);

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

    }

    private void initListen() {
        imageView.setOnClickListener(this);
    }


    @Override
    public void onRefresh() {
        pageNo = 1;
        isRefresh = true;
        initData();
        mhandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_dhdetials.setRefreshing(false);
            }
        },1000);
    }

    @Override
    public void onLoadMore() {
        adapter.stopMore();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }
}
