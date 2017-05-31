package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.adapter.MoreFuJinChiAdapter;
import com.abcs.hqbtravel.adapter.MoreFuJinMaiAdapter;
import com.abcs.hqbtravel.adapter.MoreFuJinWanAdapter;
import com.abcs.hqbtravel.entity.RestauransBean;
import com.abcs.hqbtravel.entity.ShopBean;
import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.hqbtravel.wedgt.SpaceItemDecoration2;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class MoreFuJinSBiActivity extends Activity implements  View.OnClickListener{

    private EasyRecyclerView rv_XiangCe;
    private ImageView img_back;
    private TextView tv_title;

    private MoreFuJinChiAdapter chiAdapter;

    private MoreFuJinWanAdapter wanAdapter;

    private MoreFuJinMaiAdapter maiAdapter;

    private String type;
    private String cityId;
    private Handler mhandle=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_more_fujinsbi);

        cityId=getIntent().getStringExtra("cityId");
        type=getIntent().getStringExtra("type");

        ProgressDlgUtil.showProgressDlg("Loading...", this);
        rv_XiangCe=(EasyRecyclerView)findViewById(R.id.rv_xiangce);
        tv_title=(TextView) findViewById(R.id.tv_title);
        img_back=(ImageView)findViewById(R.id.img_back);

        img_back.setOnClickListener(this);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rv_XiangCe.addItemDecoration(new SpaceItemDecoration2(4));

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

        if(!TextUtils.isEmpty(type)){
            if(type.equals(Contonst.KEY_CHI)){   /*******更多必吃*********/
                    initBiChi();
            }else  if(type.equals(Contonst.KEY_WAN)){/*******更多必玩*********/
                initBiWan();
            }else {/*******更多必买*********/
                initBiMai();
            }
        }

        mhandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressDlgUtil.stopProgressDlg();
            }
        }, 2000);
    }


    private void initBiChi(){
        tv_title.setText("下一站必吃");
        chiAdapter = new MoreFuJinChiAdapter(this);
        rv_XiangCe.setAdapter(chiAdapter);

        chiAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RestauransBean itemsEntity = chiAdapter.getAllData().get(position);
                Intent it = new Intent(MoreFuJinSBiActivity.this, BiChiDetialsActivity.class);
                it.putExtra("chiId", itemsEntity.id);
                it.putExtra("cityId", cityId);
                it.putExtra("chiphoto", itemsEntity.photo);
                startActivity(it);
            }
        });

        chiAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chiAdapter.resumeMore();
            }
        });

        if(MyApplication.restaurantsBeen!=null&&MyApplication.restaurantsBeen.size()>0){

            chiAdapter.addAll(MyApplication.restaurantsBeen);
            chiAdapter.notifyDataSetChanged();
        }


    }

    private void initBiWan(){
        tv_title.setText("下一站必玩");
        wanAdapter = new MoreFuJinWanAdapter(this);
        rv_XiangCe.setAdapter(wanAdapter);

        wanAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TouristAttractionsBean itemsEntity = wanAdapter.getAllData().get(position);
                Intent it = new Intent(MoreFuJinSBiActivity.this, BiWanDetialsActivity.class);
                it.putExtra("bwanId", itemsEntity.id);
                it.putExtra("cityId", cityId);
                it.putExtra("photo", itemsEntity.photo);
                startActivity(it);
            }
        });

        wanAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wanAdapter.resumeMore();
            }
        });
        if(MyApplication.touristAttractionsBeen!=null&&MyApplication.touristAttractionsBeen.size()>0){

            wanAdapter.addAll(MyApplication.touristAttractionsBeen);
            wanAdapter.notifyDataSetChanged();
        }
    }

    private void initBiMai(){
        tv_title.setText("下一站必买");
        maiAdapter = new MoreFuJinMaiAdapter(this);
        rv_XiangCe.setAdapter(maiAdapter);

        maiAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ShopBean itemsEntity = maiAdapter.getAllData().get(position);
                Intent it = new Intent(MoreFuJinSBiActivity.this, BiMaiDetialsActivity.class);
                it.putExtra("shopId", itemsEntity.id);
                it.putExtra("cityId", cityId);
                it.putExtra("photo", itemsEntity.photo);
                startActivity(it);
            }
        });

        maiAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maiAdapter.resumeMore();
            }
        });

        if(MyApplication.shopsBeen!=null&&MyApplication.shopsBeen.size()>0){

            maiAdapter.addAll(MyApplication.shopsBeen);
            maiAdapter.notifyDataSetChanged();
        }

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
