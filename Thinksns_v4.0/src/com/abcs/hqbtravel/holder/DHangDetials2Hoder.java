package com.abcs.hqbtravel.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.DaohangDetials2;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;


public class DHangDetials2Hoder extends BaseViewHolder<DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity> {
    ImageView img_jingdian1;
    TextView tv_name_jingdian1;


    public DHangDetials2Hoder(ViewGroup parent) {
        super(parent, R.layout.item_daohang_detials2);

        img_jingdian1=(ImageView)itemView.findViewById(R.id.img_jingdian1);
        tv_name_jingdian1=(TextView)itemView.findViewById(R.id.tv_name_jingdian1);

    }

    @Override
    public void setData(DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity data) {
//        super.setData(data);

        tv_name_jingdian1.setText(data.voiceName);
        MyApplication.imageLoader.displayImage(data.imgUrl,img_jingdian1, MyApplication.getCircleImageOptions());
    }
}
