package com.abcs.hqbtravel.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class FuJinWanHoder extends BaseViewHolder<TouristAttractionsBean> {

    private ImageView img_yuding;
    private RelativeLayout re_instron;
    private ImageView img_logo;
    private TextView tv_location;
    private TextView tv_dis,tv_pin_number;
    private TextView tv_type_names;
    private TextView tv_type_distances;
    private TextView tv_pinfen,tv_instrution;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private TextView tv_price,tv_en_name;

    private List<ImageView> imgs=new ArrayList<>();


    public FuJinWanHoder(ViewGroup parent) {
        super(parent, R.layout.item_travle_bichi);

        img_yuding=(ImageView) itemView.findViewById(R.id.img_yuding);
        re_instron=(RelativeLayout) itemView.findViewById(R.id.re_instron);
        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
        tv_location=(TextView) itemView.findViewById(R.id.tv_location);
        tv_dis=(TextView) itemView.findViewById(R.id.tv_dis);
        tv_pin_number=(TextView) itemView.findViewById(R.id.tv_pin_number);
        tv_type_names=(TextView) itemView.findViewById(R.id.tv_type_names);
        tv_type_distances=(TextView) itemView.findViewById(R.id.tv_type_distances);
        tv_pinfen=(TextView) itemView.findViewById(R.id.tv_pinfen);
        tv_instrution=(TextView) itemView.findViewById(R.id.tv_instrution);

        tv_price = (TextView) itemView.findViewById(R.id.tv_price);  //价格
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
    public void setData(TouristAttractionsBean data) {
//        super.setData(data);

        MyApplication.imageLoader.displayImage(data.photo, img_logo, MyApplication.getCircleImageOptions());
        tv_location.setText(data.name+"");
        img_yuding.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(data.product_id)){
            if(data.product_id.equals("-1")){  //  -1 隐藏
                img_yuding.setVisibility(View.GONE);
            }else {
                img_yuding.setVisibility(View.VISIBLE);
            }
        }


        if(!TextUtils.isEmpty(data.english_name)){
            tv_en_name.setText(data.english_name);
        }

        if(!TextUtils.isEmpty(data.introduction)){
            re_instron.setVisibility(View.VISIBLE);
            tv_instrution.setVisibility(View.VISIBLE);
            tv_instrution.setText(data.introduction);
        }else {
            tv_instrution.setVisibility(View.GONE);
        }

        tv_dis.setText(data.been+"");
        tv_pin_number.setText(data.commentCount+"");
        tv_pinfen.setText(data.star+"");
        if(!TextUtils.isEmpty(data.distance)){

            if(!data.distance.equals("0m")){
                tv_type_distances.setVisibility(View.VISIBLE);
                tv_type_distances.setText(data.distance+"");
            }else {
                tv_type_distances.setVisibility(View.GONE);
            }

        }
        if (!TextUtils.isEmpty(data.price)){
            tv_price.setText("¥ "+data.price);
        }else{
            tv_price.setText("");
        }

//        "cates":"集市","美食夜市"

//        if(data.cates!=null&&data.cates.size()>0){
//            StringBuilder builder=new StringBuilder();
//            for (int i=0;i<data.cates.size();i++){
//                builder.append(data.cates.get(i)+" ");
//            }
//            tv_type_nas.setText(builder+"");
//        }
        if(!TextUtils.isEmpty(data.cate)){

            tv_type_names.setText(data.cate);
        }

        for(int i=0;i<imgs.size();i++){
            imgs.get(i).setImageResource(R.drawable.img_travel_startnoselect);
        }

        String str1 = (data.star+"").substring(0, (data.star+"").indexOf(".")) ;


        if(!TextUtils.isEmpty(str1)){
            int s1 = Integer.parseInt(str1);
            double xiaoshu=data.star-s1;

            for(int i=0;i<s1;i++){
                if(i<5){
                    imgs.get(i).setImageResource(R.drawable.img_travel_startselrct);

                    if((i+1)<5){
                        if(xiaoshu<0.5){
                            imgs.get(i+1).setImageResource(R.drawable.img_travel_startnoselect);
                        }else {
                            imgs.get(i+1).setImageResource(R.drawable.img_travel_startselrct_ban);
                        }
                    }
                }
            }

        }
    }
}
