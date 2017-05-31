package com.abcs.hqbtravel.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abcs.hqbtravel.entity.SanBiDetialsPic;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;


public class SanBiDetialsHoder extends BaseViewHolder<SanBiDetialsPic.BodyBean.CommentItem> {
    ImageView img_jingdian1;


    public SanBiDetialsHoder(ViewGroup parent) {
        super(parent, R.layout.item_sanbi);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MyApplication.getWidth(), MyApplication.getWidth()*550/697);
        img_jingdian1=(ImageView)itemView.findViewById(R.id.img_jingdian1);
        img_jingdian1.setLayoutParams(layoutParams);
        img_jingdian1.setScaleType(ImageView.ScaleType.FIT_XY);


    }

//    @Override
//    public void setData(DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity data) {
////        super.setData(data);
//
//        tv_name_jingdian1.setText(data.voiceName);
//        MyApplication.imageLoader.displayImage(data.imgUrl,img_jingdian1, MyApplication.options);
//    }


    @Override
    public void setData(SanBiDetialsPic.BodyBean.CommentItem data) {
//        super.setData(data);
        MyApplication.imageLoader.displayImage(data.oss_url,img_jingdian1, MyApplication.getAvatorOptions());
    }
}
