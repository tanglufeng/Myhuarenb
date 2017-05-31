package com.abcs.haiwaigou.local.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.local.beans.Menu;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.adapter.PublishGridAdapter;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PublishActivity extends BaseActivity implements View.OnClickListener{
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_my_publish)
    RelativeLayout relativeMyPublish;
    @InjectView(R.id.grid_menu)
    GridView gridMenu;

    private String cityId,menuId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_publish);
        ButterKnife.inject(this);
        ArrayList<Menu> menuList=getIntent().getParcelableArrayListExtra("menuList");
        cityId=getIntent().getStringExtra("cityId");
        menuId=getIntent().getStringExtra("menuId");
        initGrid(menuList);
        initListener();
    }

    private void initListener() {
        relativeBack.setOnClickListener(this);
        relativeMyPublish.setOnClickListener(this);
    }

    PublishGridAdapter gridAdapter;
    private void initGrid(ArrayList<Menu> menuList) {
        gridAdapter=new PublishGridAdapter(this,menuList);
        gridMenu.setAdapter(gridAdapter);

        gridMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Menu menu=(Menu)gridAdapter.getItem(position);

                if(menu.getIs_show()==1){

                    showToast("该项暂不支持发布");

                }else {
                    Intent intent=new Intent(PublishActivity.this,PublishMessageActivity.class);
                    intent.putExtra("cityId",cityId);
                    intent.putExtra("menuId",menu.getMenuId());
                    intent.putExtra("title",menu.getMenuName());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.relative_back:
                finish();
                break;
            case R.id.relative_my_publish:
                if(MyApplication.getInstance().self!=null){
                    intent=new Intent(this,MyPublishActivity.class);
                    intent.putExtra("menuId",menuId);
                }else {
                    intent=new Intent(this, WXEntryActivity.class);
                    intent.putExtra("isthome",true);
                }
                startActivity(intent);
                break;
        }
    }
}
