package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.abcs.hqbtravel.adapter.LookPhotosAdapter;
import com.abcs.hqbtravel.biz.LookPhotosBiz;
import com.abcs.hqbtravel.entity.LookPhotos;
import com.abcs.hqbtravel.wedgt.SpaceItemDecoration2;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class LookPhotosActivity extends Activity implements  View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener  {

    private EasyRecyclerView rv_XiangCe;
    private ImageView img_back;
    private LookPhotosAdapter adapter;
//    private List<TravelResponse.BodyEntity.BannersEntity> banners;
    private String cityId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_look_photos);


        cityId=getIntent().getStringExtra("cityId");

        rv_XiangCe=(EasyRecyclerView)findViewById(R.id.rv_xiangce);
        img_back=(ImageView)findViewById(R.id.img_back);

        img_back.setOnClickListener(this);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rv_XiangCe.addItemDecoration(new SpaceItemDecoration2(10));

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                /**
                 *  分几列，普通的只需要1，而headerview则需要1
                 */
                if (position == 0) {
                    return 1;
                } else {
                    return 1;
                }
            }
        });

        rv_XiangCe.setLayoutManager(manager);

//        rv_XiangCe.setRefreshListener(this);
        rv_XiangCe.setAdapter(adapter = new LookPhotosAdapter(this));

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                LookPhotos.BodyBean itemsEntity = adapter.getAllData().get(position);
                Intent it = new Intent(LookPhotosActivity.this, PhotoDetialsActivity.class);
                it.putExtra("url", itemsEntity.logo);
                it.putExtra("position", position+"");
                startActivity(it);
            }
        });

//        adapter.setNoMore(R.layout.view_nomore);
//        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });

        if(!TextUtils.isEmpty(cityId)){
            getDatas(cityId);
        }

    }

    private void getDatas(String cityId){
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        LookPhotosBiz.getInstance(this).getPhotosList(cityId,new Response.Listener<LookPhotos>(){
            @Override
            public void onResponse(LookPhotos response) {

                if(response!=null){
                    ProgressDlgUtil.stopProgressDlg();
                    if(response.result==1){
                        if(response.body!=null&&response.body.size()>0){
                            adapter.addAll(response.body);
                            adapter.notifyDataSetChanged();
                            MyApplication.banners=response.body ;
                        }
                    }

                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ProgressDlgUtil.stopProgressDlg();
            }
        },null );

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }
private int pageNo=1;
    @Override
    public void onLoadMore() {
        if (pageNo == -1) {
            adapter.stopMore();
            return;
        }
//        getBiChi();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
//        isRefresh = true;
//        getBiChi();
    }

}
