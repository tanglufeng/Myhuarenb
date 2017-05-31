package com.abcs.hqbtravel.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.DaoHang;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/9/8.
 */
public class DaoHangHoder extends BaseViewHolder<DaoHang.BodyEntity.ItemsEntity> {

    private ImageView img_logo;
    private ImageView img_bofang;
    private TextView tv_title;
    private TextView tv_youji_numbers;

    public DaoHangHoder(ViewGroup parent) {
        super(parent, R.layout.item_travle_daohang);

        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
        img_bofang=(ImageView) itemView.findViewById(R.id.img_bofang);
        tv_title=(TextView) itemView.findViewById(R.id.tv_title);
        tv_youji_numbers=(TextView) itemView.findViewById(R.id.tv_youji_numbers);
    }

    @Override
    public void setData(DaoHang.BodyEntity.ItemsEntity data) {
//        super.setData(data);

        MyApplication.imageLoader.displayImage(data.logo, img_logo, MyApplication.options);
        tv_title.setText(data.name+"");
        tv_youji_numbers.setText(data.been+"");
    }
}
