package com.abcs.hqbtravel.holder;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.RestauransBean;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/9/8.
 */
public class MoreFuJinChiHoder extends BaseViewHolder<RestauransBean> {


    private ImageView img_logo;
    private TextView tv_distancess,tv_myname;

    public MoreFuJinChiHoder(ViewGroup parent) {
        super(parent, R.layout.item_travle_dujinsbi);

         img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
         tv_distancess=(TextView) itemView.findViewById(R.id.tv_distancess);
         tv_myname=(TextView) itemView.findViewById(R.id.tv_myname);

//        img_logo.setLayoutParams(new RelativeLayout.LayoutParams(MyApplication.getWidth()/3, RelativeLayout.LayoutParams.WRAP_CONTENT));
        
        
    }

    @Override
    public void setData(RestauransBean data) {
//        super.setData(data);

        MyApplication.imageLoader.displayImage(data.photo, img_logo, MyApplication.options);

        if( !TextUtils.isEmpty(data.name)){

            tv_myname.setText(data.name);
        }
        if( !TextUtils.isEmpty(data.distance)){

            tv_distancess.setText(data.distance);
        }
    }
}
