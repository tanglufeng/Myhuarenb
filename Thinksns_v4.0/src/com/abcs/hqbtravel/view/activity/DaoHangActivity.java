package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.abcs.hqbtravel.adapter.DaoHangAdapter2;
import com.abcs.hqbtravel.biz.DaoHangBiz;
import com.abcs.hqbtravel.entity.DaoHang2;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class DaoHangActivity extends Activity implements View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    private EasyRecyclerView rvDaoHang;
    private ImageView img_back;
    private DaoHangAdapter2 adapter;
    private int pageNo=1;
    private boolean isRefresh = false;
    private String cityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_daohang);


        cityId=getIntent().getStringExtra("cityId");
        rvDaoHang=(EasyRecyclerView)findViewById(R.id.rv_daohang);
        img_back=(ImageView)findViewById(R.id.img_back);

        img_back.setOnClickListener(this);


        rvDaoHang.setLayoutManager(new LinearLayoutManager(this));
        rvDaoHang.setRefreshListener(this);
        rvDaoHang.setRefreshingColorResources(R.color.tljr_statusbarcolor);
        rvDaoHang.setAdapter(adapter = new DaoHangAdapter2(this));
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                DaoHang2.BodyEntity.ItemsEntity itemsEntity = adapter.getAllData().get(position);
                Intent it = new Intent(DaoHangActivity.this, DaoHangDetialsActivity2.class);
                it.putExtra("dahangId", itemsEntity.id);
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
            getDaoHang();
        }
    }

    private String pageSize="10";
    private void getDaoHang() {

//        DaoHangBiz.getInstance(this).getDaoHangList("5044",pageNo+"",pageSize,new Response.Listener<DaoHang>(){
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
                        adapter.addAll(response.body.items);
                        adapter.notifyDataSetChanged();
                        pageNo = response.body.next;
                    }

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDlgUtil.stopProgressDlg();
                Toast.makeText(DaoHangActivity.this,"无数据",Toast.LENGTH_LONG).show();
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
    }
}
