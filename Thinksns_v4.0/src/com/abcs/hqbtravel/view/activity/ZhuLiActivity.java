package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.adapter.ZhuLiAdapter;
import com.abcs.hqbtravel.biz.ZhuLiBiz;
import com.abcs.hqbtravel.entity.ZhuLi;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class ZhuLiActivity extends Activity implements  View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener  {

    private EasyRecyclerView rvZhuLi;
    private ImageView img_back;
    private TextView tv_mune_right;
    private ZhuLiAdapter adapter;
    private int pageNo=1;
    private boolean isRefresh = false;
//    private String cityId="7823";
    private String cityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_travle_zhu_li);

        dialog=new ProgressDialog(this);
        cityId=getIntent().getStringExtra("cityId");
        rvZhuLi=(EasyRecyclerView)findViewById(R.id.rv_zhuli);
        img_back=(ImageView)findViewById(R.id.img_back);
        tv_mune_right=(TextView) findViewById(R.id.tv_mune_right);

        img_back.setOnClickListener(this);
        tv_mune_right.setOnClickListener(this);


        rvZhuLi.setLayoutManager(new LinearLayoutManager(this));
        rvZhuLi.setRefreshListener(this);
        rvZhuLi.setRefreshingColorResources(R.color.tljr_statusbarcolor);
        rvZhuLi.setAdapter(adapter = new ZhuLiAdapter(this));
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ZhuLi.BodyEntity.ServicesEntity entity=adapter.getAllData().get(position);
                Intent it3=new Intent(ZhuLiActivity.this,ServerDatialsActivity.class);
                it3.putExtra("serverId",entity.id);
                startActivity(it3);
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
            getZhuli();
        }
    }

    ProgressDialog dialog;

    private void getZhuli() {
        dialog.setMessage("加载中...");
        dialog.show();
        ZhuLiBiz.getInstance(this).getZhuLiList(cityId,pageNo,new Response.Listener<ZhuLi>(){
            @Override
            public void onResponse(ZhuLi response) {
                if(response!=null){
                    dialog.dismiss();
                    if (isRefresh) {
                        adapter.clear();
                        isRefresh = false;
                    }

                    adapter.addAll(response.body.services);
                    adapter.notifyDataSetChanged();
                    pageNo = response.body.next;
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ZhuLiActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }, "");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_mune_right:  // 出售服务
               /* Intent i=new Intent(this,AddServerActivity.class);
                startActivity(i);*/
                break;
        }
    }

    @Override
    public void onLoadMore() {
        if (pageNo == -1) {
            adapter.stopMore();
            return;
        }
        getZhuli();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isRefresh = true;
        getZhuli();
    }
}
