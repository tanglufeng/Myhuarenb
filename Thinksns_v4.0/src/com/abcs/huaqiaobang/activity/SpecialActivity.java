package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.fragment.adapter.HWGFragmentAdapter;
import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.presenter.LoadDataInterFace;
import com.abcs.huaqiaobang.presenter.Special;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SpecialActivity extends AppCompatActivity implements LoadDataInterFace, HWGFragmentViewHolder.ItemOnClick {


    @InjectView(R.id.image)
    ImageView image;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<Goods> goodses;
    private HWGFragmentAdapter hwgFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.img_fanhui);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbar.setTitle("购物帮");
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        hwgFragmentAdapter = new HWGFragmentAdapter(this,this, true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(hwgFragmentAdapter);


        Special special = new Special(this);
        special.loadData(getIntent().getStringExtra("special_id"));
        ImageLoader.getInstance().displayImage(getIntent().getStringExtra("picture"), image);

    }

    @Override
    public void loadSuccess(List mData) {

//        goodses.addAll(mData);
        hwgFragmentAdapter.getDatas().addAll(mData);
        hwgFragmentAdapter.notifyDataSetChanged();


    }

    @Override
    public void loadFailed(String error) {

    }

    @Override
    public void onItemRootViewClick(int position) {

        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("sid", hwgFragmentAdapter.getDatas().get(position).getGoods_id());
        intent.putExtra("pic", hwgFragmentAdapter.getDatas().get(position).getPicarr());
        startActivity(intent);

    }
}
