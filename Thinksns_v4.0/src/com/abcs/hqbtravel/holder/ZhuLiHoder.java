package com.abcs.hqbtravel.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.ZhuLi;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.view.CircleImageView;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/9/8.
 */
public class ZhuLiHoder extends BaseViewHolder<ZhuLi.BodyEntity.ServicesEntity> {

    private ImageView img_fuqu1;
    private CircleImageView img_fuqu1_avatar;
    private TextView tv_house;
    private TextView tv_fuqu1_title;
    private TextView tv_fuqu1_location;
    private TextView tv_fuqu1_type;
    private TextView tv_fuqu1_dianpin;
    private TextView tv_fuqu1_name;
    private TextView tv_fuqu1_isfamel;
    private TextView tv_fuqu1_age;
    private TextView tv_fuqu1_isvip;





    public ZhuLiHoder(ViewGroup parent) {
        super(parent, R.layout.item_travle_zhu_li);
        img_fuqu1=(ImageView) itemView.findViewById(R.id.img_fuqu_logo);
        img_fuqu1_avatar= (CircleImageView) itemView.findViewById(R.id.img_fuqu1_avatar);
        tv_house=(TextView) itemView.findViewById(R.id.tv_house);
        tv_fuqu1_title=(TextView) itemView.findViewById(R.id.tv_fuqu1_title);
        tv_fuqu1_location=(TextView) itemView.findViewById(R.id.tv_fuqu1_location);
        tv_fuqu1_type=(TextView) itemView.findViewById(R.id.tv_fuqu1_type);
        tv_fuqu1_dianpin=(TextView) itemView.findViewById(R.id.tv_fuqu1_dianpin);
        tv_fuqu1_name=(TextView) itemView.findViewById(R.id.tv_fuqu1_name);
        tv_fuqu1_isfamel=(TextView) itemView.findViewById(R.id.tv_fuqu1_isfamel);
        tv_fuqu1_age=(TextView) itemView.findViewById(R.id.tv_fuqu1_age);
        tv_fuqu1_isvip=(TextView) itemView.findViewById(R.id.tv_fuqu1_isvip);
    }

    @Override
    public void setData(ZhuLi.BodyEntity.ServicesEntity data) {
        MyApplication.imageLoader.displayImage(data.logo, img_fuqu1, MyApplication.options);
        MyApplication.imageLoader.displayImage(data.assistants.avator, img_fuqu1_avatar, MyApplication.getAvatorOptions());
        tv_fuqu1_type.setText(data.type+"");
        tv_fuqu1_title.setText(data.name+"");
        tv_fuqu1_name.setText(data.assistants.name+"");
        tv_fuqu1_age.setText(data.assistants.age+"");
        if(data.assistants.sex==0){    // 0  男  1  女
            tv_fuqu1_isfamel.setBackgroundResource(R.drawable.img_travel_sexm);
        }else{
            tv_fuqu1_isfamel.setBackgroundResource(R.drawable.img_travel_sexf);
        }
        tv_fuqu1_dianpin.setText(data.comments+"点评");
        tv_house.setText(data.price+"");
    }
}
