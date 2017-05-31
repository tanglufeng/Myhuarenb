package com.abcs.hqbtravel.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.DaoHang2;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

public class DaoHangHoder2 extends BaseViewHolder<DaoHang2.BodyEntity.ItemsEntity> {

    private ImageView img_logo;
    private TextView tv_youji_cn;
    private TextView tv_english;
    private TextView tv_yuyin_numbers;
    private TextView tv_yuyin_sizes;

    public DaoHangHoder2(ViewGroup parent) {
        super(parent, R.layout.item_travle_daohang2);

        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
        tv_youji_cn=(TextView) itemView.findViewById(R.id.tv_youji_cn);
        tv_english=(TextView) itemView.findViewById(R.id.tv_english);
        tv_yuyin_numbers=(TextView) itemView.findViewById(R.id.tv_yuyin_numbers);
        tv_yuyin_sizes=(TextView) itemView.findViewById(R.id.tv_yuyin_sizes);

    }

    @Override
    public void setData( DaoHang2.BodyEntity.ItemsEntity data) {
//        super.setData(data);

        MyApplication.imageLoader.displayImage(data.logo, img_logo, MyApplication.getCircleImageOptions());
        tv_youji_cn.setText(data.name + "");
        tv_english.setText(data.sightEnName + "");
        tv_yuyin_numbers.setText("含" + data.voiceCount + "处语音介绍");
        tv_yuyin_sizes.setText(data.voiceSize + "");
    }
}
