package com.abcs.hqbtravel.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.HuiYuanTeDian;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/9/8.
 */
public class TravelTeDianHoder extends BaseViewHolder<HuiYuanTeDian.VipTdListBean> {

    ImageView mImg;
    TextView mTxtDesc;


    public TravelTeDianHoder(ViewGroup parent) {
        super(parent, R.layout.item_travle_tedian);

       mImg = (ImageView) itemView.findViewById(R.id.img_third);
        mTxtDesc = (TextView) itemView.findViewById(R.id.text);


    }

    @Override
    public void setData(HuiYuanTeDian.VipTdListBean data) {

        ImageLoader.getInstance().displayImage(data.img,mImg, Options.getListOptions());
        mTxtDesc.setText(data.tdName);
    }
}
