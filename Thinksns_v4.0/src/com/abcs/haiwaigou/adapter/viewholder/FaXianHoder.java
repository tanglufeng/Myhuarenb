package com.abcs.haiwaigou.adapter.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abcs.haiwaigou.model.FaXian;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 */
public class FaXianHoder extends BaseViewHolder<FaXian.DatasBean> {

    private ImageView img_logo;
    private LinearLayout liner;


    public FaXianHoder(ViewGroup parent) {
        super(parent, R.layout.item_hwg_pic);

        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
        liner=(LinearLayout) itemView.findViewById(R.id.liner);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.WIDTH, Util.WIDTH*33/75);
        liner.setLayoutParams(layoutParams);

    }

    @Override
    public void setData(FaXian.DatasBean data) {

        MyApplication.imageLoader.displayImage(data.image, img_logo, MyApplication.getCircleImageOptions());

    }
}
