package com.abcs.hqbtravel.holder;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.FindByTag;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class FindBytagHoder extends BaseViewHolder<FindByTag.BodyEntity.ListEntity> {

    private ImageView img_logo;
    private TextView tv_location;
    private TextView tv_dis,tv_pin_number;
    private TextView tv_type_names;
    private TextView tv_type_distances;
    private TextView tv_pinfen;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private TextView tv_en_name;

    private List<ImageView> imgs=new ArrayList<>();


    public FindBytagHoder(ViewGroup parent) {
        super(parent, R.layout.item_travle_bichi);

        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
        tv_location=(TextView) itemView.findViewById(R.id.tv_location);
        tv_dis=(TextView) itemView.findViewById(R.id.tv_dis);
        tv_pin_number=(TextView) itemView.findViewById(R.id.tv_pin_number);
        tv_type_names=(TextView) itemView.findViewById(R.id.tv_type_names);
//        tv_type_distances=(TextView) itemView.findViewById(R.id.tv_type_distances);
        tv_pinfen=(TextView) itemView.findViewById(R.id.tv_pinfen);
        tv_en_name = (TextView) itemView.findViewById(R.id.tv_en_name);  //英文名

        img1=(ImageView) itemView.findViewById(R.id.img1);
        img2=(ImageView) itemView.findViewById(R.id.img2);
        img3=(ImageView) itemView.findViewById(R.id.img3);
        img4=(ImageView) itemView.findViewById(R.id.img4);
        img5=(ImageView) itemView.findViewById(R.id.img5);

        imgs.add(img1);
        imgs.add(img2);
        imgs.add(img3);
        imgs.add(img4);
        imgs.add(img5);

    }

    @Override
    public void setData(FindByTag.BodyEntity.ListEntity data) {
//        super.setData(data);

        MyApplication.imageLoader.displayImage(data.photo, img_logo, MyApplication.getCircleImageOptions());
        tv_location.setText(data.chineseName+"");
        tv_dis.setText(data.beenNumber+"");
        tv_pin_number.setText(data.commentCount+"");
        if(data.grade>5){
            tv_pinfen.setText("5.0");
        }else {
            tv_pinfen.setText(data.grade+"");
        }
//        if(!TextUtils.isEmpty(data.distance)){
//            tv_type_distances.setVisibility(View.VISIBLE);
//            tv_type_distances.setText(data.distance+"");
//        }else {
//            tv_type_distances.setVisibility(View.GONE);
//        }
        tv_type_names.setText(data.cateName+"");

        if(!TextUtils.isEmpty(data.english_name)){
            tv_en_name.setText(data.english_name);
        }

        for(int i=0;i<imgs.size();i++){
            imgs.get(i).setImageResource(R.drawable.img_travel_startnoselect);
        }

        String str1 = (data.grade+"").substring(0, (data.grade+"").indexOf(".")) ;

        if(!TextUtils.isEmpty(str1)){
            int s1 = Integer.parseInt(str1);
            for(int i=0;i<s1;i++){
                if(i<5){
                    imgs.get(i).setImageResource(R.drawable.img_travel_startselrct);
                }
            }
        }
    }
}
