package com.abcs.haiwaigou.local.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.abcs.haiwaigou.local.beans.Menu;
import com.abcs.haiwaigou.view.MyGridView;
import com.abcs.huaqiaobang.adapter.CommonAdapter;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.capturePhoto.CapturePhoto;
import com.abcs.huaqiaobang.view.HqbViewHolder;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PicturesActivity extends BaseActivity {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.grid_pic)
    MyGridView gridPic;
    private ArrayList<String> filePath = new ArrayList<String>();
    CapturePhoto capturePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_pictures);
        ButterKnife.inject(this);
        capturePhoto=new CapturePhoto(this);
        filePath=getIntent().getStringArrayListExtra("listPath");
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initGrid();
    }

    private void initGrid() {
        gridPic.setAdapter(new CommonAdapter<String  >(this, filePath, R.layout.local_item_camera) {
            @Override
            public void convert(HqbViewHolder helper, String item, int position) {
                helper.setImageBitmap(R.id.photoshare_item_image, capturePhoto.getSmallBitmap(filePath.get(position)));
//                helper.setImageByUrl(R.id.photoshare_item_image, filePath.get(position));
//                helper.setImageResource(R.id.img_icon, drawables[position]);
            }
        });
        gridPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Toast.makeText(PublishActivity.this,menuList.get(position).getMenuName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
