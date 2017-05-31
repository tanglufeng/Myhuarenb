package com.abcs.hqbtravel.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abcs.hqbtravel.entity.LookPhotos;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/9/8.
 */
public class LookPhotosHoder extends BaseViewHolder<LookPhotos.BodyBean> {


    private ImageView img;

    public LookPhotosHoder(ViewGroup parent) {
        super(parent, R.layout.item_travle_showphoto);

        img=(ImageView) itemView.findViewById(R.id.imge);
        img.setLayoutParams(new LinearLayout.LayoutParams(MyApplication.getWidth()/2, MyApplication.getHeight()/3));
    }

    @Override
    public void setData(LookPhotos.BodyBean data) {
//        super.setData(data);

        MyApplication.imageLoader.displayImage(data.logo, img, MyApplication.options);
    }
}
