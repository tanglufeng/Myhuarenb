package com.abcs.hqbtravel.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/9/8.
 */
public class BiWanHoder extends BaseViewHolder<TouristAttractionsBean> {

    private ImageView img_logo;
    private TextView tv_con;
    private TextView tv_dis;

    public BiWanHoder(ViewGroup parent) {
        super(parent, R.layout.item_travle_biwan);
        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
        tv_con=(TextView) itemView.findViewById(R.id.tv_con);
        tv_dis=(TextView) itemView.findViewById(R.id.tv_dis);


    }

    @Override
    public void setData(TouristAttractionsBean data) {

        MyApplication.imageLoader.displayImage(data.photo, img_logo, MyApplication.options);
        tv_con.setText(data.name+"");
        if(!TextUtils.isEmpty(data.cate)){
            tv_dis.setVisibility(View.VISIBLE);
            tv_dis.setText(data.cate+"");
        }
    }
}
